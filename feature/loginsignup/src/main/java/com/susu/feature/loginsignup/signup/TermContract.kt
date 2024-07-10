package com.susu.feature.loginsignup.signup

import com.susu.core.model.Term
import com.susu.core.model.TermDetail
import com.susu.core.ui.base.SideEffect
import com.susu.core.ui.base.UiState
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

sealed interface TermEffect : SideEffect {
    data class ShowToast(val msg: String) : TermEffect
}

data class TermState(
    val isLoading: Boolean = false,
    val terms: PersistentList<Term> = persistentListOf(),
    val currentTerm: TermDetail = TermDetail(0, "", false, ""),
) : UiState
