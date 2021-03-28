package com.kuanluntseng.cs_492_weather.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteLocationsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favoriteLocation: FavoriteLocation)

    @Delete
    fun delete(favoriteLocation: FavoriteLocation)

    @Query("SELECT * FROM favoriteLocations")
    fun getAllFavoriteLocations(): LiveData<List<FavoriteLocation>>

    @Query("SELECT * FROM favoriteLocations WHERE location = :location LIMIT 1")
    fun getFavoriteLocationByName(location: String): LiveData<FavoriteLocation>
}