package com.example.healthmate

import androidx.room.*

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {
    @Insert
    suspend fun insertMeal(meal: Meal)

    @Query("SELECT * FROM meals WHERE userId = :userId ORDER BY date DESC")
    fun getMealsByUser(userId: String): Flow<List<Meal>>

    @Query("SELECT * FROM meals WHERE mealId = :mealId")
    suspend fun getMealById(mealId: String): Meal?

    @Update
    suspend fun updateMeal(meal: Meal)

    @Delete
    suspend fun deleteMeal(meal: Meal)

    @Query("SELECT SUM(calories) FROM meals WHERE userId = :userId AND date >= :startDate AND date <= :endDate")
    suspend fun getTotalCaloriesConsumed(userId: String, startDate: Long, endDate: Long): Int?
}