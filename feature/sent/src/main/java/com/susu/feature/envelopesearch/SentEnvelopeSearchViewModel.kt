package com.susu.feature.envelopesearch

import androidx.lifecycle.viewModelScope
import com.susu.core.ui.base.BaseViewModel
import com.susu.domain.usecase.envelope.SearchSentEnvelopeListUseCase
import com.susu.domain.usecase.enveloperecentsearch.DeleteEnvelopeRecentSearchUseCase
import com.susu.domain.usecase.enveloperecentsearch.GetEnvelopeRecentSearchListUseCase
import com.susu.domain.usecase.enveloperecentsearch.UpsertEnvelopeRecentSearchUseCase
import com.susu.domain.usecase.friend.SearchFriendUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SentEnvelopeSearchViewModel @Inject constructor(
    private val getEnvelopeRecentSearchUserCase: GetEnvelopeRecentSearchListUseCase,
    private val deleteEnvelopeRecentSearchUseCase: DeleteEnvelopeRecentSearchUseCase,
    private val upsertEnvelopeRecentSearchUseCase: UpsertEnvelopeRecentSearchUseCase,
    private val searchFriendUseCase: SearchFriendUseCase,
    private val searchSentEnvelopeListUseCase: SearchSentEnvelopeListUseCase,
) : BaseViewModel<EnvelopeSearchState, EnvelopeSearchEffect>(EnvelopeSearchState()) {

    fun navigateToEnvelopeDetail(id: Long) {
        postSideEffect(EnvelopeSearchEffect.NavigateEnvelopDetail(id))
    }

    fun getEnvelopeRecentSearchList() {
        viewModelScope.launch {
            getEnvelopeRecentSearchUserCase().onSuccess(::updateRecentSearchList)
        }
    }

    fun deleteEnvelopeRecentSearch(search: String) {
        viewModelScope.launch {
            deleteEnvelopeRecentSearchUseCase(search).onSuccess(::updateRecentSearchList)
        }
    }

    fun upsertEnvelopeRecentSearch(search: String) {
        viewModelScope.launch {
            upsertEnvelopeRecentSearchUseCase(search).onSuccess(::updateRecentSearchList)
        }
    }

    fun updateSearchKeyword(search: String) = intent {
        copy(
            searchKeyword = search,
            envelopeList = if (search.isBlank()) persistentListOf() else envelopeList,
        )
    }

    fun getEnvelopeList(search: String) = viewModelScope.launch {
        if (search.isEmpty()) return@launch

        val searchedFriends = searchFriendUseCase(search).getOrThrow()

        // 친구 검색 결과가 존재하면 봉투 검색
        val envelopesByFriend = async {
            if (searchedFriends.isNotEmpty()) {
                searchSentEnvelopeListUseCase(
                    param = SearchSentEnvelopeListUseCase.Param(
                        friendIds = searchedFriends.map { it.friend.id.toInt() },
                    ),
                ).getOrDefault(emptyList())
            } else {
                emptyList()
            }
        }

        // 숫자 형식일 경우는 금액으로 봉투 검색
        val envelopesByAmount = async {
            search.toLongOrNull()?.let { amount ->
                searchSentEnvelopeListUseCase(
                    param = SearchSentEnvelopeListUseCase.Param(
                        fromAmount = amount,
                        toAmount = amount,
                    ),
                ).getOrDefault(emptyList())
            } ?: emptyList()
        }

        // 두가지 조건을 검색 완료 시 결과를 통합 표시
        val result = awaitAll(envelopesByFriend, envelopesByAmount).flatten()
        intent { copy(envelopeList = result.toPersistentList()) }
    }

    private fun updateRecentSearchList(searchList: List<String>) {
        intent {
            copy(recentSearchKeywordList = searchList.toPersistentList())
        }
    }

    fun clearFocus() = postSideEffect(EnvelopeSearchEffect.FocusClear)
    fun popBackStack() {
        postSideEffect(EnvelopeSearchEffect.PopBackStack)
    }

    fun logBackClickEvent() {
        postSideEffect(EnvelopeSearchEffect.LogBackClickEvent)
    }

    fun logSearchResultClickEvent() {
        postSideEffect(EnvelopeSearchEffect.LogSearchResultClickEvent)
    }
}
