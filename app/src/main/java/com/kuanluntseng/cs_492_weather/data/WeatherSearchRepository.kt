package com.kuanluntseng.cs_492_weather.data

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kuanluntseng.cs_492_weather.utils.WeatherUtils
import com.kuanluntseng.myapplication.data.WeatherResults
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherSearchRepository {

    val TAG = WeatherSearchRepository::class.java.simpleName
    val BASE_URL = "https://api.openweathermap.org"

    private var searchResults: MutableLiveData<WeatherResults> = MutableLiveData(null)
    private var loadingStatus: MutableLiveData<LoadingStatus> =
        MutableLiveData(LoadingStatus.SUCCESS)
    private var weatherService: WeatherService
    lateinit var currentQuery: String
    lateinit var currentUnits: String

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        weatherService = retrofit.create(WeatherService::class.java)
    }

    fun getSearchResults(): LiveData<WeatherResults> {
        return searchResults
    }

    fun getLoadingStatus(): LiveData<LoadingStatus> {
        return loadingStatus
    }

    private fun shouldExecuteSearch(query: String, units: String): Boolean {
        return !::currentQuery.isInitialized
                || !::currentUnits.isInitialized
                || !TextUtils.equals(query, currentQuery)
                || !TextUtils.equals(units, currentUnits)
                || loadingStatus.value === LoadingStatus.ERROR
    }

    fun loadSearchResults(query: String, units: String) {
        if (shouldExecuteSearch(query, units)) {
            Log.d(TAG, "Running new search for this query: " + query)
            currentQuery = query
            currentUnits = units
            val queryTerm = WeatherUtils().buildWeatherQueryTerm(query)
            executeSearch(queryTerm, units)
        } else {
            Log.d(TAG, "Using cached search results for this query: " + query)
        }
    }

    fun executeSearch(queryTerm: String, units: String) {
        val results: Call<WeatherResults> = weatherService.searchWeathers(queryTerm, units)

        searchResults.value = null
        loadingStatus.value = LoadingStatus.LOADING

        results.enqueue(object : Callback<WeatherResults> {
            override fun onResponse(
                call: Call<WeatherResults>,
                response: Response<WeatherResults>
            ) {
                if (response.code() == 200) {
                    val weatherResults = response.body()
                    weatherResults?.let {
                        it.location = queryTerm
                        searchResults.value = it
                    }
                    loadingStatus.value = LoadingStatus.SUCCESS
                } else {
                    loadingStatus.value = LoadingStatus.ERROR
                }
            }

            override fun onFailure(call: Call<WeatherResults>, t: Throwable) {
                t.printStackTrace()
                loadingStatus.value = LoadingStatus.ERROR
            }
        })
    }
}