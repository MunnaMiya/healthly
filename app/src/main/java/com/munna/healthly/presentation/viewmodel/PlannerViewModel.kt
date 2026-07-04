package com.munna.healthly.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.munna.healthly.domain.model.WeeklySchedule
import com.munna.healthly.domain.model.UserProfile
import com.munna.healthly.domain.usecase.GenerateWeeklyScheduleUseCase
import com.munna.healthly.domain.usecase.GetUserProfileUseCase
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import javax.inject.Inject

class PlannerViewModel @Inject constructor(
    private val getProfile: GetUserProfileUseCase,
    private val genSchedule: GenerateWeeklyScheduleUseCase
) : ViewModel() {

    val weeklySchedule = getProfile()
        .distinctUntilChanged()
        .map { profile ->
            profile?.let { genSchedule(it.goal.name, it.daysPerWeek, it.fitnessLevel.name) }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)
}
