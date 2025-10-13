package com.example.healthmate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthmate.databinding.FragmentWorkoutsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.*

class WorkoutsFragment : Fragment() {
    private var _binding: FragmentWorkoutsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SharedViewModel
    private lateinit var workoutsAdapter: WorkoutsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutsBinding.inflate(inflater, container, false)
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
        workoutsAdapter = WorkoutsAdapter { workout ->
            showDeleteWorkoutDialog(workout)
        }
        binding.workoutsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = workoutsAdapter
        }
    }

    private fun setupClickListeners() {
        binding.fabAddWorkout.setOnClickListener {
            showAddWorkoutDialog()
        }
    }

    private fun observeData() {
        viewModel.workouts.observe(viewLifecycleOwner) { workouts ->
            workoutsAdapter.submitList(workouts)
            binding.emptyState.visibility = if (workouts.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun showAddWorkoutDialog() {
        val workoutTypes = arrayOf("Running", "Cycling", "Weight Training", "Yoga", "Swimming", "Walking")
        var selectedType = workoutTypes[0]

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Add Workout")
            .setSingleChoiceItems(workoutTypes, 0) { dialog, which ->
                selectedType = workoutTypes[which]
            }
            .setPositiveButton("Add") { dialog, which ->
                val workout = Workout(
                    workoutId = UUID.randomUUID().toString(),
                    userId = "current_user", // In real app, get from logged in user
                    workoutType = selectedType,
                    duration = 30, // Default duration
                    caloriesBurned = when (selectedType) {
                        "Running" -> 300
                        "Cycling" -> 250
                        "Weight Training" -> 200
                        "Yoga" -> 150
                        "Swimming" -> 350
                        "Walking" -> 180
                        else -> 200
                    },
                    distance = if (selectedType == "Running" || selectedType == "Cycling") 5.0f else null,
                    date = Date()
                )
                viewModel.addWorkout(workout)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDeleteWorkoutDialog(workout: Workout) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete Workout")
            .setMessage("Are you sure you want to delete this workout?")
            .setPositiveButton("Delete") { dialog, which ->
                // Remove workout from list
                val currentWorkouts = viewModel.workouts.value?.toMutableList() ?: mutableListOf()
                currentWorkouts.remove(workout)
                // Update the flow (in real app, delete from database)
                // For now, we'll just update the local list
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}