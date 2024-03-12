package com.example.weatherforecast.model.network

import com.example.weatherforecast.model.models.Weather
import com.example.weatherforecast.model.utils.Constants
import retrofit2.http.Query

interface IRemoteDataSource {
    suspend fun getCurrentWeather(
        @Query("lat") lat: String?,
        @Query("lon") lon: String?,
        @Query("appid") appId: String = Constants.ApiKey,
        @Query("lang") lang: String?,
        @Query("units") units: String?
    ): Weather
}