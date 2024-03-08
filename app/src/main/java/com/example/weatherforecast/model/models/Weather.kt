package com.example.weatherForecast.model.models

data class Weather(
	val current: Current? = null,
	val timezone: String? = null,
	val timezoneOffset: Int? = null,
	val daily: List<DailyItem?>? = null,
	val lon: Any? = null,
	val hourly: List<HourlyItem?>? = null,
	val minutely: List<MinutelyItem?>? = null,
	val lat: Any? = null
)
