package com.munna.healthly.domain.usecase

import com.munna.healthly.domain.model.UserProfile
import com.munna.healthly.domain.repository.UserProfileRepository

class SaveUserProfileUseCase @javax.inject.Inject constructor(
    private val repo: UserProfileRepository
) {
    operator fun invoke(profile: UserProfile) = repo.saveProfile(profile)
}
