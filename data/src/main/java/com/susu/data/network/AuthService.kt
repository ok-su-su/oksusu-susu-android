package com.susu.data.network

import com.susu.data.model.TokenEntity
import com.susu.data.model.request.RefreshTokenRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/token/refresh")
    suspend fun refreshAccessToken(
        @Body refreshTokenRequest: RefreshTokenRequest,
    ): Response<TokenEntity>
}
