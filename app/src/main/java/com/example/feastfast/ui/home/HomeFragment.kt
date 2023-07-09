package com.example.feastfast.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.feastfast.R
import com.example.feastfast.databinding.FragmentHomeBinding
import com.example.feastfast.models.Category
import com.example.feastfast.models.retrofit.Endpoint
import com.example.feastfast.models.room.AppDatabase
import com.example.feastfast.ui.address.AddressActivity
import com.example.feastfast.ui.cart.CartActivity
import kotlinx.coroutines.*


class HomeFragment : Fragment() {

    lateinit var myContext : FragmentActivity
    private val categories = listOf(
        Category(name = "Pizza",R.color.primary_color,R.drawable.image_category_pizza),
        Category(name = "Sandwich",R.color.pinkish,R.drawable.image_category_hotdog),
        Category(name = "Shawarma",R.color.brownish,R.drawable.image_category_shawarma),
        Category(name = "Cake",R.color.blackish,R.drawable.image_category_cake),
    )


    lateinit var binding : FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myContext = requireActivity()
        val images = listOf(R.drawable.image_hotspot,R.drawable.ad3,R.drawable.image_hotspot,R.drawable.ad2,R.drawable.image_hotspot)
        val adapter =  AdsViewPagerAdapter(images)
        binding.viewPager2.adapter = adapter
        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ){
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                changeColor()
            }
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                changeColor()
            }
        })

        showCartCount()

        binding.iconDown.setOnClickListener {
            val intent = Intent(myContext, AddressActivity::class.java)
            myContext.startActivity(intent)
        }
        loadTopRated()
        loadTopRaters()
        setAddress()
        binding.recyclerViewTopRated.layoutManager = LinearLayoutManager(myContext,LinearLayoutManager.HORIZONTAL,false)




        binding.recyclerViewCategories.layoutManager = LinearLayoutManager(myContext,LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerViewCategories.adapter=HomeCategoryAdapter(categories,myContext)
        binding.recyclerViewNearYou.layoutManager = LinearLayoutManager(myContext,LinearLayoutManager.HORIZONTAL,false)
       binding.recyclerViewFreeDelivery.layoutManager = LinearLayoutManager(myContext,LinearLayoutManager.HORIZONTAL,false)

        binding.recyclerViewTopPastry.layoutManager = LinearLayoutManager(myContext,LinearLayoutManager.HORIZONTAL,false)




    }

    override fun onResume() {
        super.onResume()
        showCartCount()
        setAddress()
    }

    private fun showCartCount(){
        val items = AppDatabase.getInstance(myContext)!!.getMenuItemDao().getCartContents()
        val isFilled = items.isNotEmpty()
        val redCircle = binding.redCircle
        val numberOfItems = binding.textNumberOfItems
        redCircle.visibility = if(isFilled) View.VISIBLE else View.GONE
        numberOfItems.visibility= if(isFilled) View.VISIBLE else View.GONE
        numberOfItems.text=items.size.toString()
        binding.iconCart.setOnClickListener{
            if (isFilled){
                val intent = Intent(myContext, CartActivity::class.java)
                myContext.startActivity(intent)
            }
        }
    }


    private fun loadTopRated()  {

        val exceptionHandler = CoroutineExceptionHandler{ coroutineContext, throwable ->
            requireActivity().runOnUiThread {
                Toast.makeText(myContext, "request successful with Some unspecified error", Toast.LENGTH_SHORT).show()
            }
        }

        CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
            val pref = requireActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
            val idUser = pref.getInt("idUser",0)
            val response = Endpoint.createEndpoint().getAllRestaurants(idUser)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!.toList()
                    binding.recyclerViewNearYou.adapter=HomeRestaurantAdapter(data,myContext)

                    binding.recyclerViewTopRated.adapter=HomeRestaurantAdapter(data,myContext)

                } else {
                    Toast.makeText(myContext, "Request unsuccessful!", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
    private fun loadTopRaters()  {

        val exceptionHandler = CoroutineExceptionHandler{ coroutineContext, throwable ->
            requireActivity().runOnUiThread {
                Toast.makeText(myContext, "request successful with Some unspecified error", Toast.LENGTH_SHORT).show()
            }
        }

        CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
            val pref = requireActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
            val idUser = pref.getInt("idUser",0)
            val response = Endpoint.createEndpoint().getAllRestaurantsTopRaters(idUser)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!.toList()


                    binding.recyclerViewFreeDelivery.adapter=HomeRestaurantAdapter(data,myContext)

                    binding.recyclerViewTopPastry.adapter=HomeRestaurantAdapter(data,myContext)

                } else {
                    Toast.makeText(myContext, "Request unsuccessful!", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }


    fun changeColor(){

        when (binding.viewPager2.currentItem){
            0 -> {
                binding.ind1.setBackgroundColor( resources.getColor(R.color.addToCart))
                binding.ind2.setBackgroundColor( resources.getColor(R.color.addToCart_50_opacity))
                binding.ind3.setBackgroundColor( resources.getColor(R.color.addToCart_50_opacity))
                binding.ind4.setBackgroundColor( resources.getColor(R.color.addToCart_50_opacity))
                binding.ind5.setBackgroundColor( resources.getColor(R.color.addToCart_50_opacity))

            }
            1 -> {
                binding.ind2.setBackgroundColor( resources.getColor(R.color.addToCart))
                binding.ind1.setBackgroundColor( resources.getColor(R.color.addToCart_50_opacity))
                binding.ind3.setBackgroundColor( resources.getColor(R.color.addToCart_50_opacity))
                binding.ind4.setBackgroundColor( resources.getColor(R.color.addToCart_50_opacity))
                binding.ind5.setBackgroundColor( resources.getColor(R.color.addToCart_50_opacity))
            }
            2 -> {
                binding.ind3.setBackgroundColor( resources.getColor(R.color.addToCart))
                binding.ind1.setBackgroundColor( resources.getColor(R.color.addToCart_50_opacity))
                binding.ind2.setBackgroundColor( resources.getColor(R.color.addToCart_50_opacity))
                binding.ind4.setBackgroundColor( resources.getColor(R.color.addToCart_50_opacity))
                binding.ind5.setBackgroundColor( resources.getColor(R.color.addToCart_50_opacity))

            }

            3 -> {
                binding.ind4.setBackgroundColor( resources.getColor(R.color.addToCart))
                binding.ind1.setBackgroundColor( resources.getColor(R.color.addToCart_50_opacity))
                binding.ind2.setBackgroundColor( resources.getColor(R.color.addToCart_50_opacity))
                binding.ind3.setBackgroundColor( resources.getColor(R.color.addToCart_50_opacity))
                binding.ind5.setBackgroundColor( resources.getColor(R.color.addToCart_50_opacity))

            }
            4 -> {
                binding.ind5.setBackgroundColor( resources.getColor(R.color.addToCart))
                binding.ind1.setBackgroundColor( resources.getColor(R.color.addToCart_50_opacity))
                binding.ind2.setBackgroundColor( resources.getColor(R.color.addToCart_50_opacity))
                binding.ind4.setBackgroundColor( resources.getColor(R.color.addToCart_50_opacity))
                binding.ind3.setBackgroundColor( resources.getColor(R.color.addToCart_50_opacity))
            }
        }
    }

    private fun setAddress(){
        val pref = myContext.getSharedPreferences("myPreferences",Context.MODE_PRIVATE)
        val addressToShow = pref.getString("address","Enter your location")
        binding.textDeliveryAddress.text=addressToShow
    }

}
