package com.example.myrecipeapp.RecipeSearchAPI

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeApi {
    @GET("recipes/v2?type=public")
    suspend fun searchRecipes(
        @Query("q") query: String,
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String,
        @Query("diet") diet: String,
        @Query("health") health: String,
        @Query("mealType") mealType: String,
        @Query("dishType") dishType: String,
        @Query("calories") calories: String
    ): Response<RecipeApiResponse>

    //the url should have been like https://api.edamam.com/api/recipes/v2?type=public&q=salad&app_id=80412d40&app_key=0c5d75b04d0d93e9ba7a242d31bcc64c&diet=balanced&health=gluten-free&mealType=Lunch&dishType=Salad&calories=0-3000"
}
