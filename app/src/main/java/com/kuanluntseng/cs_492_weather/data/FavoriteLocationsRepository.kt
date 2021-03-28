package com.kuanluntseng.cs_492_weather.data

import android.app.Application

class FavoriteLocationsRepository(application: Application) {
    private var dao: FavoriteLocationsDao
    init {
        val db = FavoriteLocationsRoomDatabase.getInstance(application)
        dao = db.favoriteLocationsDao()
    }

    fun insetFavoriteLocation(favoriteLocation: FavoriteLocation) {
        FavoriteLocationsRoomDatabase.executorService.execute { dao.insert(favoriteLocation) }
    }

    fun deleteFavoriteLocation(favoriteLocation: FavoriteLocation) {
        FavoriteLocationsRoomDatabase.executorService.execute { dao.delete(favoriteLocation) }
    }

    fun getAllFavoriteLocations() = dao.getAllFavoriteLocations()

    fun getFavoriteLocationByName(location : String) = dao.getFavoriteLocationByName(location)
}
