package com.susu.feature.received.envelopeadd.content.memo

import com.susu.core.ui.base.SideEffect
import com.susu.core.ui.base.UiState

data class MemoState(
    val memo: String = "",
) : UiState

sealed interface MemoSideEffect : SideEffect {
    data class UpdateParentMemo(val memo: String?) : MemoSideEffect
    data object ShowKeyboard : MemoSideEffect
    data object ShowNotValidSnackbar : MemoSideEffect
}
