package com.kuanluntseng.cs_492_weather

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FavoriteLocationsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val favoriteLocationTextview : TextView = itemView.findViewById(R.id.favorite_location_textview)

    fun bind(location: String) {
        favoriteLocationTextview.text = location
    }
}