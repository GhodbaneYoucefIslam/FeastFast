package com.example.feastfast.ui.restaurant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.feastfast.databinding.FragmentRestaurantMenuBinding
import com.example.feastfast.models.MenuItem
import com.example.feastfast.models.Restaurant

class RestaurantMenuFragment : Fragment() {

    var binding :FragmentRestaurantMenuBinding? = null
    private lateinit var currentRestaurant: Restaurant

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRestaurantMenuBinding.inflate(inflater,container,false)
        val view = binding!!.root
        val bundle = arguments
        currentRestaurant = bundle!!.getSerializable("Restaurant") as Restaurant

        // Retrieve the bundle arguments
        val nameCategorie = arguments?.getString("nameCategorie")
        val menuItems = arguments?.getSerializable("menuItems") as? List<MenuItem>
        // Filter the menuItems based on the category name
        var filteredItems = menuItems?.filter { it.categorie == nameCategorie }
        for(item in filteredItems!!){
            item.restaurantName=currentRestaurant.name
            item.restaurantId = currentRestaurant.id
        }


        binding!!.RecycleViewMenu.layoutManager = LinearLayoutManager(requireActivity())
        binding!!.RecycleViewMenu.adapter = RestaurantMenuItemsAdapter(filteredItems,requireActivity(),bundle!!.getString("nameCategorie"))
        return view
    }


}