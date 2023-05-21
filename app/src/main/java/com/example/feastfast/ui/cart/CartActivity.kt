package com.example.feastfast.ui.cart

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.feastfast.databinding.ActivityCartBinding
import com.example.feastfast.databinding.ListItemCartBinding
import com.example.feastfast.models.room.AppDatabase
import com.example.feastfast.models.CartItem
import com.example.feastfast.ui.explore.ExploreFragment
import com.example.feastfast.ui.restaurant.RestaurantActivity

class CartActivity : AppCompatActivity() {
    lateinit var binding: ActivityCartBinding
    lateinit var linearLayout: LinearLayout
    lateinit var cartItems: MutableList<CartItem>
    var foodBill = 0.0
    val deliveryBill = 250.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityCartBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        linearLayout = binding.linearLayout2
        val addButton = binding.buttonAddMoreItems
        //getting cart information from room db
        cartItems = AppDatabase.getInstance(this)!!.getMenuItemDao().getCartContents().toMutableList()
        val currentRestaurantName = AppDatabase.getInstance(this)!!.getMenuItemDao().getCurrentRestaurantName().get(0)
        val currentRestaurantId = AppDatabase.getInstance(this)!!.getMenuItemDao().getCurrentRestaurantId().get(0)
        binding.textCartSourceName.text=currentRestaurantName

        //displaying the items that where added to cart start
        displayCartItems()
        //end


        updateUi(foodBill,deliveryBill)

        val emptyCartButton = binding.buttonEmptyCart
        emptyCartButton.setOnClickListener{
            AppDatabase.getInstance(this)!!.getMenuItemDao().emptyCart()
            linearLayout.removeAllViews()
            foodBill=0.0
            updateUi(foodBill,deliveryBill)
            Toast.makeText(this,"cart empty!!" , Toast.LENGTH_SHORT).show()
        }

        addButton.setOnClickListener {
            val currentRestaurant = ExploreFragment().getRestaurantById(currentRestaurantId)
            val intent = Intent(this, RestaurantActivity::class.java)
            intent.putExtra("Restaurant",currentRestaurant)
            this.startActivity(intent)
        }
    }

    fun displayCartItems(){
        for (cartItem in cartItems){
            foodBill+=cartItem.getTotalPrice()
            val itemBinding: ListItemCartBinding = ListItemCartBinding.inflate(layoutInflater)
            itemBinding.imageItem.setImageResource(cartItem.image)
            itemBinding.textNumberOfItems.text="x${cartItem.quantity.toString()}"
            itemBinding.textName.text=cartItem.name
            itemBinding.textPrice.text=cartItem.getTotalPrice().toString()
            itemBinding.textSize.text=cartItem.size
            itemBinding.trashButton.setOnClickListener {
                cartItems.remove(cartItem)
                linearLayout.removeAllViews()
                AppDatabase.getInstance(this)!!.getMenuItemDao().deleteFromCart(cartItem)
                foodBill-=cartItem.getTotalPrice()
                updateUi(foodBill,deliveryBill)
                displayCartItems()
            }
            val newItemView : View = itemBinding.root
            linearLayout.addView(newItemView)
        }
    }

    fun updateUi(foodBill:Double,deliveryBill:Double){
        binding.textDeliveryPrice.text= "$deliveryBill DZD"
        binding.textFoodPrice.text= "$foodBill DZD"
        binding.textTotalBill.text=(deliveryBill+foodBill).toString()+" DZD"
    }

}