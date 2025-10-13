package com.example.healthmate

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val userId: String,
    val fullName: String,
    val email: String,
    val passwordHash: String? = null,
    val dateOfBirth: Date? = null,
    val gender: String? = null,
    val height: Float? = null,
    val currentWeight: Float? = null,
    val targetWeight: Float? = null,
    val activityLevel: String? = null,
    val dietaryPreference: String? = null,
    val languagePreference: String = "en",
    val createdAt: Date = Date()
)