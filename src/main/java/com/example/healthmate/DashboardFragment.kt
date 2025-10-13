package com.example.healthmate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.healthmate.R
import com.example.healthmate.SharedViewModel
import com.example.healthmate.databinding.FragmentDashboardBinding
import java.text.SimpleDateFormat
import java.util.*

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        setupUI()
        observeData()
    }

    private fun setupUI() {
        binding.tvDate.text = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault()).format(Date())

        // Make workout card navigate to workouts fragment
        binding.cardWorkout.setOnClickListener {
            findNavController().navigate(R.id.workoutsFragment)
        }

        // Make meals card navigate to meals fragment
        binding.cardMeals.setOnClickListener {
            findNavController().navigate(R.id.mealsFragment)
        }

        // Make stats cards clickable too
        binding.tvWorkoutsCount.setOnClickListener {
            findNavController().navigate(R.id.workoutsFragment)
        }

        binding.tvMealsCount.setOnClickListener {
            findNavController().navigate(R.id.mealsFragment)
        }
    }

    private fun observeData() {
        // Observe current user
        viewModel.currentUser.observe(viewLifecycleOwner) { user ->
            user?.let { currentUser ->
                binding.tvWelcome.text = "Welcome, ${currentUser.fullName}"
            }
        }

        // Observe workouts
        viewModel.workouts.observe(viewLifecycleOwner) { workouts ->
            binding.tvWorkoutsCount.text = workouts.size.toString()
            val totalCalories = workouts.sumOf { it.caloriesBurned }
            binding.tvCaloriesBurned.text = totalCalories.toString()

            // Calculate weekly progress
            val weeklyWorkouts = workouts.filter {
                Date().time - it.date.time <= 7 * 24 * 60 * 60 * 1000
            }
            // Remove tvWeeklyWorkouts reference if it doesn't exist
            // binding.tvWeeklyWorkouts.text = "${weeklyWorkouts.size}/7 workouts this week"
        }

        // Observe meals
        viewModel.meals.observe(viewLifecycleOwner) { meals ->
            binding.tvMealsCount.text = meals.size.toString()
            val totalCalories = meals.sumOf { it.calories }
            binding.tvCaloriesConsumed.text = totalCalories.toString()

            // Calculate today's meals
            val todayMeals = meals.filter {
                SimpleDateFormat("yyyyMMdd").format(it.date) ==
                        SimpleDateFormat("yyyyMMdd").format(Date())
            }
            // Remove tvTodayMeals reference if it doesn't exist
            // binding.tvTodayMeals.text = "${todayMeals.size} meals today"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}