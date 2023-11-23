package com.example.myrecipeapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myrecipeapp.RecipeSearchAPI.BaseRecipe
import com.example.myrecipeapp.RecipeSearchAPI.Recipe
import com.example.myrecipeapp.RecipeSearchAPI.RecipeApi
import com.example.myrecipeapp.data.RecipeEntity
import com.example.myrecipeapp.data.RecipeViewModel
import com.example.myrecipeapp.databinding.FragmentSearchRecipesBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.UUID

const val BASE_URL = "https://api.edamam.com/api/"
const val API_ID = "80412d40"
const val API_KEY = "0c5d75b04d0d93e9ba7a242d31bcc64c"
const val AUTH_TOKEN = "Bearer YOUR_TOKEN_HERE"


fun createRecipeApi(): RecipeApi {
    val okHttpClient = OkHttpClient.Builder().build()

    val retrofit = Retrofit.Builder().baseUrl("https://api.edamam.com/api/") // Update the base URL
        .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build()

    return retrofit.create(RecipeApi::class.java)
}


class SearchRecipes : Fragment() {
    private lateinit var _binding: FragmentSearchRecipesBinding
    private lateinit var recipeAdapter: RecipeAdapter // You need to create an adapter for your RecyclerView

    private val recipeViewModel: RecipeViewModel by viewModels()

    private val addToFavoritesListener: (BaseRecipe) -> Unit = { baseRecipe ->
        // Convert BaseRecipe to RecipeEntity before adding to favorites
        val recipeEntity = RecipeEntity(
            recipeId = generateRecipeId(), // You need to generate a unique ID for the recipe
            baseRecipe = baseRecipe
            // Other specific properties for Room database
        )

        recipeViewModel.addToFavorites(recipeEntity)
    }
    private fun generateRecipeId(): Long {
        return UUID.randomUUID().mostSignificantBits
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchRecipesBinding.inflate(layoutInflater, container, false)
        setupUI()
        return _binding.root
    }

    private fun setupUI() {
        val btnFilter = _binding.btnFilter
        val filterOptionsLayout: LinearLayout = _binding.filterOptionsLayout
        val recyclerView: RecyclerView = _binding.recyclerView
        val searchBar: EditText = _binding.searchBar
        val floatingSearchButton: FloatingActionButton = _binding.floatingSearchButton
        val spinnerMealType: Spinner = _binding.spinnerMealType
        val spinnerHealthTypes: Spinner = _binding.spinnerHealthTypes
        val spinnerGroupDiet: Spinner = _binding.spinnerGroupDiet
        val spinnerDishType: Spinner = _binding.spinnerDishType


        btnFilter.setOnClickListener {
            if (filterOptionsLayout.visibility == View.VISIBLE) {
                filterOptionsLayout.visibility = View.GONE
            } else {
                filterOptionsLayout.visibility = View.VISIBLE
            }
        }

        // Initialize your RecyclerView and its adapter
        val recipes: ArrayList<Recipe> = ArrayList()
        val recipesRecyclerView = _binding.recyclerView
        recipesRecyclerView.setLayoutManager(LinearLayoutManager(requireContext()))
        val recipeAdapter: RecipeAdapter = RecipeAdapter(requireContext(),addToFavoritesListener)

        recipesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        recipesRecyclerView.adapter = recipeAdapter

        // Call your API method here (you need to implement the API service)
        val api = createRecipeApi()
        floatingSearchButton.setOnClickListener {
            val query = searchBar.text.toString()
            val mealType = spinnerMealType.selectedItem.toString()
            val healthType = spinnerHealthTypes.selectedItem.toString()
            val diet = spinnerGroupDiet.selectedItem.toString()
            val dishType = spinnerDishType.selectedItem.toString()
            Log.d("RecipeAPI", "Before making API request")
            GlobalScope.launch {
                try {
                    val responseSearch = api.searchRecipes(
                        query = query,
                        appId = API_ID,
                        appKey = API_KEY,
                        diet = diet,
                        health = healthType,
                        mealType = mealType,
                        dishType = dishType,
                        calories = "0-3000"
                    )
                    Log.d("RecipeAPI", "Recipes loaded successfully: recipes")

                    if (responseSearch.isSuccessful) {
                        val simpleRecipes: List<BaseRecipe> =
                            responseSearch.body()?.hits?.map { hit ->
                                val image = hit.recipe?.image ?: ""
                                val label = hit.recipe?.label ?: ""
                                val ingredientLines = hit.recipe?.ingredientLines ?: emptyList()
                                val url = hit.recipe?.url ?: ""
                                val calories = hit.recipe?.calories ?: 0.0

                              // Log.d("RecipeAPI", "Mapping Recipe - Label: $label, Ingredients: $ingredientLines, URL: $url, Calories: $calories")

                                BaseRecipe(
                                    image = image,
                                    label = label,
                                    ingredients = ingredientLines.size,
                                    url = url,
                                    calories = calories
                                )
                            } ?: emptyList()
                        Log.d("RecipeAPI", "Recipes loaded successfully: ${simpleRecipes.size} recipes")
                        activity?.runOnUiThread {
                            // Update the adapter with the received recipes
                            recipeAdapter.setRecipesList(simpleRecipes)
                        }
                        Log.d("RecipeAPI", "adapter work done successfully: ${simpleRecipes.size} recipes")
                    } else {
                        Log.d("RecipeAPI", "Error: ${responseSearch.code()}")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }

    }
}
