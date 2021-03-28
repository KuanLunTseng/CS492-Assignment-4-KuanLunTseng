package com.kuanluntseng.cs_492_weather

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kuanluntseng.cs_492_weather.data.Weather
import java.text.SimpleDateFormat
import java.util.*

class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val FIVE_DAY_FORECAST_ICON_URL_FORMAT_STR =
        "https://openweathermap.org/img/wn/%s@4x.png"

    val dateTextTextView: TextView = itemView.findViewById(R.id.date_textview)
    val tempMinTextView: TextView = itemView.findViewById(R.id.temp_min_textview)
    val tempMaxTextView: TextView = itemView.findViewById(R.id.temp_max_textview)
    val popTextView: TextView = itemView.findViewById(R.id.pop_textview)
    val iconImageView: ImageView = itemView.findViewById(R.id.icon_imageview)

    fun bind(weather: Weather, units: String) {
        var symbol = when (units) {
            "standard" -> "°K"
            "imperial" -> "℉"
            else -> "℃"
        }

        val date: Date = SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse(weather.dtText)
        val dateText = SimpleDateFormat("MMMM dd, hh:mm aa").format(date)
        dateTextTextView.text = dateText
        tempMinTextView.text = weather.main.tempMin.toString() + symbol
        tempMaxTextView.text = weather.main.tempMax.toString() + symbol
        popTextView.text = String.format("%.2f%%", (weather.pop * 100))
        Glide.with(itemView.context)
            .load(String.format(FIVE_DAY_FORECAST_ICON_URL_FORMAT_STR, weather.data[0].icon))
            .into(iconImageView)
    }
}