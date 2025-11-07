package com.example.healthmate

import android.content.Context
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

    private lateinit var language: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMealsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get language from SharedPreferences
        val prefs = requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)
        language = prefs.getString("language", "en") ?: "en"

        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        setupRecyclerView()
        setupClickListeners()
        observeData()
        applyTranslations()
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

    private fun applyTranslations() {
        // Empty state translations
        binding.tvEmptyTitle.text =
            if (language == "zulu") "Azikho Izidlo" else "No Meals Yet"
        binding.tvEmptySubtitle.text =
            if (language == "zulu") "Thepha inkinobho + ukuze ungeze isidlo sakho sokuqala"
            else "Tap the + button to add your first meal"
    }

    private fun showAddMealDialog() {
        val mealTypes = arrayOf(
            if (language == "zulu") "Isidlo sakuseni" else "Breakfast",
            if (language == "zulu") "Isidlo sasemini" else "Lunch",
            if (language == "zulu") "Isidlo sakusihlwa" else "Dinner",
            if (language == "zulu") "Isidlo esincane" else "Snack"
        )
        var selectedType = mealTypes[0]

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(if (language == "zulu") "Engeza Isidlo" else "Add Meal")
            .setSingleChoiceItems(mealTypes, 0) { _, which ->
                selectedType = mealTypes[which]
            }
            .setPositiveButton(if (language == "zulu") "Engeza" else "Add") { _, _ ->
                val meal = Meal(
                    mealId = UUID.randomUUID().toString(),
                    userId = "current_user",
                    mealType = selectedType,
                    foodItems = "Sample food items",
                    calories = when (selectedType) {
                        mealTypes[0] -> 400
                        mealTypes[1] -> 600
                        mealTypes[2] -> 700
                        mealTypes[3] -> 200
                        else -> 500
                    },
                    carbs = when (selectedType) {
                        mealTypes[0] -> 50f
                        mealTypes[1] -> 70f
                        mealTypes[2] -> 60f
                        mealTypes[3] -> 25f
                        else -> 50f
                    },
                    protein = when (selectedType) {
                        mealTypes[0] -> 20f
                        mealTypes[1] -> 30f
                        mealTypes[2] -> 35f
                        mealTypes[3] -> 10f
                        else -> 25f
                    },
                    fat = when (selectedType) {
                        mealTypes[0] -> 15f
                        mealTypes[1] -> 20f
                        mealTypes[2] -> 25f
                        mealTypes[3] -> 8f
                        else -> 15f
                    },
                    date = Date()
                )
                viewModel.addMeal(meal)
            }
            .setNegativeButton(if (language == "zulu") "Khansela" else "Cancel", null)
            .show()
    }

    private fun showDeleteMealDialog(meal: Meal) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(if (language == "zulu") "Susa Isidlo" else "Delete Meal")
            .setMessage(if (language == "zulu") "Uqinisekile ukuthi ufuna ukususa lesi sidlo?" else "Are you sure you want to delete this meal?")
            .setPositiveButton(if (language == "zulu") "Susa" else "Delete") { _, _ ->
                val currentMeals = viewModel.meals.value?.toMutableList() ?: mutableListOf()
                currentMeals.remove(meal)
                // Update the flow (real app should remove from DB)
            }
            .setNegativeButton(if (language == "zulu") "Khansela" else "Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
