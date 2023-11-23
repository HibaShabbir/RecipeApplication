package com.example.myrecipeapp.RecipeSearchAPI

data class BaseRecipe(
    val image: String,
    val label: String,
    val ingredients: Int,
    val url: String,
    val calories: Double
)