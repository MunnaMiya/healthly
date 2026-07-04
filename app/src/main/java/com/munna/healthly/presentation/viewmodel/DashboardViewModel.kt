package com.munna.healthly.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.munna.healthly.domain.model.DailySchedule
import com.munna.healthly.domain.model.NutritionPlan
import com.munna.healthly.domain.model.UserProfile
import com.munna.healthly.domain.usecase.CalculateNutritionPlanUseCase
import com.munna.healthly.domain.usecase.GenerateWeeklyScheduleUseCase
import com.munna.healthly.domain.usecase.GetUserProfileUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    private val getProfile: GetUserProfileUseCase,
    private val calcPlan: CalculateNutritionPlanUseCase,
    private val genSchedule: GenerateWeeklyScheduleUseCase
) : ViewModel() {

    val uiState = combine(
        getProfile().distinctUntilChanged(),
        getProfile().distinctUntilChanged().map { it?.let { calcPlan(it) } },
        getProfile().distinctUntilChanged().map { it?.let { genSchedule(it.goal.name, it.daysPerWeek, it.fitnessLevel.name) } }
    ) { profile, plan, schedule ->
        DashboardState(profile, plan, schedule?.days?.firstOrNull())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), DashboardState(null, null, null))

    data class DashboardState(
        val profile: UserProfile?,
        val nutritionPlan: NutritionPlan?,
        val todaySchedule: DailySchedule?
    )
}
