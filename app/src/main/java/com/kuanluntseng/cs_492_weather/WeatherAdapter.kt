package com.kuanluntseng.cs_492_weather

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kuanluntseng.cs_492_weather.data.Weather
import com.kuanluntseng.myapplication.data.WeatherResults

class WeatherAdapter(
    private val onSearchClickListener: OnSearchClickListener
) : RecyclerView.Adapter<WeatherViewHolder>() {

    private var weatherList: List<Weather> = emptyList()
    private var location = ""
    private var units = ""

    interface OnSearchClickListener {
        fun onSearchClicked(location: String, weather: Weather)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_item, parent, false)
        val weatherViewHolder = WeatherViewHolder(view)
        view.setOnClickListener {
            onSearchClickListener.onSearchClicked(location, weatherList[weatherViewHolder.adapterPosition])
        }
        return weatherViewHolder
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weather = weatherList[position]
        holder.bind(weather, units)
    }

    override fun getItemCount() = weatherList.size

    fun updateSearchResults(searchResult: WeatherResults?, units: String) {
        searchResult?.let {
            weatherList = it.weatherList
            location = it.location
            this.units = units
            notifyDataSetChanged()
        }
    }
}