package com.example.myrecipeapp

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.example.myrecipeapp.data.MyDatabase
import com.example.myrecipeapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    //viewBinding and navController for navigation
    companion object {
        private var _database: MyDatabase? = null

        val database: MyDatabase
            get() = _database!!

        fun initializeDatabase(context: Context) {
            _database = Room.databaseBuilder(
                context.applicationContext,
                MyDatabase::class.java, "recipe-database"
            ).build()
        }
    }

    private lateinit var activityBinding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Room database
        initializeDatabase(applicationContext)

        activityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        navController = activityBinding.navControllerContainer.getFragment<NavHostFragment>()
            .findNavController()

        // Access Room database
        val userProfileDao = database.userProfileDao()

        //Nav buttons
        activityBinding.viewHomeButton.setOnClickListener {
            val action = HomeFragmentDirections.actionGlobalHomeFragment()
            navController.navigate(action)
        }

        activityBinding.viewSearchButton.setOnClickListener {
            val action = SearchRecipesDirections.actionGlobalSearchRecipesFragment()
            navController.navigate(action)
        }

        activityBinding.viewProfileButton.setOnClickListener {
            val action = EditProfileDirections.actionGlobalEditProfile()
            navController.navigate(action)
        }

        activityBinding.viewFavouritesButton.setOnClickListener {
            val action = FavouriteRecipesDirections.actionGlobalFavouriteRecipes()
            navController.navigate(action)
        }

        activityBinding.giveFeedbackButton.setOnClickListener {
            val action = GiveFeedbackDirections.actionGlobalGiveFeedbackFragment()
            navController.navigate(action)
        }

    }
}