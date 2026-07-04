package com.munna.healthly.domain.repository

import com.munna.healthly.domain.model.Exercise
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {
    suspend fun initializeDatabaseIfNeeded()
    fun getAllExercises(): Flow<List<Exercise>>
    suspend fun getExercisesForGoal(goal: String): List<Exercise>
    suspend fun getExerciseById(id: String): Exercise?
}
