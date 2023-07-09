package com.example.feastfast.ui.restaurant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.example.feastfast.R
import com.example.feastfast.databinding.FragmentMenuItemDetailsBinding
import com.example.feastfast.models.CartItem
import com.example.feastfast.models.MenuItem
import com.example.feastfast.models.room.AppDatabase
import com.example.feastfast.util.url
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class MenuItemDetailsFragment : BottomSheetDialogFragment() {

    lateinit var binding : FragmentMenuItemDetailsBinding
    lateinit var myContext : FragmentActivity
    private lateinit var radioGroup : RadioGroup
    lateinit var cartItem : CartItem
    lateinit var addToCartButton : Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuItemDetailsBinding.inflate(inflater,container,false)
        val view = binding.root
        val behaviour = BottomSheetBehavior.from(binding.layoutRoot)
        behaviour.isHideable = false
        behaviour.isDraggable=false
        behaviour.state = BottomSheetBehavior.STATE_EXPANDED
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuItem = arguments?.getSerializable("menuItemss") as MenuItem
        cartItem = menuItem.menuItemToCartItem(menuItem)
        myContext = requireActivity()


        //updating ui based on menu item
        Glide.with(myContext).load(url +menuItem.image) . into(binding.imageView)
        binding.textDescription.text = menuItem.description
        binding.textMenuItem.text = menuItem.name



        //dish quantity logic start
        radioGroup= binding.radioGroup
        val increment = binding.buttonPlus
        val decrement = binding.buttonMinus
        val textAmmount = binding.textAmmount
        addToCartButton = binding.buttonAddToCart
        increment.setOnClickListener {
            cartItem.quantity = cartItem.quantity!! + 1
            textAmmount.text=cartItem.quantity.toString()
            addToCartButton.text = "Add to cart- DZD"+cartItem.getTotalPrice().toString()
        }
        decrement.setOnClickListener {
            cartItem.quantity = cartItem.quantity!! - 1
            textAmmount.text=cartItem.quantity.toString()
            addToCartButton.text = "Add to cart- DZD"+cartItem.getTotalPrice().toString()
        }
        //dish quantity logic end

        radioGroup.setOnCheckedChangeListener(object: RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                val checked = when (checkedId) {
                    R.id.radio_L -> "L"
                    R.id.radio_XL -> "XL"
                    R.id.radio_XXL -> "XXL"
                    else -> "L"
                }
                cartItem.size=checked
                addToCartButton.text = "Add to cart- DZD"+cartItem.getTotalPrice().toString()

            }
        })

        binding.buttonLeave.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.action_menuItemDetailsFragment_to_restaurantFragment)
        }
        binding.buttonAddToCart.setOnClickListener {
            //checking that the cart doesn't contain items from another restaurant
            val currentRestaurantId = cartItem.restaurantId
            val currentRestaurantName = cartItem.restaurantName
            val restaurantsInCart = AppDatabase.getInstance(myContext)!!.getMenuItemDao().getCurrentRestaurantId()
            val restaurantNamesInCart = AppDatabase.getInstance(myContext)!!.getMenuItemDao().getCurrentRestaurantName()
            if((currentRestaurantId in restaurantsInCart && currentRestaurantName in restaurantNamesInCart) || restaurantsInCart.isEmpty() ){
                AppDatabase.getInstance(myContext)!!.getMenuItemDao().addToCart((cartItem))
                Toast.makeText(myContext,"Items added to cart!" , Toast.LENGTH_SHORT).show()
                NavHostFragment.findNavController(this).navigate(R.id.action_menuItemDetailsFragment_to_restaurantFragment)
            }else{
                Toast.makeText(myContext,"Cannot add items from 2 different restaurants!" , Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        disableBottomSheetDraggableBehavior()
    }

    private fun disableBottomSheetDraggableBehavior() {
        this.isCancelable = false
        this.dialog?.setCanceledOnTouchOutside(true)
    }
}