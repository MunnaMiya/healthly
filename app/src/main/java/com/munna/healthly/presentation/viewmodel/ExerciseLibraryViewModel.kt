package com.munna.healthly.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.munna.healthly.domain.model.Exercise
import com.munna.healthly.domain.usecase.GetExercisesUseCase
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import javax.inject.Inject

class ExerciseLibraryViewModel @Inject constructor(
    private val getExercises: GetExercisesUseCase
) : ViewModel() {

    val exercises = getExercises().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
}
