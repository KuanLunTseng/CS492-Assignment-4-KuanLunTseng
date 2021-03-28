package com.kuanluntseng.cs_492_weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.kuanluntseng.cs_492_weather.data.LoadingStatus
import com.kuanluntseng.cs_492_weather.data.WeatherSearchRepository
import com.kuanluntseng.myapplication.data.WeatherResults

class WeatherViewModel() : ViewModel() {
    private var searchResults : LiveData<WeatherResults>
    private var loadingStatus: LiveData<LoadingStatus>
    private var weatherSearchRepository: WeatherSearchRepository = WeatherSearchRepository()

    init {
        searchResults = weatherSearchRepository.getSearchResults()
        loadingStatus = weatherSearchRepository.getLoadingStatus()
    }

    fun loadSearchResults(query: String, units: String) {
        weatherSearchRepository.loadSearchResults(query, units)
    }

    fun getSearchResults(): LiveData<WeatherResults> {
        return searchResults
    }

    fun getLoadingStatus(): LiveData<LoadingStatus> {
        return loadingStatus
    }
}