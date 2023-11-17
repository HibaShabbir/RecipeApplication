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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myrecipeapp.RecipeSearchAPI.Recipe
import com.example.myrecipeapp.RecipeSearchAPI.RecipeApi
import com.example.myrecipeapp.RecipeSearchAPI.SimpleRecipe
import com.example.myrecipeapp.databinding.FragmentSearchRecipesBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
        val recipeAdapter: RecipeAdapter = RecipeAdapter(requireContext())

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

                    if (responseSearch.isSuccessful) {
                        val simpleRecipes: List<SimpleRecipe> =
                            responseSearch.body()?.hits?.map { hit ->
                                SimpleRecipe(
                                    image = hit.recipe?.image
                                        ?: "", // Handle nullability for each property
                                    label = hit.recipe?.label ?: "",
                                    ingredients = hit.recipe?.ingredientLines ?: emptyList(),
                                    url = hit.recipe?.url ?: "",
                                    calories = hit.recipe?.calories ?: 0.0
                                )
                            } ?: emptyList()

                        activity?.runOnUiThread {
                            // Update the adapter with the received recipes
                            recipeAdapter.setRecipesList(simpleRecipes)
                        }
                    } else {
                        Log.e("RecipeAPI", "Error: ${responseSearch.code()}")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }

    }
}
