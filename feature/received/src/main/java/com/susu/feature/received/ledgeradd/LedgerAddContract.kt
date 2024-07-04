package com.susu.feature.received.ledgeradd

import com.susu.core.model.Ledger
import com.susu.core.ui.base.SideEffect
import com.susu.core.ui.base.UiState

data class LedgerAddState(
    val currentStep: LedgerAddStep = LedgerAddStep.CATEGORY,
    val buttonEnabled: Boolean = false,
    val isLoading: Boolean = false,
) : UiState

enum class LedgerAddStep {
    CATEGORY,
    NAME,
    DATE,
}

sealed interface LedgerAddSideEffect : SideEffect {
    data object HideKeyboard : LedgerAddSideEffect
    data object PopBackStack : LedgerAddSideEffect
    data class NavigateLedgerDetail(val ledger: Ledger) : LedgerAddSideEffect
    data class HandleException(val throwable: Throwable, val retry: () -> Unit) : LedgerAddSideEffect
}
