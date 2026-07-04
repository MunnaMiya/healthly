package com.munna.healthly.domain.repository

import com.munna.healthly.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserProfileRepository {
    val userProfile: Flow<UserProfile?>
    suspend fun saveProfile(profile: UserProfile)
    suspend fun clearProfile()
}
