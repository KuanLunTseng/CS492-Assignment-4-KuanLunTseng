package com.kuanluntseng.cs_492_weather

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.kuanluntseng.cs_492_weather.data.FavoriteLocation
import com.kuanluntseng.cs_492_weather.data.FavoriteLocationsRepository

class FavoriteLocationsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository : FavoriteLocationsRepository = FavoriteLocationsRepository(application)

    fun insertFavoriteLocation(favoriteLocation: FavoriteLocation) {
        repository.insetFavoriteLocation(favoriteLocation)
    }

    fun deleteFavoriteLocation(favoriteLocation: FavoriteLocation) {
        repository.deleteFavoriteLocation(favoriteLocation)
    }

    fun getAllFavoriteLocations() = repository.getAllFavoriteLocations()

    fun getFavoriteLocationByName(location: String) = repository.getFavoriteLocationByName(location)
}