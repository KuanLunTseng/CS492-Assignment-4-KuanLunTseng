package com.kuanluntseng.cs_492_weather.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.util.concurrent.Executors

@Database(entities = [FavoriteLocation::class], version = 1)
abstract class FavoriteLocationsRoomDatabase: RoomDatabase() {
    abstract fun favoriteLocationsDao(): FavoriteLocationsDao

    companion object {
        @Volatile private var INSTANCE: FavoriteLocationsRoomDatabase? = null
        fun getInstance(context: Context): FavoriteLocationsRoomDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDataBase(context).also {
                    INSTANCE = it
                }
            }

        private fun buildDataBase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
            FavoriteLocationsRoomDatabase::class.java, "favorite_locations.db")
                .build()

        val executorService = Executors.newFixedThreadPool(4)
    }
}


//@Database(entities = arrayOf(FavoriteLocation::class), version = 1, exportSchema = false)
//abstract class FavoriteLocationsRoomDatabase : RoomDatabase() {
//    abstract fun favoriteLocationsDao(): FavoriteLocationsDao
//
//    companion object {
//        @Volatile
//        private var INSTANCE: FavoriteLocationsRoomDatabase? = null
//        private val NUM_THREADS = 4
//        val executorService = Executors.newFixedThreadPool(NUM_THREADS)
//        fun getDatabase(context: Context): FavoriteLocationsRoomDatabase {
//            return INSTANCE ?: synchronized(FavoriteLocationsRoomDatabase::class.java) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    FavoriteLocationsRoomDatabase::class.java,
//                    "favorite_locations.db"
//                )
//                    .fallbackToDestructiveMigration()
//                    .build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
//}


//@Database(entities = [FavoriteLocation::class], version = 1)
//abstract class FavoriteLocationsRoomDatabase : RoomDatabase() {
//    abstract fun favoriteLocationsDao(): FavoriteLocationsDao
//
//    companion object {
//        @Volatile
//        private var INSTANCE: FavoriteLocationsRoomDatabase? = null
//        private const val NUM_THREADS = 4
//        val executorService = Executors.newFixedThreadPool(NUM_THREADS)
//        fun getDatabase(context: Context): FavoriteLocationsRoomDatabase {
//            if (INSTANCE == null) {
//                synchronized(FavoriteLocationsRoomDatabase::class.java) {
//                    if (INSTANCE == null) {
//                        INSTANCE = Room.databaseBuilder(
//                            context.applicationContext,
//                            FavoriteLocationsRoomDatabase::class.java,
//                            "favorite_locations.db"
//                        ).build()
//                    }
//                }
//            }
//            return INSTANCE!!
//        }
//    }
//}


