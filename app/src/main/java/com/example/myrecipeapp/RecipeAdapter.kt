package com.example.myrecipeapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.myrecipeapp.RecipeSearchAPI.BaseRecipe
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RecipeAdapter(
    private val context: Context,
    private val addToFavoritesListener: (BaseRecipe) -> Unit
) : RecyclerView.Adapter<RecipeAdapter.ItemsViewHolder>() {

    private val recipesList = ArrayList<BaseRecipe>()

    fun setRecipesList(newList: List<BaseRecipe>) {
        recipesList.clear()
        recipesList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.grid_item_recipe, parent, false)
        return ItemsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return recipesList.size
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        holder.recipeName.text = recipesList[position].label
        // Load the image using Coil
        holder.recipeImage.load(recipesList[position].image)
        holder.recipeIngridientsCount.text = recipesList[position].ingredients.toString()
        holder.recipeCaloriesCount.text = recipesList[position].calories.toString()
        holder.addToFavoritesButton.setOnClickListener {
            val recipe = recipesList[position]
            addToFavoritesListener.invoke(recipe)
        }

        holder.cardView.setOnClickListener {
            // Handle the click event here, e.g., open the recipe link
            val recipeUrl = recipesList[position].url
            if (recipeUrl.isNotBlank() && android.util.Patterns.WEB_URL.matcher(recipeUrl).matches()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(recipeUrl))
                context.startActivity(intent)
            } else {
                // Log or display a message indicating that the URL is empty or not valid
                Log.e("RecipeAdapter", "Invalid or empty recipe URL: $recipeUrl")
            }
        }

    }

    class ItemsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeName = itemView.findViewById<TextView>(R.id.recipeName)
        val recipeImage = itemView.findViewById<ImageView>(R.id.recipeImage)
        val recipeCaloriesCount = itemView.findViewById<TextView>(R.id.countCalories)
        val recipeIngridientsCount = itemView.findViewById<TextView>(R.id.countIngridients)
        val cardView = itemView.findViewById<CardView>(R.id.cardView)
        val addToFavoritesButton = itemView.findViewById<FloatingActionButton>(R.id.addToFavouritesButton)
    }
}
