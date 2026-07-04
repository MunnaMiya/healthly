package com.munna.healthly.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.munna.healthly.domain.model.Exercise
import com.munna.healthly.domain.usecase.GetExercisesUseCase
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import javax.inject.Inject

class ExerciseDetailViewModel @Inject constructor(
    private val getExercises: GetExercisesUseCase
) : ViewModel() {

    fun getExercise(id: String) = getExercises().map { list -> list.find { it.id == id } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)
}
