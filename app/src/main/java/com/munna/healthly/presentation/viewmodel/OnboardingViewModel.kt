package com.munna.healthly.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.munna.healthly.domain.model.ActivityLevel
import com.munna.healthly.domain.model.FitnessLevel
import com.munna.healthly.domain.model.Gender
import com.munna.healthly.domain.model.GoalType
import com.munna.healthly.domain.model.UserProfile
import com.munna.healthly.domain.usecase.SaveUserProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class OnboardingViewModel @Inject constructor(
    private val saveProfile: SaveUserProfileUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(OnboardingState())
    val state = _state.asStateFlow()

    data class OnboardingState(
        val age: String = "25", val height: String = "175", val weight: String = "70",
        val gender: Gender = Gender.MALE,
        val activity: ActivityLevel = ActivityLevel.SEDENTARY,
        val goal: GoalType = GoalType.MAINTAIN,
        val targetKg: String = "0.5", val level: FitnessLevel = FitnessLevel.BEGINNER,
        val days: Int = 4, val error: String? = null
    )

    fun updateAge(s: String) = _state.update { it.copy(age = s, error = null) }
    fun updateHeight(s: String) = _state.update { it.copy(height = s) }
    fun updateWeight(s: String) = _state.update { it.copy(weight = s) }
    fun updateGender(g: Gender) = _state.update { it.copy(gender = g) }
    fun updateActivity(a: ActivityLevel) = _state.update { it.copy(activity = a) }
    fun updateGoal(g: GoalType) = _state.update { it.copy(goal = g) }
    fun updateTargetKg(s: String) = _state.update { it.copy(targetKg = s) }
    fun updateLevel(l: FitnessLevel) = _state.update { it.copy(level = l) }
    fun updateDays(d: Int) = _state.update { it.copy(days = d) }

    fun save(onSuccess: () -> Unit) {
        val s = _state.value
        try {
            val profile = UserProfile(
                age = s.age.toIntOrNull() ?: 25, gender = s.gender,
                heightCm = s.height.toIntOrNull() ?: 175, weightKg = s.weight.toFloatOrNull() ?: 70f,
                activityLevel = s.activity, goal = s.goal,
                targetKgPerMonth = s.targetKg.toFloatOrNull() ?: 0.5f,
                fitnessLevel = s.level, daysPerWeek = s.days
            )
            viewModelScope.launch { saveProfile(profile); onSuccess() }
        } catch (e: Exception) { _state.update { it.copy(error = "Invalid input") } }
    }
}
