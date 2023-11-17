package com.example.myrecipeapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey val recipeId: String,
    val image: String,
    val label: String,
    val ingredientsCount: Int,
    val url: String,
    val calories: Double
)