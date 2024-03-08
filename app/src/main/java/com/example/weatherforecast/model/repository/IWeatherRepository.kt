package com.example.weatherForecast.model.repository

import com.example.weatherForecast.model.models.Weather
import com.example.weatherForecast.model.utils.Constants
import kotlinx.coroutines.flow.Flow

interface IWeatherRepository {
    //retrofit
    suspend fun getCurrentWeather(
        lat: String?,
        lon: String?,
        lang: String = Constants.LANGUAGE_EN,
        units: String = Constants.UNITS_DEFAULT
    ): Flow<Weather>
}