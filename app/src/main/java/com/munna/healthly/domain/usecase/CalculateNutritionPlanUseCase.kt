package com.munna.healthly.domain.usecase

import com.munna.healthly.domain.model.NutritionPlan
import com.munna.healthly.domain.model.UserProfile
import kotlin.math.roundToInt

class CalculateNutritionPlanUseCase {
    operator fun invoke(profile: UserProfile): NutritionPlan {
        val bmr = if (profile.gender == com.munna.healthly.domain.model.Gender.MALE) {
            (10 * profile.weightKg) + (6.25 * profile.heightCm) - (5 * profile.age) + 5
        } else {
            (10 * profile.weightKg) + (6.25 * profile.heightCm) - (5 * profile.age) - 161
        }

        val activityMultiplier = when (profile.activityLevel) {
            com.munna.healthly.domain.model.ActivityLevel.SEDENTARY -> 1.2f
            com.munna.healthly.domain.model.ActivityLevel.LIGHT -> 1.375f
            com.munna.healthly.domain.model.ActivityLevel.MODERATE -> 1.55f
            com.munna.healthly.domain.model.ActivityLevel.VERY_ACTIVE -> 1.725f
            com.munna.healthly.domain.model.ActivityLevel.EXTRA_ACTIVE -> 1.9f
        }
        val tdee = bmr * activityMultiplier

        val targetKgPerMonth = profile.targetKgPerMonth.coerceIn(-4f, 4f)
        val dailyCalChange = (targetKgPerMonth * 7700f) / 30f
        val targetCalories = (tdee + dailyCalChange).roundToInt().coerceAtLeast(1200)

        val (proteinPct, fatPct, carbPct) = when (profile.goal) {
            com.munna.healthly.domain.model.GoalType.LOSE_WEIGHT -> Triple(0.35f, 0.25f, 0.40f)
            com.munna.healthly.domain.model.GoalType.GAIN_WEIGHT -> Triple(0.25f, 0.25f, 0.50f)
            com.munna.healthly.domain.model.GoalType.MAINTAIN -> Triple(0.30f, 0.30f, 0.40f)
        }

        val proteinG = (targetCalories * proteinPct / 4).roundToInt()
        val fatG = (targetCalories * fatPct / 9).roundToInt()
        val carbG = (targetCalories * carbPct / 4).roundToInt()
        val fiberG = (targetCalories / 1000 * 14).roundToInt()
        val waterMl = (profile.weightKg * 35).roundToInt()

        return NutritionPlan(
            bmr = bmr.roundToInt(), tdee = tdee.roundToInt(), targetCalories = targetCalories,
            proteinGrams = proteinG, carbsGrams = carbG, fatGrams = fatG, fiberGrams = fiberG,
            waterMl = waterMl, sodiumMg = 2300, potassiumMg = 4700,
            magnesiumMg = if (profile.gender == com.munna.healthly.domain.model.Gender.MALE) 420 else 320,
            ironMg = if (profile.gender == com.munna.healthly.domain.model.Gender.FEMALE && profile.age < 50) 18 else 8,
            bmi = profile.weightKg / ((profile.heightCm / 100f) * (profile.heightCm / 100f)),
            goal = profile.goal
        )
    }
}
