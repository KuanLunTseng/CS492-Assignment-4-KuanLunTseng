package com.kuanluntseng.cs_492_weather

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kuanluntseng.cs_492_weather.data.FavoriteLocation

class FavoriteLocationsAdapter(private val onLocationClickListener: OnLocationClickListener) : RecyclerView.Adapter<FavoriteLocationsViewHolder>() {

    private var favoriteLocationsList = emptyList<FavoriteLocation>()

    interface OnLocationClickListener {
        fun onLocationClicked(location: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteLocationsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.favorite_location_item, parent, false)
        val viewHolder = FavoriteLocationsViewHolder(view)
        view.setOnClickListener {
            onLocationClickListener.onLocationClicked(favoriteLocationsList[viewHolder.adapterPosition].location)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: FavoriteLocationsViewHolder, position: Int) {
        val location = favoriteLocationsList[position]
        holder.bind(location.location)
    }

    override fun getItemCount() = favoriteLocationsList.size

    fun updateFavoriteLocations(favoriteLocationsList: List<FavoriteLocation>) {
        favoriteLocationsList?.let {
            this.favoriteLocationsList = it
            notifyDataSetChanged()
        }
    }
}