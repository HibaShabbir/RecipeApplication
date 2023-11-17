package com.example.myrecipeapp.RecipeSearchAPI

data class RecipeApiResponse(
    val from: Int?,
    val to: Int?,
    val count: Int?,
    val _links: Links?,
    val hits: List<Hit>
)