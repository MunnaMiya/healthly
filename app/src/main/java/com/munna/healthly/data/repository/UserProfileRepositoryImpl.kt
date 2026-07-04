package com.munna.healthly.data.repository

import android.content.Context
import com.munna.healthly.data.db.UserProfileDao
import com.munna.healthly.data.db.UserProfileEntity
import com.munna.healthly.domain.model.ActivityLevel
import com.munna.healthly.domain.model.FitnessLevel
import com.munna.healthly.domain.model.Gender
import com.munna.healthly.domain.model.GoalType
import com.munna.healthly.domain.model.UserProfile
import com.munna.healthly.domain.repository.UserProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class UserProfileRepositoryImpl @javax.inject.Inject constructor(
    private val dao: UserProfileDao,
    private val context: Context
) : UserProfileRepository {

    override val userProfile: Flow<UserProfile?> = dao.getProfile().map { entity ->
        entity?.let { toDomain(it) }
    }

    override suspend fun saveProfile(profile: UserProfile) {
        withContext(Dispatchers.IO) {
            dao.insert(toEntity(profile))
        }
    }

    override suspend fun clearProfile() {
        withContext(Dispatchers.IO) { dao.clear() }
    }

    private fun toDomain(e: UserProfileEntity) = UserProfile(
        age = e.age, gender = Gender.valueOf(e.gender),
        heightCm = e.heightCm, weightKg = e.weightKg,
        activityLevel = ActivityLevel.valueOf(e.activityLevel),
        goal = GoalType.valueOf(e.goal),
        targetKgPerMonth = e.targetKgPerMonth,
        fitnessLevel = FitnessLevel.valueOf(e.fitnessLevel),
        daysPerWeek = e.daysPerWeek
    )

    private fun toEntity(p: UserProfile) = UserProfileEntity(
        age = p.age, gender = p.gender.name, heightCm = p.heightCm,
        weightKg = p.weightKg, activityLevel = p.activityLevel.name,
        goal = p.goal.name, targetKgPerMonth = p.targetKgPerMonth,
        fitnessLevel = p.fitnessLevel.name, daysPerWeek = p.daysPerWeek
    )
}
