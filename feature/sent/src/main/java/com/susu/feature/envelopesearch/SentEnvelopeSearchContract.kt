package com.susu.feature.envelopesearch

import com.susu.core.model.SearchEnvelope
import com.susu.core.ui.base.SideEffect
import com.susu.core.ui.base.UiState
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

data class EnvelopeSearchState(
    val searchKeyword: String = "",
    val recentSearchKeywordList: PersistentList<String> = persistentListOf(),
    val envelopeList: PersistentList<SearchEnvelope> = persistentListOf(),
) : UiState

sealed interface EnvelopeSearchEffect : SideEffect {
    data object PopBackStack : EnvelopeSearchEffect
    data class NavigateEnvelopDetail(val envelopeId: Long) : EnvelopeSearchEffect
    data object FocusClear : EnvelopeSearchEffect
    data object LogBackClickEvent : EnvelopeSearchEffect
    data object LogSearchResultClickEvent : EnvelopeSearchEffect
}
