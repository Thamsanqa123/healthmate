package com.example.healthmate

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.healthmate.databinding.FragmentDashboardBinding
import java.text.SimpleDateFormat
import java.util.*

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SharedViewModel
    private var language = "english"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prefs = requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)
        language = prefs.getString("language", "english") ?: "english"

        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        setupUI()
        observeData()
    }

    private fun setupUI() {
        val locale = if (language == "zulu") Locale("zu") else Locale.getDefault()
        val sdf = SimpleDateFormat("EEEE, MMMM d", locale)
        binding.tvDate.text = sdf.format(Date())

        binding.cardWorkout.setOnClickListener { findNavController().navigate(R.id.workoutsFragment) }
        binding.cardMeals.setOnClickListener { findNavController().navigate(R.id.mealsFragment) }
        binding.tvWorkoutsCount.setOnClickListener { findNavController().navigate(R.id.workoutsFragment) }
        binding.tvMealsCount.setOnClickListener { findNavController().navigate(R.id.mealsFragment) }

        binding.tvWelcome.text = if (language == "zulu") "Siyakwamukela, " else "Welcome, "
        binding.tvWelcome.append(viewModel.currentUser.value?.fullName ?: "")

        binding.tvWorkoutsLabel.text = if (language == "zulu") "Ukuziqeqesha" else "Workouts"
        binding.tvMealsLabel.text = if (language == "zulu") "Izidlo" else "Meals"
        binding.tvQuickActions.text = if (language == "zulu") "Izinyathelo Ezisheshayo" else "Quick Actions"
        binding.tvCaloriesSummary.text = if (language == "zulu") "Ukudla Kwamakhalori" else "Calories Summary"
        binding.tvConsumed.text = if (language == "zulu") "Kudliwe" else "Consumed"
        binding.tvBurned.text = if (language == "zulu") "Kushisiwe" else "Burned"
        binding.tvLogWorkout.text = if (language == "zulu") "Bhalisela Ukuzivocavoca" else "Log Workout"
        binding.tvTrackExercise.text = if (language == "zulu") "Landela Ukuzivocavoca Kwakho" else "Track Your Exercise"
        binding.tvLogMeal.text = if (language == "zulu") "Bhalisela Isidlo" else "Log Meal"
        binding.tvTrackNutrition.text = if (language == "zulu") "Landela Ukudla Kwakho" else "Track Your Nutrition"
    }

    private fun observeData() {
        viewModel.currentUser.observe(viewLifecycleOwner) { user ->
            user?.let {
                binding.tvWelcome.text = if (language == "zulu") "Siyakwamukela, ${it.fullName}" else "Welcome, ${it.fullName}"
            }
        }

        viewModel.workouts.observe(viewLifecycleOwner) { workouts ->
            binding.tvWorkoutsCount.text = workouts.size.toString()
            val totalCalories = workouts.sumOf { it.caloriesBurned }
            binding.tvCaloriesBurned.text = totalCalories.toString()
        }

        viewModel.meals.observe(viewLifecycleOwner) { meals ->
            binding.tvMealsCount.text = meals.size.toString()
            val totalCalories = meals.sumOf { it.calories }
            binding.tvCaloriesConsumed.text = totalCalories.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
