package com.example.myrecipeapp.data

import androidx.lifecycle.LiveData

class UserProfileRepository(private val userProfileDao: UserProfileDao) {
    val allUserProfilesLiveData: LiveData<List<UserProfile>> = userProfileDao.getAllUserProfiles()

    suspend fun insertUserProfile(userProfile: UserProfile) {
        userProfileDao.insertUserProfile(userProfile)
    }

    suspend fun loginUser(username: String, password: String): LiveData<UserProfile?> {
        return userProfileDao.loginUser(username, password)
    }

    suspend fun findUserProfile(username: String): LiveData<UserProfile?> {
        return userProfileDao.findUserProfile(username)
    }

    suspend fun updateUserProfile(userProfile: UserProfile) {
        userProfileDao.updateUserProfile(userProfile)
    }


    fun getAllUserProfiles(): LiveData<List<UserProfile>> {
        return allUserProfilesLiveData
    }
}

