package com.example.feastfast.ui.cart

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.feastfast.databinding.ActivityCartBinding
import com.example.feastfast.databinding.ListItemCartBinding
import com.example.feastfast.models.CartItem
import com.example.feastfast.models.Order
import com.example.feastfast.models.retrofit.Endpoint
import com.example.feastfast.models.room.AppDatabase
import com.example.feastfast.ui.restaurant.RestaurantActivity
import com.example.feastfast.util.url
import kotlinx.coroutines.*
import java.time.LocalDate
import java.time.LocalTime

class CartActivity : AppCompatActivity() {
    lateinit var binding: ActivityCartBinding
    private lateinit var linearLayout: LinearLayout
    private lateinit var cartItems: MutableList<CartItem>
    private lateinit var currentRestaurantName: String
    private var currentRestaurantId : Int = 5
    private var foodBill = 0.0
    private val deliveryBill = 250.0
    val myContext = this
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityCartBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        linearLayout = binding.linearLayout2
        val addButton = binding.buttonAddMoreItems
        //getting cart information from room db
        cartItems = AppDatabase.getInstance(this)!!.getMenuItemDao().getCartContents().toMutableList()
        currentRestaurantName = AppDatabase.getInstance(this)!!.getMenuItemDao().getCurrentRestaurantName()[0]
        currentRestaurantId = AppDatabase.getInstance(this)!!.getMenuItemDao().getCurrentRestaurantId()[0]
        binding.textCartSourceName.text=currentRestaurantName
        //displaying the items that where added to cart start
        displayCartItems()
        //end


        updateUi()

        val emptyCartButton = binding.buttonEmptyCart
        emptyCartButton.setOnClickListener{
            AppDatabase.getInstance(this)!!.getMenuItemDao().emptyCart()
            linearLayout.removeAllViews()
            updateUi()
            Toast.makeText(this,"Cart emptied!" , Toast.LENGTH_SHORT).show()
        }

        addButton.setOnClickListener {

            val exceptionHandler = CoroutineExceptionHandler{ coroutineContext, throwable ->
                this.runOnUiThread {
                    Toast.makeText(myContext, "request successful with Some unspecified error", Toast.LENGTH_SHORT).show()
                }
            }

            CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
                val pref = myContext.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                val idUser = pref.getInt("idUser",0)
                val response = Endpoint.createEndpoint().getUniqueRestaurantById(currentRestaurantId,idUser)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val data = response.body()!!
                        val intent = Intent(myContext, RestaurantActivity::class.java)
                        intent.putExtra("Restaurant", data)
                        myContext.startActivity(intent)
                        myContext.finish()
                    } else {
                        Toast.makeText(myContext, "Request unsuccessful!", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }


        //checking out
        binding.buttonCheckout.setOnClickListener {
            val pref = this.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
            val isLoggedIn= pref.getBoolean("connected",false)
            if (!isLoggedIn){
                val popUp = NotLoggedInPopUp()
                popUp.show(this.supportFragmentManager,"NotLoggedInPopUp")
            }else{
                val currentDate = LocalDate.now().toString()
                val systemTime = LocalTime.now()
                val currentTime = "${systemTime.hour}:${systemTime.minute}"
                val totalPrice = deliveryBill+foodBill
                val pref = this.getSharedPreferences("myPreferences",Context.MODE_PRIVATE)
                val deliveryAddress = pref.getString("address","Enter your location")
                val userId = pref.getInt("idUser",1)
                val orderToSend= Order(userId,currentRestaurantId,currentRestaurantName,"Pending",currentDate,currentTime,deliveryAddress!!,totalPrice.toFloat(),false)

                if (deliveryAddress=="Enter your location"){
                    Toast.makeText(this, "Go back to home page and set delivery  address", Toast.LENGTH_LONG).show()
                }else{
                    val exceptionHandler = CoroutineExceptionHandler{ coroutineContext, throwable ->
                        this.runOnUiThread {
                            val errorMessage = "Error occurred: ${throwable.localizedMessage}"
                            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                        }
                    }

                    CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
                        val response = Endpoint.createEndpoint().sendOrder(orderToSend)
                        withContext(Dispatchers.Main){
                            if (response.isSuccessful && response.body()!=null){
                                val orderThatWasSent = response.body() as Order
                                Toast.makeText(myContext, " Your order from ${orderThatWasSent.restaurantName} has been validated successfully ", Toast.LENGTH_SHORT).show()
                                AppDatabase.getInstance(myContext)!!.getMenuItemDao()!!.emptyCart()
                                myContext.finish()
                            }else{
                                Toast.makeText(myContext, " The request was launched but unsuccessful ", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }


            }
        }
    }

    private fun displayCartItems(){
        for (cartItem in cartItems){
            val itemBinding: ListItemCartBinding = ListItemCartBinding.inflate(layoutInflater)
            Glide.with(myContext).load(url +cartItem.image) . into(itemBinding.imageItem)
            itemBinding.textNumberOfItems.text="x${cartItem.quantity}"
            itemBinding.textName.text=cartItem.name
            itemBinding.textPrice.text=cartItem.getTotalPrice().toString()
            itemBinding.textSize.text=cartItem.size
            itemBinding.trashButton.setOnClickListener {
                linearLayout.removeAllViews()
                AppDatabase.getInstance(this)!!.getMenuItemDao().deleteFromCart(cartItem)
                cartItems = AppDatabase.getInstance(this)!!.getMenuItemDao().getCartContents() as MutableList<CartItem>
                updateUi()
                displayCartItems()
            }
            val newItemView : View = itemBinding.root
            linearLayout.addView(newItemView)
        }
    }

    private fun updateUi(){
        foodBill= cartItems.sumOf { it.getTotalPrice() }
        binding.textDeliveryPrice.text= "$deliveryBill DZD"
        binding.textFoodPrice.text= "$foodBill DZD"
        binding.textTotalBill.text=(deliveryBill+foodBill).toString()+" DZD"
        }

}