package com.example.healthmate

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthmate.databinding.FragmentWorkoutsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*

class WorkoutsFragment : Fragment() {
    private var _binding: FragmentWorkoutsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SharedViewModel
    private lateinit var workoutsAdapter: WorkoutsAdapter
    private lateinit var language: String


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

        // Get language from SharedPreferences
        val prefs = requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)
        language = prefs.getString("language", "en") ?: "en"

        setupRecyclerView()
        setupClickListeners()
        observeData()
        applyTranslations()
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

    private fun applyTranslations() {
        // Empty state translations
        binding.tvEmptyTitle.text =
            if (language == "zulu") "Azikho Iziqeqesho" else "No Workouts Yet"
        binding.tvEmptySubtitle.text =
            if (language == "zulu") "Thepha inkinobho + ukuze ungeze ukuziqeqesha kwakho kokuqala"
            else "Tap the + button to add your first workout"
    }

    private fun showAddWorkoutDialog() {
        val workoutTypes = arrayOf(
            if (language == "zulu") "Ukugijima" else "Running",
            if (language == "zulu") "Ukuhamba ngebhayisikili" else "Cycling",
            if (language == "zulu") "Ukuzivocavoca ngeweyithi" else "Weight Training",
            if (language == "zulu") "Yoga" else "Yoga",
            if (language == "zulu") "Ukubhukuda" else "Swimming",
            if (language == "zulu") "Ukuhamba" else "Walking"
        )
        var selectedType = workoutTypes[0]

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(if (language == "zulu") "Engeza Ukuziqeqesha" else "Add Workout")
            .setSingleChoiceItems(workoutTypes, 0) { _, which ->
                selectedType = workoutTypes[which]
            }
            .setPositiveButton(if (language == "zulu") "Engeza" else "Add") { _, _ ->
                val workout = Workout(
                    workoutId = UUID.randomUUID().toString(),
                    userId = "current_user",
                    workoutType = selectedType,
                    duration = 30,
                    caloriesBurned = when (selectedType) {
                        workoutTypes[0] -> 300
                        workoutTypes[1] -> 250
                        workoutTypes[2] -> 200
                        workoutTypes[3] -> 150
                        workoutTypes[4] -> 350
                        workoutTypes[5] -> 180
                        else -> 200
                    },
                    distance = if (selectedType == workoutTypes[0] || selectedType == workoutTypes[1]) 5.0f else null,
                    date = Date()
                )
                viewModel.addWorkout(workout)
            }
            .setNegativeButton(if (language == "zulu") "Khansela" else "Cancel", null)
            .show()
    }

    private fun showDeleteWorkoutDialog(workout: Workout) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(if (language == "zulu") "Susa Ukuziqeqesha" else "Delete Workout")
            .setMessage(if (language == "zulu") "Uqinisekile ukuthi ufuna ukususa lokhu kuziqeqesha?" else "Are you sure you want to delete this workout?")
            .setPositiveButton(if (language == "zulu") "Susa" else "Delete") { _, _ ->
                val currentWorkouts = viewModel.workouts.value?.toMutableList() ?: mutableListOf()
                currentWorkouts.remove(workout)
                // Update the list (real app should remove from DB)
            }
            .setNegativeButton(if (language == "zulu") "Khansela" else "Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
