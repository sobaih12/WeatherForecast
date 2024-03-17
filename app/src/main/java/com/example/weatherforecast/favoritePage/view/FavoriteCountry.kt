package com.example.weatherforecast.favoritePage.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.ActivityFavoriteCountryBinding
import com.example.weatherforecast.homePage.view.DailyAdapter
import com.example.weatherforecast.homePage.view.HourlyAdapter
import com.example.weatherforecast.homePage.viewModel.HomeViewModel
import com.example.weatherforecast.homePage.viewModel.HomeViewModelFactory
import com.example.weatherforecast.model.database.LocalDataSource
import com.example.weatherforecast.model.network.RemoteDataSource
import com.example.weatherforecast.model.repository.WeatherRepository
import com.example.weatherforecast.model.utils.ApiState
import com.example.weatherforecast.model.utils.Constants
import com.example.weatherforecast.model.utils.Converter
import com.example.weatherforecast.model.utils.PreferenceManager
import kotlinx.coroutines.launch

class FavoriteCountry : AppCompatActivity() {

    lateinit var binding: ActivityFavoriteCountryBinding
    lateinit var hourlyAdapter: HourlyAdapter
    lateinit var dailyAdapter: DailyAdapter
    lateinit var homeViewModelFactory: HomeViewModelFactory
    val homeViewModel : HomeViewModel by viewModels { homeViewModelFactory }
    val remoteDataSource = RemoteDataSource()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_country)
        binding = ActivityFavoriteCountryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val repository = WeatherRepository.getInstance(localData = LocalDataSource.getInstance(this), remoteData =  remoteDataSource)
        homeViewModelFactory = HomeViewModelFactory(repository)

        hourlyAdapter = HourlyAdapter(this)
        binding.hourlyRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.hourlyRecyclerView.adapter = hourlyAdapter

        dailyAdapter = DailyAdapter(this)
        binding.dailyRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.dailyRecyclerView.adapter = dailyAdapter

        val latitude = intent.getStringExtra("LATITUDE") ?: "32.15"
        val longitude = intent.getStringExtra("LONGITUDE") ?: "23.16"


        val language = PreferenceManager.getLanguage(this)
        val unit = PreferenceManager.getTempUnit(this)

        homeViewModel.getHomeData(latitude,longitude,language.toString(), unit.toString())
        lifecycleScope.launch {
            homeViewModel.responseList.collect{
                when(it){
                    is ApiState.Success ->{
                        val lat = it.weatherItem.lat.toString().toDouble()
                        val lon = it.weatherItem.lon.toString().toDouble()
                        if(PreferenceManager.getLanguage(this@FavoriteCountry)== Constants.LANGUAGE_EN){
                            val address = Converter.getAddressEnglish(this@FavoriteCountry,lat,lon)
                            binding.cityName.text = address
                        }else{
                            val address = Converter.getAddressArabic(this@FavoriteCountry,lat,lon)
                            binding.cityName.text = address
                        }
                        if (PreferenceManager.getTempUnit(this@FavoriteCountry)== Constants.UNITS_CELSIUS){
                            if (PreferenceManager.getLanguage(this@FavoriteCountry)== Constants.LANGUAGE_EN){
                                val currentTemp = Converter.convertToCelsius(it.weatherItem.current?.temp?.toString())
                                val weatherDesc = it.weatherItem.current?.weather?.get(0)?.description
                                binding.tempDesc.text = "$currentTemp | $weatherDesc"
                            }else{
                                val currentTemp = Converter.convertToCelsiusArabic(it.weatherItem.current?.temp?.toString())
                                val weatherDesc = it.weatherItem.current?.weather?.get(0)?.description
                                binding.tempDesc.text = "$currentTemp | $weatherDesc"
                            }
                        }else if(PreferenceManager.getTempUnit(this@FavoriteCountry)== Constants.UNITS_FAHRENHEIT){
                            if (PreferenceManager.getLanguage(this@FavoriteCountry)== Constants.LANGUAGE_EN){
                                val currentTemp = Converter.convertToFahrenheit(it.weatherItem.current?.temp?.toString())
                                val weatherDesc = it.weatherItem.current?.weather?.get(0)?.description
                                binding.tempDesc.text = "$currentTemp | $weatherDesc"
                            }else{
                                val currentTemp = Converter.convertToFahrenheitArabic(it.weatherItem.current?.temp?.toString())
                                val weatherDesc = it.weatherItem.current?.weather?.get(0)?.description
                                binding.tempDesc.text = "$currentTemp | $weatherDesc"
                            }
                        }else{
                            if(PreferenceManager.getLanguage(this@FavoriteCountry)== Constants.LANGUAGE_EN){
                                val currentTemp = Converter.convertToKelvin(it.weatherItem.current?.temp?.toString())
                                val weatherDesc = it.weatherItem.current?.weather?.get(0)?.description
                                binding.tempDesc.text = "$currentTemp | $weatherDesc"
                            }else{
                                val currentTemp = Converter.convertToKelvinArabic(it.weatherItem.current?.temp?.toString())
                                val weatherDesc = it.weatherItem.current?.weather?.get(0)?.description
                                binding.tempDesc.text = "$currentTemp | $weatherDesc"
                            }
                        }
                        binding.humidity.text = it.weatherItem.current?.humidity.toString()+"%"
                        binding.clouds.text = it.weatherItem.current?.clouds.toString()+"%"
                        binding.pressure.text = it.weatherItem.current?.pressure.toString()+"hPa"

                        if (PreferenceManager.getWindUnit(this@FavoriteCountry)== Constants.WIND_SPEED_KILO){
                            if(PreferenceManager.getTempUnit(this@FavoriteCountry)== Constants.UNITS_DEFAULT|| PreferenceManager.getTempUnit(this@FavoriteCountry)== Constants.UNITS_CELSIUS){
                                val windSpeedMps = it.weatherItem.current?.wind_speed
                                val windSpeedKmh = windSpeedMps?.let {
                                    Converter.convertMpsToKmh(it)
                                }
                                binding.windSpeed.text = windSpeedKmh?.toInt().toString()+" k/hr"
                            }else{
                                val windSpeedMps = it.weatherItem.current?.wind_speed
                                val windSpeedKmh = windSpeedMps?.let {
                                    Converter.convertMphToKmh(it)
                                }
                                binding.windSpeed.text = windSpeedKmh?.toInt().toString()+" k/hr"                            }
                        }else{
                            if(PreferenceManager.getTempUnit(this@FavoriteCountry)== Constants.UNITS_DEFAULT|| PreferenceManager.getTempUnit(this@FavoriteCountry)== Constants.UNITS_CELSIUS){
                                val windSpeedMps = it.weatherItem.current?.wind_speed
                                val windSpeedMph = windSpeedMps?.let {
                                    Converter.convertMpsToMph(it)
                                }
                                binding.windSpeed.text = windSpeedMph?.toInt().toString()+" M/hr"
                            }else{
                                val windSpeedMps = it.weatherItem.current?.wind_speed
                                binding.windSpeed.text = windSpeedMps?.toInt().toString()+" M/hr"                            }
                        }


                        switchBetweenHourlyAndDaily(24.0F,18.0F, R.color.white,R.color.grey,
                            View.VISIBLE,
                            View.INVISIBLE)
                        hourlyAdapter.submitList(it.weatherItem.hourly)
                        dailyAdapter.submitList(it.weatherItem.daily)


                        binding.dailyForecast.setOnClickListener {
                            switchBetweenHourlyAndDaily(18.0F,24.0F,R.color.grey,R.color.white,
                                View.INVISIBLE,
                                View.VISIBLE)
                        }
                        binding.hourlyForecast.setOnClickListener {
                            switchBetweenHourlyAndDaily(24.0F,18.0F,R.color.white,R.color.grey,
                                View.VISIBLE,
                                View.INVISIBLE)
                        }
                    }
                    is ApiState.Loading ->{

                    }
                    else ->{
                        Toast.makeText(this@FavoriteCountry, "error is ${it}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    private fun switchBetweenHourlyAndDaily(hourlySize: Float, dailySize:Float, hourlyColor: Int, dailyColor:Int, hourlyVisibility: Int, dailyVisibility:Int) {
        binding.hourlyForecast.textSize = hourlySize
        binding.dailyForecast.textSize = dailySize
        binding.hourlyForecast.setTextColor(ContextCompat.getColor(this, hourlyColor))
        binding.dailyForecast.setTextColor(ContextCompat.getColor(this, dailyColor))
        binding.hourlyRecyclerView.visibility = hourlyVisibility
        binding.dailyRecyclerView.visibility = dailyVisibility
    }
}