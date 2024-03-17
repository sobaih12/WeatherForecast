package com.example.weatherforecast

import com.example.weatherforecast.model.models.Weather
import com.example.weatherforecast.model.network.IRemoteDataSource
import retrofit2.Response

class FakeRemoteDataSource(private val response: Weather):IRemoteDataSource {
    override suspend fun getCurrentWeather(
        lat: String?,
        lon: String?,
        appId: String,
        lang: String?,
        units: String?
    ): Weather {
        return response
    }
}