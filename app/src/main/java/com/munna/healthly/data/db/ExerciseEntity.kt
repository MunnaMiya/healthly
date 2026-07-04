package com.munna.healthly.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "exercises")
@Serializable
data class ExerciseEntity(
    @PrimaryKey val id: String,
    val name: String,
    val muscleGroup: String,
    val difficulty: String,
    val equipment: String,
    val description: String,
    @androidx.room.ColumnInfo(name = "gif_url") val gifUrl: String,
    val targetGoal: String
)
