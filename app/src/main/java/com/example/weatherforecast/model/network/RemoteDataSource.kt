package com.example.weatherforecast.model.network

import com.example.weatherforecast.model.models.Weather


class RemoteDataSource:IRemoteDataSource {
    override suspend fun getCurrentWeather(
        lat: String?,
        lon: String?,
        appId: String,
        lang: String,
        units: String
    ): Weather {
        return WeatherAPI.retrofitService.getCurrentWeather(lat,lon, appId, lang, units)
    }
}