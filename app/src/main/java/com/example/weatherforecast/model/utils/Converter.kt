package com.example.weatherforecast.model.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object Converter {
    fun getAddressEnglish(context: Context, lat: Double, lon: Double):String{
        var address:MutableList<Address>?
        val geocoder= Geocoder(context)
        address =geocoder.getFromLocation(lat,lon,1)
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

    fun getAddressArabic(context: Context,lat:Double?,lon:Double?):String{
        var address:MutableList<Address>?
        val geocoder= Geocoder(context, Locale("ar"))
        address =geocoder.getFromLocation(lat!!,lon!!,1)
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

    fun convertToCelsius(temp: Any?): String {
        val tempString: String? = temp?.toString()
        val roundedTemp: Int? = tempString?.toDouble()?.toInt()
        return roundedTemp?.toString() + "°C" ?: "N/A"
    }

    fun convertToFahrenheit(temp: Any?): String {
        val tempString: String? = temp?.toString()
        val celsiusTemp: Double? = tempString?.toDouble()
        val fahrenheitTemp: Double? = celsiusTemp?.times(9.0 / 5.0)?.plus(32)
        return fahrenheitTemp?.toInt()?.toString() + "°F" ?: "N/A"
    }

    fun convertToKelvin(temp: Any?): String {
        val tempString: String? = temp?.toString()
        val celsiusTemp: Double? = tempString?.toDouble()
        val kelvinTemp: Double? = celsiusTemp?.plus(273.15)
        return kelvinTemp?.toInt()?.toString() + "K" ?: "N/A"
    }

    fun convertToCelsiusArabic(temp: Any?): String {
        val tempString: String? = temp?.toString()
        val roundedTemp: Int? = tempString?.toDouble()?.toInt()
        return convertNumberToArabic(roundedTemp?.toString() ?: "N/A") + "°م"
    }

    fun convertToFahrenheitArabic(temp: Any?): String {
        val tempString: String? = temp?.toString()
        val roundedTemp: Int? = tempString?.toDouble()?.toInt()
        return convertNumberToArabic(roundedTemp?.toString() ?: "N/A") + "°ف"
    }

    fun convertToKelvinArabic(temp: Any?): String {
        val tempString: String? = temp?.toString()
        val roundedTemp: Int? = tempString?.toDouble()?.toInt()
        return convertNumberToArabic(roundedTemp?.toString() ?: "N/A") + "°ك"
    }

    fun convertNumberToArabic(number: String): String {
        val arabicNumbers = arrayOf("٠", "١", "٢", "٣", "٤", "٥", "٦", "٧", "٨", "٩")
        val result = StringBuilder()
        for (char in number) {
            if (char.isDigit()) {
                result.append(arabicNumbers[char.toString().toInt()])
            } else {
                result.append(char)
            }
        }
        return result.toString()
    }

    fun convertUnixTimeToDay(unixTime: Int): String {
        val date = Date(unixTime * 1000L)
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(date)
    }

    fun convertUnixTimeToArabicDay(unixTime: Int): String {
        val date = Date(unixTime * 1000L)
        val sdf = SimpleDateFormat("EEEE", Locale("ar"))
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(date)
    }
    fun convertUnixTimeToHour(unixTimestamp: Long?): String {
        val sdf = SimpleDateFormat("h a", Locale.getDefault())
        val date = Date(unixTimestamp?.times(1000) ?: 0)
        return sdf.format(date)
    }
    fun convertUnixTimeToHourArabic(unixTimestamp: Long?): String {
        val sdf = SimpleDateFormat("h a", Locale.getDefault())
        sdf.applyPattern("h a")
        val date = Date(unixTimestamp?.times(1000) ?: 0) // Convert seconds to milliseconds
        val hour = sdf.format(date)
        val arabicNumbers = arrayOf("٠", "١", "٢", "٣", "٤", "٥", "٦", "٧", "٨", "٩")
        val result = StringBuilder()
        for (char in hour) {
            if (char.isDigit()) {
                result.append(arabicNumbers[char.toString().toInt()])
            } else if (char == 'A') {
                result.append("ص")
            } else if (char == 'P') {
                result.append("مً")
            }
        }
        return result.toString()
    }

    fun convertMpsToKmh(speedMps: Double?): Double {
        return if (speedMps == null) {
            0.0
        } else {
            speedMps * 3.6
        }
    }
    fun convertMpsToMph(speedMps: Double?): Double {
        return if (speedMps == null) {
            0.0
        } else {
            speedMps * 2.23694
        }
    }

    fun convertMphToKmh(speedMph: Double?): Double {
        return speedMph?.times(1.60934) ?: 0.0
    }


}