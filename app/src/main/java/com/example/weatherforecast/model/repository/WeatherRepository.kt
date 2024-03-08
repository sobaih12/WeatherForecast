package com.example.weatherForecast.model.repository

import com.example.weatherForecast.model.database.ILocalDataSource
import com.example.weatherForecast.model.models.Weather
import com.example.weatherForecast.model.network.IRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRepository private constructor(
    var localData: ILocalDataSource,
    var remoteData: IRemoteDataSource
):IWeatherRepository {

    companion object{
        @Volatile
        private var INSTANCE: WeatherRepository? = null
        fun getInstance (localData: ILocalDataSource, remoteData: IRemoteDataSource): WeatherRepository{
            return INSTANCE ?: synchronized(this) {
                val instance =WeatherRepository(localData,remoteData)
                INSTANCE = instance
                instance }
        }
    }
    override suspend fun getCurrentWeather(
        lat: String?,
        lon: String?,
        lang: String,
        units: String
    ): Flow<Weather> = flow{
         emit(remoteData.getCurrentWeather(lat = lat, lon =lon, lang =lang, units = units))
    }
}