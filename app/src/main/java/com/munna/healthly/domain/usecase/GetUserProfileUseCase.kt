package com.munna.healthly.domain.usecase

import com.munna.healthly.domain.model.UserProfile
import com.munna.healthly.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.Flow

class GetUserProfileUseCase @javax.inject.Inject constructor(
    private val repo: UserProfileRepository
) {
    operator fun invoke(): Flow<UserProfile?> = repo.userProfile
}
