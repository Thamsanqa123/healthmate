package com.example.healthmate

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(tableName = "workouts")
data class Workout(
    @PrimaryKey
    val workoutId: String = UUID.randomUUID().toString(),
    val userId: String,
    val workoutType: String,
    val duration: Int,
    val caloriesBurned: Int,
    val distance: Float? = null,
    val gpsRoute: String? = null,
    val date: Date = Date(),
    val notes: String? = null
)