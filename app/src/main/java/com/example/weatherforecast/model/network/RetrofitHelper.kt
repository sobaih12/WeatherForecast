package com.example.weatherForecast.model.network

import com.example.weatherForecast.model.network.RetrofitHelper.retrofit
import com.example.weatherForecast.model.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(Constants.BASE_URL)
        .build()
}
object WeatherAPI {
    val retrofitService: WeatherService by lazy {
        retrofit.create(WeatherService::class.java)
    }
}