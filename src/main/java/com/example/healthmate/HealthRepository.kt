package com.example.healthmate

import com.example.healthmate.AppDatabase
import com.example.healthmate.Meal
import com.example.healthmate.User
import com.example.healthmate.Workout
import kotlinx.coroutines.flow.Flow

class HealthRepository(private val db: AppDatabase) {
    private val userDao = db.userDao()
    private val workoutDao = db.workoutDao()
    private val mealDao = db.mealDao()

    // User operations
    suspend fun insertUser(user: User) = userDao.insertUser(user)
    suspend fun getUserByEmail(email: String): User? = userDao.getUserByEmail(email)
    suspend fun getUserById(userId: String): User? = userDao.getUserById(userId)
    suspend fun updateUser(user: User) = userDao.updateUser(user)

    // Workout operations
    suspend fun insertWorkout(workout: Workout) = workoutDao.insertWorkout(workout)
    fun getWorkoutsByUser(userId: String): Flow<List<Workout>> = workoutDao.getWorkoutsByUser(userId)
    suspend fun deleteWorkout(workout: Workout) = workoutDao.deleteWorkout(workout)

    // Meal operations
    suspend fun insertMeal(meal: Meal) = mealDao.insertMeal(meal)
    fun getMealsByUser(userId: String): Flow<List<Meal>> = mealDao.getMealsByUser(userId)
    suspend fun deleteMeal(meal: Meal) = mealDao.deleteMeal(meal)

    // Analytics
    suspend fun getTotalCaloriesBurned(userId: String, startDate: Long, endDate: Long): Int =
        workoutDao.getTotalCaloriesBurned(userId, startDate, endDate) ?: 0

    suspend fun getTotalCaloriesConsumed(userId: String, startDate: Long, endDate: Long): Int =
        mealDao.getTotalCaloriesConsumed(userId, startDate, endDate) ?: 0
}