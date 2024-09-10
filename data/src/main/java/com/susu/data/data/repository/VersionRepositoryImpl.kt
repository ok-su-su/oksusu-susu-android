package com.susu.data.data.repository

import com.susu.data.remote.api.VersionService
import com.susu.domain.repository.VersionRepository
import javax.inject.Inject

class VersionRepositoryImpl @Inject constructor(
    private val versionService: VersionService
): VersionRepository {
    override suspend fun checkIfForceUpdateNeeded(versionName: String): Boolean {
        versionService.checkForceUpdate(
            os = "AOS",
            version = versionName
        )
        return false
    }
}
