package com.susu.data.repository

import com.susu.core.model.User
import com.susu.data.model.SnsProviders
import com.susu.data.model.request.AccessTokenRequest
import com.susu.data.model.toData
import com.susu.data.model.toDomain
import com.susu.data.network.ApiService
import com.susu.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
) : AuthRepository {
    override suspend fun login(
        accessToken: String,
    ) = kotlin.runCatching {
        apiService.login(
            provider = SnsProviders.Kakao.name,
            accessTokenRequest = AccessTokenRequest(accessToken),
        ).toDomain()
    }

    override suspend fun signUp(
        snsAccessToken: String,
        user: User,
    ) = runCatching {
        apiService.signUp(
            provider = SnsProviders.Kakao.name,
            accessToken = snsAccessToken,
            user = user.toData(),
        ).toDomain()
    }

    override suspend fun canRegister(
        accessToken: String,
    ): Boolean {
        return apiService.checkValidRegister(
            provider = SnsProviders.Kakao.name,
            accessTokenRequest = AccessTokenRequest(accessToken),
        ).canRegister
    }

    override suspend fun logout() {
        apiService.logout()
    }
}
