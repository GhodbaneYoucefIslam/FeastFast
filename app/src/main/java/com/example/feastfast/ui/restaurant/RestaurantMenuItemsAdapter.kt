package com.example.feastfast.ui.restaurant

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.feastfast.R
import com.example.feastfast.databinding.ListItemMenuBinding
import com.example.feastfast.models.MenuItem
import com.example.feastfast.util.url


class RestaurantMenuItemsAdapter(val data: List<MenuItem>?, val context: Context, val name :String?="kook") :
    RecyclerView.Adapter<RestaurantMenuItemsAdapter.MenuItemViewHolder>() {

    class MenuItemViewHolder(val binding: ListItemMenuBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuItemViewHolder {
        return MenuItemViewHolder(
            ListItemMenuBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    override fun onBindViewHolder(holder: MenuItemViewHolder, position: Int) {
        holder.binding.apply {

        textSize.text=data!![position].price.toString()
        textPrice.text=data!![position].description
        Glide.with(context).load(url +data!![position].image) . into(imageItem)

        textName.text = data!![position].name
        cardMenuItem.setOnClickListener {
                val data = bundleOf("menuItemss" to data[position])
            it.findNavController().navigate(R.id.action_restaurantFragment_to_menuItemDetailsFragment,data)
            }
        }
    }
}