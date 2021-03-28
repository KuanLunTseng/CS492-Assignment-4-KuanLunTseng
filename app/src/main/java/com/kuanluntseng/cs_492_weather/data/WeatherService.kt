package com.kuanluntseng.cs_492_weather.data

import com.kuanluntseng.myapplication.data.WeatherResults
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call

interface WeatherService {
    @GET("data/2.5/forecast?appid=ce18d7c424d3b79ce710493560f99e17")
    fun searchWeathers(
        @Query("q")
        query: String,
        @Query("units")
        units: String
    ): Call<WeatherResults>
}