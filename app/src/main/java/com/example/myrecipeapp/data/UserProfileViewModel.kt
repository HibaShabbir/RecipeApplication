package com.example.myrecipeapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.myrecipeapp.data.MyDatabase
import com.example.myrecipeapp.data.UserProfile
import com.example.myrecipeapp.data.UserProfileRepository
import kotlinx.coroutines.launch

// UserProfileViewModel
class UserProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserProfileRepository
    val allUserProfiles: LiveData<List<UserProfile>>

    init {
        val userProfileDao = MyDatabase.getDatabase(application).userProfileDao()
        repository = UserProfileRepository(userProfileDao)
        allUserProfiles = repository.allUserProfilesLiveData
    }

    fun insertUserProfile(userProfile: UserProfile) {
        viewModelScope.launch {
            repository.insertUserProfile(userProfile)
        }
    }

    suspend fun findUserProfile(username: String, password: String): LiveData<UserProfile?> {
        return repository.findUserProfile(username, password)
    }
}

