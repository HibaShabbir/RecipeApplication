package com.example.myrecipeapp.data

import androidx.lifecycle.LiveData

class RecipeRepository(private val recipeDao: RecipeDao) {
    fun getAllRecipes(): LiveData<List<RecipeEntity>> {
        return recipeDao.getAllRecipes()
    }

    fun getRecipeById(recipeId: String): LiveData<RecipeEntity> {
        return recipeDao.getRecipeById(recipeId)
    }

    suspend fun insertRecipe(recipe: RecipeEntity) {
        recipeDao.insertRecipe(recipe)
    }

    suspend fun deleteRecipe(recipe: RecipeEntity) {
        recipeDao.deleteRecipe(recipe)
    }
}
