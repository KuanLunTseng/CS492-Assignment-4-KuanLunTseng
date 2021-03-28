package com.kuanluntseng.myapplication.data

import com.google.gson.annotations.SerializedName
import com.kuanluntseng.cs_492_weather.data.Weather
import java.io.Serializable

data class WeatherResults(

    var location: String,

    @SerializedName("list")
    val weatherList: List<Weather>
) : Serializable

