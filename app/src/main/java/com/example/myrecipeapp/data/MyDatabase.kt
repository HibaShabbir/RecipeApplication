package com.example.myrecipeapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserProfile::class, Recipe::class], version = 1)
abstract class MyDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun recipeDao(): RecipeDao
}
