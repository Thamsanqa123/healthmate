package com.example.healthmate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthmate.databinding.FragmentMealsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*

class MealsFragment : Fragment() {
    private var _binding: FragmentMealsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SharedViewModel
    private lateinit var mealsAdapter: MealsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMealsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        setupRecyclerView()
        setupClickListeners()
        observeData()
    }

    private fun setupRecyclerView() {
        mealsAdapter = MealsAdapter { meal ->
            showDeleteMealDialog(meal)
        }
        binding.mealsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mealsAdapter
        }
    }

    private fun setupClickListeners() {
        binding.fabAddMeal.setOnClickListener {
            showAddMealDialog()
        }
    }

    private fun observeData() {
        viewModel.meals.observe(viewLifecycleOwner) { meals ->
            mealsAdapter.submitList(meals)
            binding.emptyState.visibility = if (meals.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun showAddMealDialog() {
        val mealTypes = arrayOf("Breakfast", "Lunch", "Dinner", "Snack")
        var selectedType = mealTypes[0]

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Add Meal")
            .setSingleChoiceItems(mealTypes, 0) { dialog, which ->
                selectedType = mealTypes[which]
            }
            .setPositiveButton("Add") { dialog, which ->
                val meal = Meal(
                    mealId = UUID.randomUUID().toString(),
                    userId = "current_user", // In real app, get from logged in user
                    mealType = selectedType,
                    foodItems = "Sample food items", // In real app, get from user input
                    calories = when (selectedType) {
                        "Breakfast" -> 400
                        "Lunch" -> 600
                        "Dinner" -> 700
                        "Snack" -> 200
                        else -> 500
                    },
                    carbs = when (selectedType) {
                        "Breakfast" -> 50f
                        "Lunch" -> 70f
                        "Dinner" -> 60f
                        "Snack" -> 25f
                        else -> 50f
                    },
                    protein = when (selectedType) {
                        "Breakfast" -> 20f
                        "Lunch" -> 30f
                        "Dinner" -> 35f
                        "Snack" -> 10f
                        else -> 25f
                    },
                    fat = when (selectedType) {
                        "Breakfast" -> 15f
                        "Lunch" -> 20f
                        "Dinner" -> 25f
                        "Snack" -> 8f
                        else -> 15f
                    },
                    date = Date()
                )
                viewModel.addMeal(meal)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDeleteMealDialog(meal: Meal) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete Meal")
            .setMessage("Are you sure you want to delete this meal?")
            .setPositiveButton("Delete") { dialog, which ->
                // Remove meal from list
                val currentMeals = viewModel.meals.value?.toMutableList() ?: mutableListOf()
                currentMeals.remove(meal)
                // Update the flow (in real app, delete from database)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}