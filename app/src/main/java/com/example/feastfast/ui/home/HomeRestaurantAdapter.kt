package com.example.feastfast.ui.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.feastfast.databinding.ListItemHomeRestaurantBinding
import com.example.feastfast.models.Restaurant
import com.example.feastfast.ui.restaurant.RestaurantActivity
import com.example.feastfast.util.url

class HomeRestaurantAdapter(val data : List<Restaurant>, val context : Context) : RecyclerView.Adapter<HomeRestaurantAdapter.HomeRestaurantListItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeRestaurantListItemViewHolder {
        return HomeRestaurantListItemViewHolder(
            ListItemHomeRestaurantBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: HomeRestaurantListItemViewHolder, position: Int) {
        holder.binding.apply {

            Glide.with(context).load(url +data[position].logo) . into(imageLogo)
            Glide.with(context).load(url +data[position].picture).into(imageRestaurant)
            textRating.text = data[position].averageRating.toString()
            textCuisine.text =  data[position].cuisineType
            textAddress.text = data[position].locationAddress
            textRestaurantName.text = data[position].name
            viewCardContent.setOnClickListener {
                    val intent = Intent(context , RestaurantActivity::class.java)
                    intent.putExtra("Restaurant",data[position])
                    context.startActivity(intent)
            }
            if (position==data.size-1){
                val params =root.layoutParams as ViewGroup.MarginLayoutParams
                params.setMargins(0,0,50,0)
            }

        }
    }


    class HomeRestaurantListItemViewHolder(val binding: ListItemHomeRestaurantBinding) : RecyclerView.ViewHolder(binding.root)
}