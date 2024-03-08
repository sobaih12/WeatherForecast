package com.example.weatherForecast.model.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object Converter {
    fun getAddressEnglish(context: Context, lat: Double?, lon: Double?):String{
        var address:MutableList<Address>?
        val geocoder= Geocoder(context)
        address =geocoder.getFromLocation(lat!!,lon!!,1)
        if (address?.isEmpty()==true) {
            return "Unknown location"
        } else if (address?.get(0)?.countryName.isNullOrEmpty()) {
            return "Unknown Country"
        } else if (address?.get(0)?.adminArea.isNullOrEmpty()) {
            return address?.get(0)?.countryName.toString()
        } else{
            var result = address?.get(0)?.adminArea.toString()+", "+address?.get(0)?.countryName
            result = result.replace(" Governorate", "")
            return result
        }
    }

    fun getAddressArabic(context: Context,lat:Double,lon:Double):String{
        var address:MutableList<Address>?
        val geocoder= Geocoder(context, Locale("ar"))
        address =geocoder.getFromLocation(lat,lon,1)
        if (address?.isEmpty()==true) {
            return "Unknown location"
        } else if (address?.get(0)?.countryName.isNullOrEmpty()) {
            return "Unknown Country"
        } else if (address?.get(0)?.adminArea.isNullOrEmpty()) {
            return address?.get(0)?.countryName.toString()
        } else{
            return address?.get(0)?.countryName.toString()+"، "+address?.get(0)?.adminArea
        }
    }

    fun convertTemperatureToString(temp: Any?): String {
        val tempString: String? = temp?.toString()
        val roundedTemp: Int? = tempString?.toDouble()?.toInt()
        return roundedTemp?.toString()+"°C" ?: "N/A"
    }

    fun convertUnixTimeToDay(unixTime: Int): String {
        val date = Date(unixTime * 1000L)
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(date)
    }
    fun convertUnixTimeToHour(unixTimestamp: Long?): String {
        val sdf = SimpleDateFormat("h a", Locale.getDefault())
        val date = Date(unixTimestamp?.times(1000) ?: 0) // Convert seconds to milliseconds
        return sdf.format(date)
    }

}