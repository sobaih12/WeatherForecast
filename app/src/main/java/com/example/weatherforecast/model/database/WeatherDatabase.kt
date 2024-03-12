package com.example.weatherforecast.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherforecast.model.models.Favorite

@Database(entities = arrayOf(Favorite::class), version = 2 )
abstract class WeatherDatabase: RoomDatabase() {
    abstract fun getWeatherDao():WeatherDao
    companion object{
        @Volatile
        private var INSTANCE :WeatherDatabase? = null
        fun getInstance(context: Context):WeatherDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext,
                    WeatherDatabase::class.java,
                    "weather_table").build()
                INSTANCE = instance
                instance
            }
        }
    }
}
