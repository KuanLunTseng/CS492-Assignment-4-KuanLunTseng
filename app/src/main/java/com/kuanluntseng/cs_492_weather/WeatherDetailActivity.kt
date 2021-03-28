package com.kuanluntseng.cs_492_weather

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.kuanluntseng.cs_492_weather.data.FavoriteLocation
import com.kuanluntseng.cs_492_weather.data.Weather
import java.text.SimpleDateFormat
import java.util.*

class WeatherDetailActivity : AppCompatActivity() {
    companion object {
        val EXTRA_WEATHER = "OpenWeather"
        val EXTRA_WEATHER_LOCATION = "Location"
    }

    lateinit var weather: Weather
    private var isFavorited: Boolean = false
    private lateinit var viewModel: FavoriteLocationsViewModel
    private lateinit var location: String
    private lateinit var dateText: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.weather_detail)

        intent?.run {
            if (hasExtra(EXTRA_WEATHER)) {
                weather = getSerializableExtra(EXTRA_WEATHER) as Weather
                getStringExtra(EXTRA_WEATHER_LOCATION)?.let { location = it }

                val locationTextView: TextView = findViewById(R.id.location_textview)
                val dateTextView: TextView = findViewById(R.id.date_textview)
                val tempLowTextView: TextView = findViewById(R.id.temp_low_textview)
                val tempHighTextView: TextView = findViewById(R.id.temp_high_textview)
                val popTextView: TextView = findViewById(R.id.pop_textview)
                val cloudTextView: TextView = findViewById(R.id.cloud_textview)
                val windTextView: TextView = findViewById(R.id.wind_textview)
                val descriptionTextView: TextView = findViewById(R.id.description_textview)
                val windDirectionImageView: ImageView = findViewById(R.id.wind_direction_imageview)

                val date = SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse(weather.dtText)
                dateText = SimpleDateFormat("MMMM dd, hh:mm aa").format(date)

                viewModel = ViewModelProvider(
                    this@WeatherDetailActivity,
                    ViewModelProvider.AndroidViewModelFactory(application)
                ).get(FavoriteLocationsViewModel::class.java)

                locationTextView.text = location
                dateTextView.text = dateText
                tempLowTextView.text = "Low: ${weather.main.tempMin.toString()}"
                tempHighTextView.text = "Low: ${weather.main.tempMax.toString()}"
                popTextView.text = "Precip: ${weather.pop.toString()}"
                descriptionTextView.text = weather.data[0].description
                cloudTextView.text = "Cloud: ${weather.clouds.all.toString()}"
                windTextView.text = "Wind Speed: ${weather.wind.speed.toString()}"
                windDirectionImageView.rotation = weather.wind.deg
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.weather_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share -> {
                shareWeather()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun shareWeather() {
        weather?.let {
            val shareText = getString(
                R.string.share_weather_text,
                weather.dtText,
                weather.main.tempMax,
                weather.main.tempMin,
                weather.data[0].description
            )
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, shareText)
            intent.setType("text/plain")
            val chooserIntent = Intent.createChooser(intent, null)
            startActivity(chooserIntent)
        }
    }
}