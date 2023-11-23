package com.example.myrecipeapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.myrecipeapp.data.MyDatabase
import com.example.myrecipeapp.data.UserProfileRepository
import com.example.myrecipeapp.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch

class Login : Fragment() {
    private lateinit var _binding: FragmentLoginBinding
    private lateinit var viewModel: UserProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)

        val navControllerAuth = activity?.run {
            findNavController(R.id.navControllerAuth)
        }

        val userProfileDao = MyDatabase.getDatabase(requireContext()).userProfileDao()
        val repository = UserProfileRepository(userProfileDao)
        viewModel = ViewModelProvider(this).get(UserProfileViewModel::class.java)

        _binding.buttonLogin.setOnClickListener {
            val username = _binding.editTextUsername.text.toString()
            val password = _binding.editTextPassword.text.toString()

            lifecycleScope.launch {
                viewModel.findUserProfile(username, password).observe(viewLifecycleOwner) { userProfile ->
                    if (userProfile != null) {
                        // Login successful
                        showToast("Login successful")

                        // Update authentication state
                        AuthenticationManager.isAuthenticated = true
                        AuthenticationManager.currentUser = userProfile

                        val intent = Intent(activity, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        // Display an error message or handle invalid credentials
                        showToast("Invalid username or password")
                    }
                }
            }
        }


        _binding.textViewSignup.setOnClickListener {
            val action = LoginDirections.actionLoginToSignup()
            navControllerAuth?.navigate(action)
        }

        return _binding.root
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}


