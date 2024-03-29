package com.example.weatherforecast.model.models

data class Current(
	val sunrise: Int? = null,
	val temp: Any? = null,
	val visibility: Int? = null,
	val uvi: Double? = null,
	val pressure: Int? = null,
	val clouds: Int? = null,
	val feelsLike: Any? = null,
	val dt: Int? = null,
	val windDeg: Int? = null,
	val dewPoint: Any? = null,
	val sunset: Int? = null,
	val weather: List<WeatherItem?>? = null,
	val humidity: Int? = null,
	val wind_speed: Double? = null
)
