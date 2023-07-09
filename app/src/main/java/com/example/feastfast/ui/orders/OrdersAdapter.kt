package com.example.feastfast.ui.orders

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.feastfast.R
import com.example.feastfast.databinding.ListItemOrderBinding
import com.example.feastfast.models.Order

class OrdersAdapter(var data : List<Order>, val context : Context, private val activity: AppCompatActivity) : RecyclerView.Adapter<OrdersAdapter.OrderListItemViewHolder>() {






    class OrderListItemViewHolder(val binding: ListItemOrderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderListItemViewHolder {
        return OrderListItemViewHolder(
            ListItemOrderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false)
        )
    }

    override fun getItemCount(): Int = data.size
    override fun onBindViewHolder(holder: OrderListItemViewHolder, position: Int) {
        holder.binding.apply {
            textRestaurantName.text = data[position].restaurantName
            textDate.text= data[position].date
            textLocation.text = data[position].delivery_address
            textOrderStatus.text = data[position].order_status
            textMoney.text = data[position].totalPrice.toString()
            textTime.text = data[position].time
            val rateable = (!data[position].isRated) && (data[position].order_status == "Completed")
            buttonRate.backgroundTintList =  ColorStateList.valueOf( ContextCompat.getColor(context,if(rateable) R.color.pinkish else  R.color.greyish)  )
            buttonRate.setOnClickListener {
                if(rateable){
                    val ratingPopUp = RatingPopUp()
                    val bundle = Bundle()
                    bundle.putInt("userId",data[position].user_id)
                    bundle.putInt("restaurantId",data[position].restaurant_id)
                    ratingPopUp.arguments = bundle
                    ratingPopUp.show(activity.supportFragmentManager,"ratingPopUp")
                }
            }
        }
    }
}