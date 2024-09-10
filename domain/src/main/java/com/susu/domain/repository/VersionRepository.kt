package com.susu.domain.repository

interface VersionRepository {
    suspend fun checkIfForceUpdateNeeded(versionName: String): Boolean
}
