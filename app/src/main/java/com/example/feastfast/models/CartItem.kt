package com.example.feastfast.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.feastfast.util.Util

@Entity("cartItems")
data class CartItem(
    @PrimaryKey(autoGenerate = true)
    var itemId : Int? = null,
    val name : String,
    val image : Int,
    val price : Double,
    var quantity : Int? = 1,
    var size : String? = "L",
    val specialInstructions : String? = null,
    val restaurantId : Int,
    val restaurantName : String
){
    fun getTotalPrice(): Double{
        return price*quantity!!.toDouble()*Util().sizeToPriceMultiplier(size!!)
    }

}



