package com.example.myrecipeapp.data

import androidx.lifecycle.LiveData

class UserProfileRepository(private val userProfileDao: UserProfileDao) {
    val allUserProfilesLiveData: LiveData<List<UserProfile>> = userProfileDao.getAllUserProfiles()

    suspend fun insertUserProfile(userProfile: UserProfile) {
        userProfileDao.insertUserProfile(userProfile)
    }

    suspend fun findUserProfile(username: String, password: String): LiveData<UserProfile?> {
        return userProfileDao.findUserProfile(username, password)
    }

    suspend fun updateUserProfile(userProfile: UserProfile) {
        userProfileDao.updateUserProfile(userProfile)
    }

    fun getAllUserProfiles(): LiveData<List<UserProfile>> {
        return allUserProfilesLiveData
    }
}

