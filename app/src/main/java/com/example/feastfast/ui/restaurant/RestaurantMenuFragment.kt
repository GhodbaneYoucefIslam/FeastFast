package com.example.feastfast.ui.restaurant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.feastfast.R
import com.example.feastfast.databinding.FragmentRestaurantMenuBinding
import com.example.feastfast.models.MenuItem
import com.example.feastfast.models.Restaurant

class RestaurantMenuFragment : Fragment() {

    var binding :FragmentRestaurantMenuBinding? = null
    lateinit var currentRestaurant: Restaurant
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRestaurantMenuBinding.inflate(inflater,container,false)
        val view = binding!!.root
        val bundle = arguments
        currentRestaurant = arguments?.getSerializable("Restaurant") as Restaurant

        binding!!.RecycleViewMenu.layoutManager = LinearLayoutManager(requireActivity())
        binding!!.RecycleViewMenu.adapter = RestaurantMenuItemsAdapter(getMenuItems(),requireActivity(),bundle!!.getString("nameCategorie"))
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    fun getMenuItems() : List<MenuItem>{
        val data = listOf<MenuItem>(
            MenuItem(name = "Mediterranean wrap", image = R.drawable.image_wrap, price = 400.0, description = "A delicious menu item for middle east cuisine lovers", restaurantId = currentRestaurant.id, restaurantName = currentRestaurant.name ),
            MenuItem(name="Pizza",image=R.drawable.image_pizza,price=300.0, description = "Pizza! not much to explain",restaurantId = currentRestaurant.id, restaurantName = currentRestaurant.name),
            MenuItem(name = "Mediterranean wrap", image = R.drawable.image_wrap, price = 400.0, description = "A delicious menu item for middle east cuisine lovers", restaurantId = currentRestaurant.id, restaurantName = currentRestaurant.name),
            MenuItem(name="Pizza",image=R.drawable.image_pizza,price=300.0, description = "Pizza! not much to explain", restaurantId = currentRestaurant.id, restaurantName = currentRestaurant.name),
            MenuItem(name = "Mediterranean wrap", image = R.drawable.image_wrap, price = 400.0, description = "A delicious menu item for middle east cuisine lovers", restaurantId = currentRestaurant.id, restaurantName = currentRestaurant.name),
            MenuItem(name="Pizza",image=R.drawable.image_pizza,price=300.0, description = "Pizza! not much to explain", restaurantId = currentRestaurant.id, restaurantName = currentRestaurant.name),
            MenuItem(name = "Mediterranean wrap", image = R.drawable.image_wrap, price = 400.0, description = "A delicious menu item for middle east cuisine lovers", restaurantId = currentRestaurant.id, restaurantName = currentRestaurant.name),
            MenuItem(name="Pizza",image=R.drawable.image_pizza,price=300.0, description = "Pizza! not much to explain", restaurantId = currentRestaurant.id, restaurantName = currentRestaurant.name),
        )

        return data
    }


}