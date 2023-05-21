package com.example.feastfast.models

data class MenuItem(
    val name : String,
    val image : Int,
    val price : Double,
    val description: String,
    val restaurantId : Int,
    val restaurantName : String
):java.io.Serializable{
    fun menuItemToCartItem(menuItem: MenuItem): CartItem{
        return CartItem(name = menuItem.name, image = menuItem.image, price = menuItem.price, restaurantId = menuItem.restaurantId, restaurantName = menuItem.restaurantName)
    }
}
