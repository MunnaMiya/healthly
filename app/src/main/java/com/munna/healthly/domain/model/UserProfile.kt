package com.munna.healthly.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val age: Int = 25,
    val gender: Gender = Gender.MALE,
    val heightCm: Int = 175,
    val weightKg: Float = 70f,
    val activityLevel: ActivityLevel = ActivityLevel.SEDENTARY,
    val goal: GoalType = GoalType.MAINTAIN,
    val targetKgPerMonth: Float = 0.5f,
    val fitnessLevel: FitnessLevel = FitnessLevel.BEGINNER,
    val daysPerWeek: Int = 4
)

enum class Gender { MALE, FEMALE }
enum class ActivityLevel { SEDENTARY, LIGHT, MODERATE, VERY_ACTIVE, EXTRA_ACTIVE }
enum class GoalType { LOSE_WEIGHT, GAIN_WEIGHT, MAINTAIN }
enum class FitnessLevel { BEGINNER, INTERMEDIATE, ADVANCED }

@Serializable
data class NutritionPlan(
    val bmr: Int,
    val tdee: Int,
    val targetCalories: Int,
    val proteinGrams: Int,
    val carbsGrams: Int,
    val fatGrams: Int,
    val fiberGrams: Int,
    val waterMl: Int,
    val sodiumMg: Int,
    val potassiumMg: Int,
    val magnesiumMg: Int,
    val ironMg: Int,
    val bmi: Float,
    val goal: GoalType
)

@Serializable
data class ScheduledExercise(
    val exerciseId: String,
    val sets: Int,
    val reps: String,
    val isWarmup: Boolean = false
)

@Serializable
data class DailySchedule(
    val day: Int,
    val focus: String,
    val exercises: List<ScheduledExercise>
)

@Serializable
data class WeeklySchedule(
    val days: List<DailySchedule>
)
