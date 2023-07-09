package com.example.feastfast.ui.restaurant

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.feastfast.R
import com.example.feastfast.databinding.ActivityRestaurantBinding
import com.example.feastfast.models.Restaurant

class RestaurantActivity : AppCompatActivity() {

    private var restaurant: Restaurant? = null
    lateinit var binding: ActivityRestaurantBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRestaurantBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Retrieve the Restaurant object from intent extras
        restaurant = intent.getSerializableExtra("Restaurant") as? Restaurant


        // Get the NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        // Get the NavController
        val navController = navHostFragment.navController

        // Pass the Restaurant object as an argument when navigating to the main fragment
        val bundle = Bundle()
        bundle.putSerializable("Restaurant", restaurant)
        navController.setGraph(navController.graph, bundle)
    }

    fun getCurrentRestaurant(): Restaurant {
        return intent.getSerializableExtra("Restaurant") as Restaurant
    }


}