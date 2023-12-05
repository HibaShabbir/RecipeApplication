package com.example.myrecipeapp

import LanguageManager
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myrecipeapp.data.UserProfile
import com.example.myrecipeapp.databinding.FragmentEditProfileBinding

class EditProfile : Fragment() {
    private lateinit var _binding: FragmentEditProfileBinding
    private lateinit var userProfileViewModel: UserProfileViewModel
    private var currentUser: UserProfile = AuthenticationManager.currentUser!!
    val languagesList = listOf("English", "Urdu", "Spanish", "French")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Load the selected language from SharedPreferences
        val selectedLanguage = loadSelectedLanguage()
        LanguageManager.updateLanguage(selectedLanguage, requireContext())
        Log.d("EditProfile", "Current language: $selectedLanguage")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("EditProfile", "onCreateView")
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        userProfileViewModel = ViewModelProvider(this).get(UserProfileViewModel::class.java)

        _binding.editName.setText(currentUser.name)
        _binding.editUsername.setText(currentUser.username)
        _binding.editDietaryPreferences.setSelection(getDietPreferenceIndex(currentUser.dietaryPreference))
        _binding.editLanguagePreferences.setSelection(getLanguagePreferenceIndex(currentUser.languagePreference))


        val languageSpinner: Spinner = _binding.editLanguagePreferences
        val currentLanguageCode = loadSelectedLanguage()

        // Determine the current language based on the code
        val currentLanguage = when (currentLanguageCode) {
            "es" -> "Spanish"
            "ur" -> "Urdu"
            "fr" -> "French"
            else -> "English"
        }
        val position = languagesList.indexOf(currentLanguage)
        languageSpinner.setSelection(position)

        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                val selectedLanguage = languageSpinner.selectedItem.toString()
                LanguageManager.updateLanguage(selectedLanguage, requireContext())
                // Save the selected language in SharedPreferences
                saveSelectedLanguage(selectedLanguage)
                // Update UI elements with the new translations
                updateUIWithNewLanguage()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Do nothing
            }
        }
        updateUIWithNewLanguage()

        _binding.logoutButton.setOnClickListener {

            AuthenticationManager.isAuthenticated = false
            AuthenticationManager.currentUser = null

            val intent = Intent(activity, AuthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            activity?.finish()
        }

        // Assume there is a button with ID R.id.saveButton
        val saveButton: Button = _binding.saveButton

        saveButton.setOnClickListener {
            val name = _binding.editName.text.toString()
            val dietPreference = _binding.editDietaryPreferences.selectedItem.toString()
            val languagePreference = _binding.editLanguagePreferences.selectedItem.toString()

            // Call the updateUserProfile method in the ViewModel

            Log.d(
                "saveBtn",
                "Name : $name, Diet : $dietPreference, Language : $languagePreference "
            )
            // Call the updateUserProfile method in the ViewModel
            userProfileViewModel.updateUserProfile(
                name,
                currentUser.username,
                currentUser.password,
                dietPreference,
                languagePreference
            )


        }
        return _binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        saveSelectedLanguage(_binding.editLanguagePreferences.selectedItem.toString())
    }

    override fun onResume() {
        super.onResume()
        val selectedLanguage = loadSelectedLanguage()
        val position = languagesList.indexOf(selectedLanguage)
        _binding.editLanguagePreferences.setSelection(position)
    }

    // Helper function to get the index of diet preference in the spinner
    private fun getDietPreferenceIndex(dietPreference: String): Int {
        // Replace this with your logic to get the index based on the actual values in the spinner
        return languagesList.indexOf(dietPreference)
    }

    // Helper function to get the index of language preference in the spinner
    private fun getLanguagePreferenceIndex(languagePreference: String): Int {
        // Replace this with your logic to get the index based on the actual values in the spinner
        return languagesList.indexOf(languagePreference)
    }
    private fun updateUIWithNewLanguage() {
        Log.d("EditProfile", "updateUIWithNewLanguage")
        // Update UI elements with the new translations
        _binding.pageTitle.text = getString(R.string.edit_profile_title)
        _binding.subtitle.text = getString(R.string.edit_profile_subtitle)
        _binding.editUsername.hint = getString(R.string.username_hint)
        _binding.editName.hint = getString(R.string.name_hint)
        _binding.editChangePassword.hint = getString(R.string.password_hint)
        _binding.logoutButton.text = getString(R.string.btnLogoutText)
    }

    private fun saveSelectedLanguage(language: String) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        sharedPreferences.edit().putString("selected_language", language).apply()
    }

    private fun loadSelectedLanguage(): String {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        return sharedPreferences.getString("selected_language", "en") ?: "en"
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.d("EditProfile", "onConfigurationChanged")
        // Configuration change is handled dynamically, so no need to recreate the activity.
    }
}

