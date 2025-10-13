package com.example.healthmate

import androidx.room.*

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Insert
    suspend fun insertWorkout(workout: Workout)

    @Query("SELECT * FROM workouts WHERE userId = :userId ORDER BY date DESC")
    fun getWorkoutsByUser(userId: String): Flow<List<Workout>>

    @Query("SELECT * FROM workouts WHERE workoutId = :workoutId")
    suspend fun getWorkoutById(workoutId: String): Workout?

    @Update
    suspend fun updateWorkout(workout: Workout)

    @Delete
    suspend fun deleteWorkout(workout: Workout)

    @Query("SELECT SUM(caloriesBurned) FROM workouts WHERE userId = :userId AND date >= :startDate AND date <= :endDate")
    suspend fun getTotalCaloriesBurned(userId: String, startDate: Long, endDate: Long): Int?
}