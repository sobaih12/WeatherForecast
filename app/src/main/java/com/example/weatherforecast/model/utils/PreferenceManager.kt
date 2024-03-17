package com.example.weatherforecast.model.utils

import android.content.Context

object PreferenceManager {
    private const val PREF_NAME = "Setting"
    private const val KEY_TEMP_UNIT = "temp_unit"
    private const val KEY_LANGUAGE = "app_language"
    private const val KEY_LATITUDE = "latitude"
    private const val KEY_LONGITUDE = "longitude"
    private const val KEY_WIND_SPEED = "wind_speed"

    fun saveTempUnit(context: Context, unit: String) {
        val editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
        editor.putString(KEY_TEMP_UNIT, unit)
        editor.apply()
    }

    fun getTempUnit(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_TEMP_UNIT, Constants.UNITS_CELSIUS)
    }
    fun saveWindUnit(context: Context, unit: String) {
        val editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
        editor.putString(KEY_WIND_SPEED, unit)
        editor.apply()
    }

    fun getWindUnit(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_WIND_SPEED, Constants.WIND_SPEED_KILO)
    }

    fun saveLanguage(context: Context, language: String) {
        val editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
        editor.putString(KEY_LANGUAGE, language)
        editor.apply()
    }

    fun getLanguage(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_LANGUAGE, Constants.LANGUAGE_EN)
    }
    fun saveLatitude(context: Context, latitude: Double) {
        val editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
        editor.putFloat(KEY_LATITUDE, latitude.toFloat())
        editor.apply()
    }

    fun getLatitude(context: Context): Double {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getFloat(KEY_LATITUDE, 0f).toDouble()
    }

    fun saveLongitude(context: Context, longitude: Double) {
        val editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
        editor.putFloat(KEY_LONGITUDE, longitude.toFloat())
        editor.apply()
    }

    fun getLongitude(context: Context): Double {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getFloat(KEY_LONGITUDE, 0f).toDouble()
    }

}
