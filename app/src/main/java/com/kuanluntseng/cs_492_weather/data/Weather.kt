package com.kuanluntseng.cs_492_weather.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Weather(
    @SerializedName("dt_txt")
    val dtText: String,
    val main: Main,
    @SerializedName("weather")
    val data: List<Data>,
    val pop: Double,
    val clouds: Clouds,
    val wind: Wind
) : Serializable

data class Clouds(
    val all: Int
) : Serializable

data class Wind(
    val speed: Float,
    val deg: Float
) : Serializable

data class Data(
    val description: String,
    val icon: String
) : Serializable

data class Main(
    @SerializedName("temp_min")
    val tempMin: Double,
    @SerializedName("temp_max")
    val tempMax: Double
) : Serializable