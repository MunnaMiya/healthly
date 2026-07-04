package com.munna.healthly.domain.usecase

import com.munna.healthly.domain.model.DailySchedule
import com.munna.healthly.domain.model.Exercise
import com.munna.healthly.domain.model.ScheduledExercise
import com.munna.healthly.domain.model.WeeklySchedule
import com.munna.healthly.domain.repository.ExerciseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GenerateWeeklyScheduleUseCase @javax.inject.Inject constructor(
    private val exerciseRepo: ExerciseRepository
) {
    operator fun invoke(goal: String, daysPerWeek: Int, level: String): WeeklySchedule {
        val allExercises = exerciseRepo.getExercisesForGoal(goal)
        val strength = allExercises.filter { it.muscleGroup in setOf("Chest", "Back", "Legs", "Shoulders", "Arms") }
        val core = allExercises.filter { it.muscleGroup == "Core" }
        val cardio = allExercises.filter { it.muscleGroup == "Cardio" }
        val rng = java.util.Random()

        val scheduleDays = mutableListOf<DailySchedule>()
        repeat(daysPerWeek) { dayIndex ->
            val exercises = mutableListOf<ScheduledExercise>()
            // Warmup
            if (cardio.isNotEmpty()) exercises.add(ScheduledExercise(cardio[rng.nextInt(cardio.size)].id, 1, "5-10 min", true))
            // Main (3 exercises)
            val focus = when(dayIndex % 3) { 0 -> "Upper"; 1 -> "Lower"; else -> "Full" }
            val pool = if (focus == "Full") strength else strength.filter { it.muscleGroup.startsWith(focus.substring(0,1)) }
            val mainPool = if (pool.isNotEmpty()) pool else strength
            repeat(3) {
                if (mainPool.isNotEmpty()) exercises.add(ScheduledExercise(mainPool[rng.nextInt(mainPool.size)].id, 3, "8-12"))
            }
            // Core
            if (core.isNotEmpty()) exercises.add(ScheduledExercise(core[rng.nextInt(core.size)].id, 3, "30-60s"))

            scheduleDays.add(DailySchedule(dayIndex + 1, focus, exercises))
        }
        return WeeklySchedule(scheduleDays)
    }
}
