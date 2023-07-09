package com.example.feastfast.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.feastfast.R

class AdsViewPagerAdapter (private val Images:List<Int>):RecyclerView.Adapter<AdsViewPagerAdapter.ViewPagerViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdsViewPagerAdapter.ViewPagerViewHolder {
       val view = LayoutInflater.from (parent.context).inflate(R.layout.list_item_home_ad,parent,false)
        return  ViewPagerViewHolder(view)
    }

    inner class ViewPagerViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ImageView : ImageView = itemView.findViewById(R.id.adImage)
    }

    override fun onBindViewHolder(holder: AdsViewPagerAdapter.ViewPagerViewHolder, position: Int) {
        val curImage = Images[position];
        holder.ImageView.setImageResource(curImage)
    }

    override fun getItemCount(): Int {
        return  Images.size
    }


}