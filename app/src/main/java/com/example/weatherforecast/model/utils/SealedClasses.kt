package com.example.weatherforecast.model.utils

import com.example.weatherforecast.model.models.Weather


sealed class ApiState{
    class Success(var weatherItem: Weather):ApiState()
    class Fail(val throwable: Throwable):ApiState()
    object Loading :ApiState()
}