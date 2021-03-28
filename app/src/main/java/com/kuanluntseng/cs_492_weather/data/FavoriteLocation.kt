package com.kuanluntseng.cs_492_weather.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoriteLocations")
data class FavoriteLocation (

    @PrimaryKey
    @ColumnInfo(name = "location")
    val location: String,

    @ColumnInfo(name = "date")
    val date : String
)