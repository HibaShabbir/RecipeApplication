package com.example.myrecipeapp.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class FeedbackViewModel(application: Application, private val feedbackDao: FeedbackDao) : AndroidViewModel(application) {

    @Suppress("unused")
    constructor(application: Application) : this(application, MyDatabase.getDatabase(application).feedbackDao())

    suspend fun findUserProfile(username: String, password: String) {
        // Implementation of findUserProfile function
    }

    // Function to insert feedback into the database
    fun insertFeedback(feedback: Feedback) {
        viewModelScope.launch {
            try {
                // Insert feedback into the database
                feedbackDao.insertFeedback(feedback)
                // Optionally, you can perform any UI updates or other actions after the insertion
            } catch (e: Exception) {
                // Handle any exceptions that may occur during the insertion
                // This might include database-related errors or coroutine cancellation exceptions
                // You can log the error or handle it as appropriate for your app
                // For example, you might want to display an error message to the user
                // or perform some cleanup.
            }
        }
    }
}