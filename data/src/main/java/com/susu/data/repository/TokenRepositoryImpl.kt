package com.susu.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.susu.core.model.Token
import com.susu.data.extension.secureEdit
import com.susu.data.extension.secureMap
import com.susu.domain.repository.TokenRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : TokenRepository {

    override fun getAccessToken(): Flow<String?> {
        return dataStore.data.secureMap { preference ->
            preference[ACCESS_TOKEN]
        }
    }

    override fun getRefreshToken(): Flow<String?> {
        return dataStore.data.secureMap { preference ->
            preference[REFRESH_TOKEN]
        }
    }

    override suspend fun saveTokens(token: Token) {
        dataStore.secureEdit(token.accessToken) { preference, encrypted ->
            println(encrypted)
            preference[ACCESS_TOKEN] = encrypted
        }
        dataStore.secureEdit(token.refreshToken) { preference, encrypted ->
            preference[REFRESH_TOKEN] = encrypted
        }
    }

    override suspend fun deleteTokens() {
        dataStore.edit { preference ->
            preference.remove(ACCESS_TOKEN)
            preference.remove(REFRESH_TOKEN)
        }
    }

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("accessToken")
        private val REFRESH_TOKEN = stringPreferencesKey("refreshToken")
    }
}
