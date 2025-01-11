package com.susu.feature.envelopeadd.content.present

import androidx.lifecycle.viewModelScope
import com.susu.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PresentViewModel @Inject constructor() : BaseViewModel<PresentState, PresentSideEffect>(
    PresentState(),
) {
    fun updatePresent(present: String?) {
        if (present != null) {
            if (present.length > 30) {
                postSideEffect(PresentSideEffect.ShowNotValidSnackbar)
                return
            }
        }

        intent {
            postSideEffect(PresentSideEffect.UpdateParentPresent(present?.trim()))
            copy(present = present ?: "")
        }
    }

    fun showKeyboardIfTextEmpty() = viewModelScope.launch {
        if (currentState.present.isEmpty()) {
            delay(400L)
            postSideEffect(PresentSideEffect.ShowKeyboard)
        }
    }
}
