package com.example.feastfast.ui.account

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.feastfast.databinding.ListItemRestaurantBinding
import com.example.feastfast.models.Restaurant
import com.example.feastfast.ui.restaurant.RestaurantActivity
import com.example.feastfast.util.url

class RestaurantAdapter(val data : List<Restaurant> , val context : Context) : RecyclerView.Adapter<RestaurantAdapter.RestaurantListItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantListItemViewHolder {
        return RestaurantAdapter.RestaurantListItemViewHolder(
            ListItemRestaurantBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: RestaurantListItemViewHolder, position: Int) {


        holder.binding.apply {
             Glide.with(context).load(url+data[position].logo) . into(imageLogo)
             Glide.with(context).load(url+data[position].picture).into(imageRestaurant)
            textRating.text = data[position].averageRating.toString()
            textCuisine.text =  data[position].cuisineType
            val pref = context.getSharedPreferences("myPrefrences", Context.MODE_PRIVATE)
            val con = pref.getBoolean("connected", false);
            if (con)   textRestaurantName.text = data[position].name
            textAddress.text = data[position].locationAddress
            textRestaurantName.text = data[position].name
            viewCardContent.setOnClickListener {
                val intent = Intent(context , RestaurantActivity::class.java)
                intent.putExtra("Restaurant", data[position]) // Pass the Restaurant object as an extra
                context .startActivity(intent)

            }

        }
    }


    class RestaurantListItemViewHolder(val binding: ListItemRestaurantBinding) : RecyclerView.ViewHolder(binding.root)
}