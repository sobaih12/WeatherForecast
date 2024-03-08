package com.example.weatherforecast.model.database

import android.content.Context
import com.example.weatherforecast.model.models.Favorite
import kotlinx.coroutines.flow.Flow

class LocalDataSource private constructor(context: Context):ILocalDataSource {
    companion object{
        private var instance: LocalDataSource? = null
        fun getInstance (context: Context): LocalDataSource {
            return instance ?: synchronized(this){
                val temp = LocalDataSource(context)
                instance = temp
                temp
            }
        }
    }
    private val weatherDao : WeatherDao by lazy {
        WeatherDatabase.getInstance(context).getWeatherDao()
    }
    override fun getAll(): Flow<List<Favorite>> {
        return weatherDao.getAllFavorite()
    }

    override fun insert(favorite: Favorite) {
        weatherDao.insert(favorite)
    }

    override fun delete(favorite: Favorite) {
        weatherDao.delete(favorite)
    }
}