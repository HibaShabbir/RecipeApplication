package com.example.myrecipeapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey val username: String,
    val password : String,
    val name :String,
    val dietaryPreference: String,
    val languagePreference: String
)