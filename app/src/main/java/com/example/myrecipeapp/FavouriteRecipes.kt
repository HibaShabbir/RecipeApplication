package com.example.myrecipeapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myrecipeapp.RecipeSearchAPI.BaseRecipe
import com.example.myrecipeapp.RecipeSearchAPI.Recipe
import com.example.myrecipeapp.data.RecipeEntity
import com.example.myrecipeapp.data.RecipeViewModel
import com.example.myrecipeapp.databinding.FragmentFavouriteRecipesBinding


class FavouriteRecipes : Fragment() {
    private lateinit var _binding: FragmentFavouriteRecipesBinding
    private lateinit var viewModel: RecipeViewModel
    private var favouriteRecipes: ArrayList<Recipe> = ArrayList()

    private val adapter: RecipeAdapter by lazy {
        RecipeAdapter(requireContext()) { recipe ->
            viewModel.addToFavorites(mapToRecipeEntity(recipe))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RecipeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouriteRecipesBinding.inflate(layoutInflater, container, false)
        val favouriteRecipesRV = _binding.showFavouritesRecyclerView
        favouriteRecipesRV.layoutManager = LinearLayoutManager(requireContext())
        favouriteRecipesRV.adapter = adapter

        observeFavouriteRecipes()

        return _binding.root
    }

    private fun observeFavouriteRecipes() {
        viewModel.getAllRecipes().observe(viewLifecycleOwner) { recipes ->
            adapter.setRecipesList(recipes)
        }
    }

    private fun mapToRecipeEntity(recipe: BaseRecipe): RecipeEntity {
        return RecipeEntity(
            baseRecipe = recipe
        )
    }
}

