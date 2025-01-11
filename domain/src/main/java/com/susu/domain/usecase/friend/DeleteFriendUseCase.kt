package com.susu.domain.usecase.friend

import com.susu.core.common.runCatchingIgnoreCancelled
import com.susu.domain.repository.FriendRepository
import javax.inject.Inject

class DeleteFriendUseCase @Inject constructor(
    private val friendRepository: FriendRepository
) {
    suspend operator fun invoke(friendId: Long) = runCatchingIgnoreCancelled {
        friendRepository.deleteFriends(ids = listOf(friendId))
    }
}
