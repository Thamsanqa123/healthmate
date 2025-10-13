package com.example.healthmate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.healthmate.databinding.FragmentProfileBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        setupClickListeners()
        observeData()
    }

    private fun setupClickListeners() {
        binding.btnEditProfile.setOnClickListener {
            // Show edit profile dialog
            showEditProfileDialog()
        }

        binding.btnSettings.setOnClickListener {
            // Navigate to settings (you can create a SettingsFragment)
            // For now, show a message
            showMessage("Settings feature coming soon!")
        }

        binding.btnLogout.setOnClickListener {
            // Show confirmation dialog before logout
            showLogoutConfirmation()
        }
    }

    private fun observeData() {
        viewModel.currentUser.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                binding.tvUserName.text = user.fullName
                binding.tvUserEmail.text = user.email
                binding.tvMemberSince.text = "Member since ${SimpleDateFormat("MMM yyyy").format(user.createdAt)}"
            } else {
                // If no user is logged in, navigate back to login
                navigateToLogin()
            }
        }

        viewModel.workouts.observe(viewLifecycleOwner) { workouts ->
            binding.tvTotalWorkouts.text = workouts.size.toString()

            // Calculate workouts from the last 7 days
            val weekAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000)
            val weeklyWorkouts = workouts.count { it.date.time >= weekAgo }
            binding.tvTotalWorkoutsThisWeek.text = weeklyWorkouts.toString()
        }

        viewModel.meals.observe(viewLifecycleOwner) { meals ->
            binding.tvTotalMeals.text = meals.size.toString()

            // Calculate meals from the last 7 days
            val weekAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000)
            val weeklyMeals = meals.count { it.date.time >= weekAgo }
            binding.tvTotalMealsThisWeek.text = weeklyMeals.toString()
        }
    }

    private fun showLogoutConfirmation() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Logout") { dialog, which ->
                performLogout()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun performLogout() {
        // Clear the current user data
        viewModel.setCurrentUser(null)

        // Clear workouts and meals data
        viewModel.clearAllData()

        // Navigate back to login screen
        navigateToLogin()
    }

    private fun navigateToLogin() {
        // Use Navigation Component to navigate to login
        // Make sure you have loginFragment in your navigation graph
        try {
            findNavController().navigate(R.id.loginFragment)
        } catch (e: Exception) {
            // If navigation fails, you might need to handle this differently
            // For now, we'll just finish the activity as fallback
            requireActivity().finish()
        }
    }

    private fun showEditProfileDialog() {
        // Implement edit profile functionality
        // You can create a dialog or new fragment for editing profile
        showMessage("Edit profile feature coming soon!")
    }

    private fun showMessage(message: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}