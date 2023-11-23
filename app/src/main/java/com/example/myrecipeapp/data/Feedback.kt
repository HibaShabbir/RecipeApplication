package com.example.myrecipeapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feedback")
data class Feedback(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String,
    val feedbackText: String,
    val rating : Int
)
