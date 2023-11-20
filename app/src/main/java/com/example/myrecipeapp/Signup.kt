package com.example.myrecipeapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.myrecipeapp.data.UserProfile
import com.example.myrecipeapp.databinding.FragmentSignupBinding


class Signup : Fragment() {
    private lateinit var _binding: FragmentSignupBinding
    private lateinit var viewModel: UserProfileViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //val authActivity: AuthActivity? = requireActivity() as? AuthActivity
        _binding = FragmentSignupBinding.inflate(layoutInflater, container, false)
        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(UserProfileViewModel::class.java)

        val navControllerAuth = activity?.run{
            findNavController(R.id.navControllerAuth)
        }


        //navigate to home
        _binding.buttonSignup.setOnClickListener {
            val name = _binding.editTextName.text.toString()
            val username = _binding.editTextNewUsername.text.toString()
            val password = _binding.editTextNewPassword.text.toString()
            val dietPreference = _binding.spinnerGroupDiet.selectedItem.toString()
            val languagePreference = _binding.spinnerLanguagePreferences.selectedItem.toString()

            // Check if any of the fields are empty
            if (name.isBlank() || username.isBlank() || password.isBlank()) {
                // Show a toast message indicating that fields must be filled
                showToast("Please fill in all the fields.")
            } else {
                // Create a UserProfile object
                val userProfile = UserProfile(
                    username = username,
                    password = password,
                    name = name,
                    dietaryPreference = dietPreference,
                    languagePreference = languagePreference
                )

                // Insert the user profile into the Room database
                viewModel.insertUserProfile(userProfile)

                // Navigate to MainActivity
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
            }
        }

        _binding.textViewLogin.setOnClickListener{
            //on click navigate to login page
            val action = SignupDirections.actionSignupToLogin()
            navControllerAuth?.let {
                it.navigate(action)
            }
        }
        return _binding.root
    }

    // Helper function to show a toast message
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}