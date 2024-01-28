package com.susu.data.remote.api

import com.susu.data.remote.model.request.EnvelopeRequest
import com.susu.data.remote.model.response.EnvelopeResponse
import com.susu.data.remote.model.response.EnvelopesListResponse
import com.susu.data.remote.model.response.RelationShipListResponse
import com.susu.data.remote.retrofit.ApiResult
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface EnvelopesService {
    @GET("envelopes/friend-statistics")
    suspend fun getEnvelopesList(
        @Query("friendIds") friendIds: List<Int>?,
        @Query("fromTotalAmounts") fromTotalAmounts: Int?,
        @Query("toTotalAmounts") toTotalMounts: Int?,
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("sort") sort: String?,
    ): ApiResult<EnvelopesListResponse>

    @GET("envelopes/configs/create-envelopes")
    suspend fun getRelationShipConfigList(): ApiResult<RelationShipListResponse>

    @POST("envelopes")
    suspend fun createEnvelope(
        @Body envelopeRequest: EnvelopeRequest,
    ): ApiResult<EnvelopeResponse>
}