package com.example.feastfast.ui.restaurant

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.feastfast.models.Restaurant

class RestaurantMenuAdapter(fragment: FragmentActivity,val data:List<String>,val currentRestaurant:Restaurant) : FragmentStateAdapter(fragment) {


    override fun createFragment(position: Int): Fragment {
        val fragment : Fragment = RestaurantMenuFragment()
        val bundle = Bundle()
        bundle.putString("nameCategorie",data[position])
        bundle.putSerializable("Restaurant",currentRestaurant)
        fragment.arguments =bundle
        return fragment

    }

    override fun getItemCount():Int {
        return  data.size
    }

}
