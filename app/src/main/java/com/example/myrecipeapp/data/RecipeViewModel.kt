package com.example.myrecipeapp.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.myrecipeapp.RecipeSearchAPI.BaseRecipe
import kotlinx.coroutines.launch


class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: RecipeRepository

    init {
        val recipeDao = MyDatabase.getDatabase(application).recipeDao()
        repository = RecipeRepository(recipeDao)
    }

    fun getAllRecipes(): LiveData<List<BaseRecipe>> {
        return repository.getAllRecipes().map { recipeEntities ->
            recipeEntities.map { it.baseRecipe }
        }
    }

    fun getAllRecipesForUser(userId: String): LiveData<List<BaseRecipe>> {
        return repository.getAllRecipesForUser(userId).map { recipeEntities ->
            recipeEntities.map { it.baseRecipe }
        }
    }


    fun getRecipeById(recipeId: String): LiveData<RecipeEntity> {
        return repository.getRecipeById(recipeId)
    }

    fun addToFavorites(recipe: RecipeEntity) {
        viewModelScope.launch {
            repository.insertRecipe(recipe)
        }
    }

    fun removeFromFavorites(recipe: RecipeEntity) {
        viewModelScope.launch {
            repository.deleteRecipe(recipe)
        }
    }
}
