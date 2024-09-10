package com.susu.domain.usecase.version

import com.susu.core.common.runCatchingIgnoreCancelled
import com.susu.domain.repository.VersionRepository
import javax.inject.Inject

class CheckForceUpdateUseCase @Inject constructor(
    private val versionRepository: VersionRepository,
) {
    suspend operator fun invoke(versionName: String) = runCatchingIgnoreCancelled {
        versionRepository.checkIfForceUpdateNeeded(versionName)
    }
}
