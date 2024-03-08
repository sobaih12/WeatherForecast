package com.example.weatherforecast.model.repository


import com.example.weatherforecast.model.models.Favorite
import com.example.weatherforecast.model.models.Weather
import com.example.weatherforecast.model.utils.Constants
import kotlinx.coroutines.flow.Flow

interface IWeatherRepository {
    //room
    fun getAllFavorite(): Flow<List<Favorite>>
    fun insertFavorite(favorite: Favorite)
    fun deleteFavorite(favorite: Favorite)


    //retrofit
    suspend fun getCurrentWeather(
        lat: String?,
        lon: String?,
        lang: String = Constants.LANGUAGE_EN,
        units: String = Constants.UNITS_DEFAULT
    ): Flow<Weather>
}