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
import com.example.myrecipeapp.databinding.FragmentSignupBinding

class Signup : Fragment() {
    private lateinit var _binding: FragmentSignupBinding
    private lateinit var viewModel: UserProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this).get(UserProfileViewModel::class.java)

        val navControllerAuth = activity?.run {
            findNavController(R.id.navControllerAuth)
        }

        _binding.buttonSignup.setOnClickListener {
            val name = _binding.editTextName.text.toString()
            val username = _binding.editTextNewUsername.text.toString()
            val password = _binding.editTextNewPassword.text.toString()
            val dietPreference = _binding.spinnerGroupDiet.selectedItem.toString()
            val languagePreference = _binding.spinnerLanguagePreferences.selectedItem.toString()

            // Check if any of the fields are empty
            if (name.isBlank() || username.isBlank() || password.isBlank()) {
                showToast("Please fill in all the fields.")
            } else {
                // Insert the user profile into the Room database
                viewModel.insertUserProfile(
                    name,
                    username,
                    password,
                    dietPreference,
                    languagePreference
                )



                // Observe username validation status
                viewModel.usernameValidationStatus.observe(viewLifecycleOwner) { isValid ->
                    if (isValid.not())
                        showToast("Invalid username. Please enter a valid email address.")
                    else{
                        // Observe password validation status
                        viewModel.passwordValidationStatus.observe(viewLifecycleOwner) { isValid ->
                            if (isValid.not())
                            // Password is invalid, show a toast message
                                showToast("Invalid password. Password must contain at least one uppercase letter, one digit, and one special character.")
                            else{
                                showToast("Successful Signup!")
                                val intent = Intent(activity, MainActivity::class.java)
                                startActivity(intent)

                            }
                        }
                    }
                }
            }
        }

        _binding.textViewLogin.setOnClickListener {
            // On click, navigate to the login page
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
