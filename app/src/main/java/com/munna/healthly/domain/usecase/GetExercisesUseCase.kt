package com.munna.healthly.domain.usecase

import com.munna.healthly.domain.model.Exercise
import com.munna.healthly.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow

class GetExercisesUseCase @javax.inject.Inject constructor(
    private val repo: ExerciseRepository
) {
    operator fun invoke(): Flow<List<Exercise>> = repo.getAllExercises()
    suspend fun forGoal(goal: String): List<Exercise> = repo.getExercisesForGoal(goal)
    suspend fun byId(id: String): Exercise? = repo.getExerciseById(id)
}
