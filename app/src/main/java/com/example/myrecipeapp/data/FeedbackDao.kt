package com.example.myrecipeapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myrecipeapp.data.Feedback

@Dao
interface FeedbackDao {
    @Insert
    suspend fun insertFeedback(feedback: Feedback)

    @Query("SELECT * FROM feedback WHERE username = :username")
    suspend fun getFeedbackByUser(username: String): List<Feedback>

}
