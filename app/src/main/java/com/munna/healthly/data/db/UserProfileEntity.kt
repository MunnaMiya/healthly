package com.munna.healthly.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "user_profile")
@Serializable
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val age: Int,
    val gender: String,
    val heightCm: Int,
    val weightKg: Float,
    val activityLevel: String,
    val goal: String,
    val targetKgPerMonth: Float,
    val fitnessLevel: String,
    val daysPerWeek: Int
)
