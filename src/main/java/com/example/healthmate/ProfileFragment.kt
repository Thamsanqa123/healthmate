package com.example.healthmate

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.healthmate.databinding.FragmentProfileBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SharedViewModel

    private lateinit var sharedPrefs: android.content.SharedPreferences
    private var language: String = "en"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        sharedPrefs = requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)
        language = sharedPrefs.getString("language", "en") ?: "en"

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        setupClickListeners()
        observeData()
        applyLanguage()
    }

    private fun applyLanguage() {
        // Buttons
        binding.btnSettings.text = if (language == "zulu") "Izilungiselelo" else "Settings"
        binding.btnLogout.text = if (language == "zulu") "Phuma" else "Logout"
        binding.btnEditProfile.text = if (language == "zulu") "Hlela Iphrofayela" else "Edit Profile"


         binding.tvTotalWorkoutsLabel.text = if (language == "zulu") "Ukuziqeqesha Konke" else "Total Workouts"
        binding.tvTotalMealsLabel.text = if (language == "zulu") "Izidlo Zonke" else "Total Meals"
    }

    private fun setupClickListeners() {
        binding.btnEditProfile.setOnClickListener {
            showEditProfileDialog()
        }

        binding.btnSettings.setOnClickListener {
            showMessage(if (language == "zulu") "Izilungiselelo zisazayo!" else "Settings feature coming soon!")
        }

        binding.btnLogout.setOnClickListener {
            showLogoutConfirmation()
        }
    }

    private fun observeData() {
        viewModel.currentUser.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                binding.tvUserName.text = user.fullName
                binding.tvUserEmail.text = user.email
                binding.tvMemberSince.text = if (language == "zulu")
                    "Umlungu kusukela ${SimpleDateFormat("MMM yyyy").format(user.createdAt)}"
                else "Member since ${SimpleDateFormat("MMM yyyy").format(user.createdAt)}"
            } else {
                navigateToLogin()
            }
        }

        viewModel.workouts.observe(viewLifecycleOwner) { workouts ->
            binding.tvTotalWorkouts.text = workouts.size.toString()
            val weekAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000)
            val weeklyWorkouts = workouts.count { it.date.time >= weekAgo }
            binding.tvTotalWorkoutsThisWeek.text = weeklyWorkouts.toString()
        }

        viewModel.meals.observe(viewLifecycleOwner) { meals ->
            binding.tvTotalMeals.text = meals.size.toString()
            val weekAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000)
            val weeklyMeals = meals.count { it.date.time >= weekAgo }
            binding.tvTotalMealsThisWeek.text = weeklyMeals.toString()
        }
    }

    private fun showLogoutConfirmation() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(if (language == "zulu") "Phuma" else "Logout")
            .setMessage(if (language == "zulu") "Uqinisekile ukuthi ufuna ukuphuma?" else "Are you sure you want to logout?")
            .setPositiveButton(if (language == "zulu") "Phuma" else "Logout") { dialog, which ->
                performLogout()
            }
            .setNegativeButton(if (language == "zulu") "Khansela" else "Cancel", null)
            .show()
    }

    private fun performLogout() {
        viewModel.setCurrentUser(null)
        viewModel.clearAllData()
        navigateToLogin()
    }

    private fun navigateToLogin() {
        try {
            findNavController().navigate(R.id.loginFragment)
        } catch (e: Exception) {
            requireActivity().finish()
        }
    }

    private fun showEditProfileDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_profile, null)

        val etFullName = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etFullName)
        val etEmail = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etEmail)
        val btnSave = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnSave)
        val btnCancel = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnCancel)
        val tvDialogTitle = dialogView.findViewById<TextView>(R.id.tvDialogTitle)

        // Pre-fill existing data
        viewModel.currentUser.value?.let { user ->
            etFullName.setText(user.fullName)
            etEmail.setText(user.email)
        }

        // Localize text based on language
        if (language == "zulu") {
            tvDialogTitle.text = "Hlela Iphrofayela"
            etFullName.hint = "Igama Eligcwele"
            etEmail.hint = "I-imeyili"
            btnSave.text = "Londoloza"
            btnCancel.text = "Khansela"
        }

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .show()

        btnCancel.setOnClickListener { dialog.dismiss() }

        btnSave.setOnClickListener {
            val newName = etFullName.text.toString().trim()
            val newEmail = etEmail.text.toString().trim()

            if (newName.isEmpty()) {
                etFullName.error = if (language == "zulu") "Igama liyadingeka" else "Name required"
                return@setOnClickListener
            }
            if (newEmail.isEmpty()) {
                etEmail.error = if (language == "zulu") "I-imeyili iyadingeka" else "Email required"
                return@setOnClickListener
            }

            val updatedUser = viewModel.currentUser.value?.copy(
                fullName = newName,
                email = newEmail
            )
            viewModel.setCurrentUser(updatedUser)
            dialog.dismiss()
        }
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
