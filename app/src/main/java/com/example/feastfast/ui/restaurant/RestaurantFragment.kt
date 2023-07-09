package com.example.feastfast.ui.restaurant

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.feastfast.R
import com.example.feastfast.databinding.FragmentRestaurantBinding
import com.example.feastfast.models.MenuItem
import com.example.feastfast.models.Restaurant
import com.example.feastfast.models.room.AppDatabase
import com.example.feastfast.ui.cart.CartActivity
import com.example.feastfast.util.url
import com.example.feastfast.viewModels.RestaurantViewModel
import com.google.android.material.tabs.TabLayout

class RestaurantFragment : Fragment() {

    lateinit var binding : FragmentRestaurantBinding
    private lateinit var viewModel: RestaurantViewModel
    lateinit var myContext: FragmentActivity
    private var tabLayout: TabLayout? = null
    private var viewPager2: ViewPager2? = null
    private lateinit var currentRestaurant : Restaurant
    private var idRes : Int? =0
    private val REQUEST_CALL_PERMISSION = 0



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        // Retrieve the Restaurant object from arguments

        binding = FragmentRestaurantBinding.inflate(inflater,container,false)
        val view = binding.root
        viewModel = ViewModelProvider(this)[RestaurantViewModel::class.java]

        return  view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //getting current restaurant from activity
        val activity = activity as RestaurantActivity
        currentRestaurant = activity.getCurrentRestaurant()
        //updating ui based on current restaurant


        myContext = requireActivity()
        tabLayout =binding.tabLayout
        viewPager2 = binding.viewPager


        // Retrieve the Restaurant object from arguments
        setResInfo(currentRestaurant)
        binding.viewCartButton.setOnClickListener {
            val intent = Intent(myContext, CartActivity::class.java)
            myContext.startActivity(intent)
            myContext.finish()

        }

        val buttonBack = binding.resBackIcon
        buttonBack.setOnClickListener {
            myContext.finish()

        }
        loadData(currentRestaurant.id)


    }


    private fun loadData(restaurantId : Int) {

        viewModel.data.observe(viewLifecycleOwner) { data ->
            setupViewPagerAndTabLayout(data)
            // Perform any other UI updates using the data
        }

        viewModel.favrite.observe(viewLifecycleOwner) { data ->
            updateFavrite(data)
            // Perform any other UI updates using the data
        }

        viewModel.loadData(restaurantId)

    }


    private fun setResInfo (res : Restaurant?) {
        idRes = res?.id
        binding.resName.text = res?.name
        binding.resCuisine.text = res?.cuisineType
        Glide.with(myContext).load(url +res?.picture) . into(binding!!.resImage)
        Glide.with(myContext).load(url +res?.logo) . into(binding!!.resLogo)
        binding.textgps.text = res?.locationAddress
        val startTime = res?.opening_time?.substring(11,16)
        val endTime = res?.closing_time?.substring(11,16)
        binding.openTime.text = "$startTime-$endTime"
        binding.textrating.text = "${res?.averageRating}(${res?.ratersCount} Rating)"


        if (res?.isPreferred == true ) {
            viewModel.initilizeFavState( 1)
            binding!!.resFavIcon.setImageResource(R.drawable.baseline_favorite_red_24)
            binding.resName.text = res?.name
        }
        else {
            println(res?.isPreferred)

        }
        binding!!.resFavIcon.setOnClickListener {
            favIconHnadler()
        }

        binding!!.resFaceook.setOnClickListener {
            try {
                val fbPageId = res?.fbLink
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/$fbPageId"))
                startActivity(intent)
            } catch (e: Exception) {
                // If the Facebook app is not installed, open Facebook in a browser
                val fbPageUrl = "https://www.facebook.com/"+res?.fbLink
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(fbPageUrl))
                startActivity(intent)
            }
        }

        binding!!.resInstagrame.setOnClickListener {
            val instagramLink = res?.instaLink

            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/$instagramLink"))
                intent.setPackage("com.instagram.android")
                startActivity(intent)
            } catch (e: Exception) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/$instagramLink"))
                startActivity(intent)
            }
        }

        binding!!.gpsArrow.setOnClickListener {
            val latitude = 37.7749
            val longitude = -122.4194

            val mapUri = Uri.parse("geo:$latitude,$longitude")
            val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)
            mapIntent.setPackage("com.google.android.apps.maps")

            if (mapIntent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(mapIntent)
            } else {
                // Google Maps app is not installed, open in browser
                val mapUrl = "https://www.google.com/maps?q=$latitude,$longitude"
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(mapUrl))
                startActivity(browserIntent)
            }
        }
        binding!!.resPhone. setOnClickListener {
            val phoneNumber = res?.phoneNumber

            val callIntent = Intent(Intent.ACTION_DIAL)
            callIntent.data = Uri.parse("tel:$phoneNumber")

            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                startActivity(callIntent)
            } else {

                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CALL_PHONE),
                    REQUEST_CALL_PERMISSION
                )
            }
        }

        binding!!.gpsArrow3.setOnClickListener {
            val data= Bundle()
            data.putInt("id",res!!.id)
            data.putFloat("average",res!!.averageRating)
            data.putInt("count",res!!.ratersCount)
            it.findNavController().navigate(R.id.action_restaurantFragment_to_ratingFragment,data)

        }

    }

   private fun getDistinctCategories(apiResponse:  List<MenuItem>): Array<String> {
        return apiResponse!!.map { it.categorie }.distinct().toTypedArray()
    }

    private fun favIconHnadler () {
        val pref = requireActivity().getSharedPreferences("fileName", Context.MODE_PRIVATE)
        val idUser = pref.getInt("idUser",0)
        viewModel.handleFavClick(idUser ,idRes )

    }
    private fun updateFavrite(data :Int){
        if (data == 1) {
            binding?.resFavIcon?.setImageResource(R.drawable.baseline_favorite_red_24)
        }
        else {
            binding!!.resFavIcon.setImageResource(R.drawable.icon_fav)

        }
    }










    private fun setupViewPagerAndTabLayout(apiResponse:  List<MenuItem>) {

        val categories: Array<String> = getDistinctCategories(apiResponse)

        // Remove all existing tabs
        binding.tabLayout.removeAllTabs()

        for (category in categories) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(category))
        }

        val adapter = RestaurantMenuAdapter(myContext, categories.toList(), currentRestaurant,apiResponse)
        viewPager2!!.adapter = adapter
        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager2!!.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        viewPager2!!.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tabLayout!!.selectTab(tabLayout!!.getTabAt(position))
            }
        })


        binding.viewCartButton.setOnClickListener {
            val intent = Intent(myContext, CartActivity::class.java)
            myContext.startActivity(intent)
            myContext.finish()

        }

        val buttonBack = binding.resBackIcon
        buttonBack.setOnClickListener {
            myContext.finish()
        }
    }

    override fun onResume() {
        super.onResume()
        //making see cart contents button visible if there are items in the cart
        val items = AppDatabase.getInstance(myContext)!!.getMenuItemDao().getCartContents()
        val isFilled = items.isNotEmpty()
        val viewCartButton = binding.viewCartButton
        viewCartButton.visibility = if(isFilled) View.VISIBLE else View.GONE
        //end of
        val numberOfItems = binding.textNumberOfItems
        numberOfItems.text=items.size.toString()

    }


}