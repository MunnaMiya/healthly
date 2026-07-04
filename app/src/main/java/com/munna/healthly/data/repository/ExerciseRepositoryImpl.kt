package com.munna.healthly.data.repository

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.munna.healthly.data.db.ExerciseDao
import com.munna.healthly.data.db.ExerciseEntity
import com.munna.healthly.domain.model.Exercise
import com.munna.healthly.domain.repository.ExerciseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.coroutines.withContext

class ExerciseRepositoryImpl @javax.inject.Inject constructor(
    private val dao: ExerciseDao,
    private val context: Context
) : ExerciseRepository {

    private val INIT_KEY = booleanPreferencesKey("exercises_initialized")

    override suspend fun initializeDatabaseIfNeeded() {
        val initialized = context.dataStore.data.first()[INIT_KEY] ?: false
        if (!initialized) {
            withContext(Dispatchers.IO) {
                val input = context.assets.open("exercises.json").bufferedReader().readText()
                val exercises = Json.decodeFromString<List<ExerciseEntity>>(input)
                dao.insertAll(exercises)
                context.dataStore.edit { it[INIT_KEY] = true }
            }
        }
    }

    override fun getAllExercises(): Flow<List<Exercise>> = dao.getAll().map { entities ->
        entities.map { toDomain(it) }
    }

    override suspend fun getExercisesForGoal(goal: String): List<Exercise> {
        return withContext(Dispatchers.IO) {
            dao.getForGoal(goal).map { toDomain(it) }
        }
    }

    override suspend fun getExerciseById(id: String): Exercise? {
        return withContext(Dispatchers.IO) {
            dao.getById(id)?.let { toDomain(it) }
        }
    }

    private fun toDomain(e: ExerciseEntity) = Exercise(
        e.id, e.name, e.muscleGroup, e.difficulty, e.equipment,
        e.description, e.gifUrl, e.targetGoal
    )
}
