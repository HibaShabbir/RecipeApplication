package com.example.myrecipeapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myrecipeapp.databinding.FragmentFavouriteRecipesBinding


class FavouriteRecipes : Fragment() {
    private lateinit var favourite_recipes_binding: FragmentFavouriteRecipesBinding
    private var favouriteRecipes : ArrayList<FavouriteRecipesModel>  = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        favourite_recipes_binding =  FragmentFavouriteRecipesBinding.inflate(layoutInflater, container, false)

        val favouriteRecipes : ArrayList<FavouriteRecipesModel> = ArrayList()
        val favouriteRecipesRV = favourite_recipes_binding.showFavouritesRecyclerView
        favouriteRecipesRV.setLayoutManager(LinearLayoutManager(requireContext()))
        //val adapter :RecipeAdapter= RecipeAdapter(requireContext())
        //val model : FavouriteRecipesModel = FavouriteRecipesModel()

        return favourite_recipes_binding.root
    }

}