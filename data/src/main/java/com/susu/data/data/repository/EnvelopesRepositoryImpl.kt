package com.susu.data.data.repository

import com.susu.core.model.FriendStatistics
import com.susu.data.remote.api.EnvelopesService
import com.susu.data.remote.model.response.toModel
import com.susu.domain.repository.EnvelopesRepository
import javax.inject.Inject

class EnvelopesRepositoryImpl @Inject constructor(
    private val envelopesService: EnvelopesService,
) : EnvelopesRepository {
    override suspend fun getEnvelopesList(
        friendIds: List<Int>?,
        fromTotalAmounts: Int?,
        toTotalAmounts: Int?,
        page: Int?,
        size: Int?,
        sort: String?,
    ): List<FriendStatistics> = envelopesService.getEnvelopesList(
        friendIds = friendIds,
        fromTotalAmounts = fromTotalAmounts,
        toTotalAmounts = toTotalAmounts,
        page = page,
        size = size,
        sort = sort,
    ).getOrThrow().toModel()
}
