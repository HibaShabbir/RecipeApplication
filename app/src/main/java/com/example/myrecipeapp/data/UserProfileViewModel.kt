package com.example.myrecipeapp

import android.app.Application
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myrecipeapp.data.MyDatabase
import com.example.myrecipeapp.data.UserProfile
import com.example.myrecipeapp.data.UserProfileRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

// UserProfileViewModel
class UserProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserProfileRepository
    val allUserProfiles: LiveData<List<UserProfile>>
    private val currentUser = AuthenticationManager.currentUser

    // Expose LiveData for validation status
    private val _passwordValidationStatus = MutableLiveData<Boolean>()
    val passwordValidationStatus: LiveData<Boolean>
        get() = _passwordValidationStatus

    private val _usernameValidationStatus = MutableLiveData<Boolean>()
    val usernameValidationStatus: LiveData<Boolean>
        get() = _usernameValidationStatus

    init {
        val userProfileDao = MyDatabase.getDatabase(application).userProfileDao()
        repository = UserProfileRepository(userProfileDao)
        allUserProfiles = repository.allUserProfilesLiveData
    }

    fun insertUserProfile(
        name: String,
        username: String,
        password: String,
        dietPreference: String,
        languagePreference: String
    ) {
        if (isPasswordValid(password) && isUsernameValid(username)) {

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userProfile = UserProfile(
                            username = username,
                            password = password,
                            name = name,
                            dietaryPreference = dietPreference,
                            languagePreference = languagePreference
                        )

                        viewModelScope.launch {
                            repository.insertUserProfile(userProfile)
                        }

                        // Update authentication state
                        AuthenticationManager.isAuthenticated = true
                        AuthenticationManager.currentUser = userProfile

                        Log.d("UserViewModel", "Successful username password")
                    }

                }
            _passwordValidationStatus.value = true
            _usernameValidationStatus.value = true
        } else {
            // Notify the UI about the invalid password or username
            _passwordValidationStatus.value = false
            _usernameValidationStatus.value = false
            Log.d("UserViewModel", "Failed username password")
        }
    }

    fun updateUserProfile(
        name: String,
        username: String,
        password: String,
        dietPreference: String,
        languagePreference: String
    ) {

        val updatedUserProfile = UserProfile(
            username = username,
            password = password,
            name = name,
            dietaryPreference = dietPreference,
            languagePreference = languagePreference
        )
        viewModelScope.launch {
            repository.updateUserProfile(updatedUserProfile)
        }


    }

    private fun isPasswordValid(password: String): Boolean {
        val passwordPattern =
            "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$".toRegex()
        return password.matches(passwordPattern)
    }

    private fun isUsernameValid(username: String): Boolean {
        // Simple check for email format
        val emailPattern = Patterns.EMAIL_ADDRESS
        return emailPattern.matcher(username).matches()
    }

    suspend fun loginUser(username: String, password: String): LiveData<UserProfile?> {
        return repository.loginUser(username, password)
    }

    suspend fun findUserProfile(username: String): LiveData<UserProfile?> {
        return repository.findUserProfile(username)
    }

}

