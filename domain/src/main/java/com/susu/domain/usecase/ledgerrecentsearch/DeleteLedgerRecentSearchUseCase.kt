package com.susu.domain.usecase.ledgerrecentsearch

import com.susu.core.common.runCatchingIgnoreCancelled
import com.susu.domain.repository.LedgerRecentSearchRepository
import javax.inject.Inject

class DeleteLedgerRecentSearchUseCase @Inject constructor(
    private val ledgerRecentSearchRepository: LedgerRecentSearchRepository,
) {
    suspend operator fun invoke(search: String) = runCatchingIgnoreCancelled {
        ledgerRecentSearchRepository.deleteBySearch(search)
    }
}
