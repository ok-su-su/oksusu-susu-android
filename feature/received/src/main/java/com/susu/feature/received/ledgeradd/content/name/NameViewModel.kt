package com.susu.feature.received.ledgeradd.content.name

import androidx.lifecycle.viewModelScope
import com.susu.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NameViewModel @Inject constructor() : BaseViewModel<NameState, NameSideEffect>(
    NameState(),
) {
    fun updateName(name: String) {
        if (name.length > 10) {
            postSideEffect(NameSideEffect.ShowNotValidSnackbar)
            return
        }

        intent {
            postSideEffect(NameSideEffect.UpdateParentName(name.trim()))
            copy(name = name)
        }
    }

    fun showKeyboardIfTextEmpty() = viewModelScope.launch {
        if (currentState.name.isEmpty()) {
            delay(400L)
            postSideEffect(NameSideEffect.ShowKeyboard)
        }
    }
}
