package com.example.myrecipeapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey val userId: String,
    val username: String,
    val dietaryPreferences: String,
    val languagePreferences: String
)