package com.example.healthmate

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.Date
import java.util.UUID

@Entity(tableName = "meals")
data class Meal(
    @PrimaryKey
    val mealId: String = UUID.randomUUID().toString(),
    val userId: String,
    val date: Date = Date(),
    val mealType: String,
    val foodItems: String, // JSON string of food items
    val calories: Int,
    val carbs: Float,
    val protein: Float,
    val fat: Float,
    val notes: String? = null
)