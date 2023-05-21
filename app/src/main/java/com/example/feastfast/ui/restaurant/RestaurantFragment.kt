package com.example.feastfast.ui.restaurant

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.feastfast.databinding.FragmentRestaurantBinding
import com.example.feastfast.models.room.AppDatabase
import com.example.feastfast.ui.cart.CartActivity
import com.google.android.material.tabs.TabLayout

class RestaurantFragment : Fragment() {

    lateinit var binding : FragmentRestaurantBinding
    lateinit var myContext: FragmentActivity
    private var tabLayout: TabLayout? = null
    private var viewPager2: ViewPager2? = null
    private var adapter: RestaurantMenuAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRestaurantBinding.inflate(inflater,container,false)
        val view = binding.root
        return  view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //getting current restaurant from activity
        val activity = activity as RestaurantActivity
        val currentRestaurant = activity.getCurrentRestaurant()
        //updating ui based on current restaurant
        binding.apply {
            resImage.setImageResource(currentRestaurant.picture)
            resName.text = currentRestaurant.name
            resCuisine.text=currentRestaurant.cuisineType
            resLogo.setImageResource(currentRestaurant.logo)
            textgps.text=currentRestaurant.locationAddress
            textrating.text=currentRestaurant.averageRating.toString()
        }

        myContext = requireActivity()
        tabLayout =binding.tabLayout
        viewPager2 = binding.viewPager
        val cats = getCategories()
        for (x in cats) {
            tabLayout?.addTab(tabLayout?.newTab()!!.setText(x))
        }
        adapter = RestaurantMenuAdapter(myContext,getCategories(),currentRestaurant)
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

    fun getCategories() : List<String>{
        val data = mutableListOf<String>()
        data.add("pizza")
        data.add("tacos")
        data.add("traditional")
        data.add("Deserts")
        data.add("Pizza")
        data.add("Tacos")
        return data
    }

}