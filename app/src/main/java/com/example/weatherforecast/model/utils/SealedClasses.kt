package com.example.weatherForecast.model.utils

import com.example.weatherForecast.model.models.Weather


sealed class ApiState{
    class Success(var weatherItem: Weather):ApiState()
    class Fail(val throwable: Throwable):ApiState()
    object Loading :ApiState()
}