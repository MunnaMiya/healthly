package com.munna.healthly.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Exercise(
    val id: String,
    val name: String,
    val muscleGroup: String,
    val difficulty: String,
    val equipment: String,
    val description: String,
    val gifUrl: String, // Relative to assets/gifs/
    val targetGoal: String // "LOSE", "GAIN", "BOTH"
)
