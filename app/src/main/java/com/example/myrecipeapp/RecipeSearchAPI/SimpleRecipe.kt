package com.example.myrecipeapp.RecipeSearchAPI

data class SimpleRecipe(
    val image: String,
    val label: String,
    val ingredients: List<String>,
    val url: String,
    val calories: Double
)