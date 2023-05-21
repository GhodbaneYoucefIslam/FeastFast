package com.example.feastfast.ui.restaurant

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.feastfast.databinding.ActivityRestaurantBinding
import com.example.feastfast.models.Restaurant
import com.google.android.material.tabs.TabLayout

class RestaurantActivity : AppCompatActivity() {
    private var tabLayout: TabLayout? = null
    private var viewPager2: ViewPager2? = null
    private var adapter: RestaurantMenuAdapter? = null
    lateinit var binding: ActivityRestaurantBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRestaurantBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    public fun getCurrentRestaurant():Restaurant{
        return intent.getSerializableExtra("Restaurant") as Restaurant
    }

}