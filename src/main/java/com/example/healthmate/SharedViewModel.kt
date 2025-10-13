package com.example.healthmate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthmate.Meal
import com.example.healthmate.User
import com.example.healthmate.Workout
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class SharedViewModel : ViewModel() {
    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> = _currentUser

    private val _workouts = MutableLiveData<List<Workout>>(emptyList())
    val workouts: LiveData<List<Workout>> = _workouts

    private val _meals = MutableLiveData<List<Meal>>(emptyList())
    val meals: LiveData<List<Meal>> = _meals

    // Add a list to store registered users
    private val _registeredUsers = MutableLiveData<MutableList<User>>(mutableListOf())
    val registeredUsers: LiveData<MutableList<User>> = _registeredUsers

    fun setCurrentUser(user: User?) {
        _currentUser.value = user
    }

    fun addWorkout(workout: Workout) {
        val currentWorkouts = _workouts.value?.toMutableList() ?: mutableListOf()
        currentWorkouts.add(workout)
        _workouts.value = currentWorkouts
    }

    fun addMeal(meal: Meal) {
        val currentMeals = _meals.value?.toMutableList() ?: mutableListOf()
        currentMeals.add(meal)
        _meals.value = currentMeals
    }

    // Add methods for user registration and login
    fun registerUser(user: User) {
        val currentUsers = _registeredUsers.value?.toMutableList() ?: mutableListOf()
        currentUsers.add(user)
        _registeredUsers.value = currentUsers
        _currentUser.value = user
    }

    fun loginUser(email: String, password: String): Boolean {
        val users = _registeredUsers.value ?: return false
        val user = users.find { it.email == email && it.passwordHash == password }
        if (user != null) {
            _currentUser.value = user
            return true
        }
        return false
    }

    fun clearAllData() {
        _currentUser.value = null
        _workouts.value = emptyList()
        _meals.value = emptyList()
    }
}