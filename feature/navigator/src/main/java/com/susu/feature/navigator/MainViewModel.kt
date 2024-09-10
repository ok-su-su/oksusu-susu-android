package com.susu.feature.navigator

import androidx.lifecycle.viewModelScope
import com.susu.core.android.throwUnknownException
import com.susu.core.model.exception.NetworkException
import com.susu.core.ui.DialogToken
import com.susu.core.ui.SnackbarToken
import com.susu.core.ui.SnsProviders
import com.susu.core.ui.base.BaseViewModel
import com.susu.domain.usecase.categoryconfig.GetCategoryConfigUseCase
import com.susu.domain.usecase.loginsignup.CheckCanRegisterUseCase
import com.susu.domain.usecase.loginsignup.CheckShowOnboardVoteUseCase
import com.susu.domain.usecase.loginsignup.LoginUseCase
import com.susu.domain.usecase.version.CheckForceUpdateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val checkCanRegisterUseCase: CheckCanRegisterUseCase,
    private val checkShowOnboardVoteUseCase: CheckShowOnboardVoteUseCase,
    private val getCategoryConfigUseCase: GetCategoryConfigUseCase,
    private val checkForceUpdateUseCase: CheckForceUpdateUseCase,
) : BaseViewModel<MainState, MainSideEffect>(MainState()) {
    companion object {
        private const val NAVIGATE_DELAY = 500L
        private const val SHOW_TOAST_LENGTH = 2750L
    }

    private val mutex = Mutex()

    fun onShowSnackbar(snackbarToken: SnackbarToken) = viewModelScope.launch {
        if (snackbarToken == currentState.snackbarToken) return@launch

        mutex.withLock {
            intent { copy(snackbarToken = snackbarToken, snackbarVisible = true) }
            delay(SHOW_TOAST_LENGTH)
            intent { copy(snackbarVisible = false) }
        }
    }

    fun onShowDialog(dialogToken: DialogToken) {
        intent { copy(dialogToken = dialogToken, dialogVisible = true) }
    }

    fun dismissDialog() {
        intent { copy(dialogVisible = false) }
    }

    fun handleException(throwable: Throwable, retry: () -> Unit) = when (throwable) {
        is NetworkException -> postSideEffect(MainSideEffect.ShowNetworkErrorSnackbar(retry))
        is UnknownHostException -> postSideEffect(MainSideEffect.ShowNetworkErrorSnackbar(retry))
        else -> throwUnknownException(throwable)
    }

    fun initCategoryConfig() = viewModelScope.launch {
        getCategoryConfigUseCase()
            .onFailure { }
        intent { copy(isInitializing = false) }
    }

    fun navigate(kakaoAccessToken: String?) = viewModelScope.launch {
        if (checkShowOnboardVoteUseCase() == null) {
            intent { copy(isNavigating = false) }
            return@launch
        }

        if (kakaoAccessToken == null) {
            postSideEffect(MainSideEffect.NavigateLogin)
            intent { copy(isNavigating = false) }
            return@launch
        }

        checkCanRegisterUseCase(provider = SnsProviders.Kakao.path, oauthAccessToken = kakaoAccessToken)
            .onSuccess { canRegister ->
                handleCanRegisterSuccess(
                    canRegister = canRegister,
                    kakaoAccessToken = kakaoAccessToken,
                )
            }
        delay(NAVIGATE_DELAY)
        intent { copy(isNavigating = false) }
    }

    private suspend fun handleCanRegisterSuccess(canRegister: Boolean, kakaoAccessToken: String) {
        if (canRegister) {
            postSideEffect(MainSideEffect.NavigateSignup)
        } else {
            login(kakaoAccessToken)
        }
    }

    fun checkForceUpdate(versionName: String) {
        viewModelScope.launch {
            checkForceUpdateUseCase(versionName).onSuccess { needed ->
                // TODO: 하드코딩 제거
                if (needed) {
                    onShowDialog(
                        DialogToken(
                            title = "업데이트가 필요해요",
                            text = "새로운 버전의 수수를 다운로드해주세요",
                            confirmText = "스토어로 이동하기",
                            onConfirmRequest = {
                                postSideEffect(MainSideEffect.NavigatePlayStore)
                            },
                        ),
                    )
                }
            }
        }
    }

    private suspend fun login(oauthAccessToken: String) {
        loginUseCase(provider = SnsProviders.Kakao.path, oauthAccessToken = oauthAccessToken)
            .onSuccess {
                postSideEffect(MainSideEffect.NavigateSent)
            }.onFailure {
                postSideEffect(MainSideEffect.NavigateLogin)
            }
    }
}
