package com.susu.data.remote.api

import com.susu.data.remote.model.request.ForceUpdateRequest
import com.susu.data.remote.retrofit.ApiResult
import retrofit2.http.GET
import retrofit2.http.Query

interface VersionService {
    @GET("metadata/version")
    suspend fun checkForceUpdate(
        @Query("deviceOS") os: String,
        @Query("version") version: String,
    ): ApiResult<ForceUpdateRequest>
}
