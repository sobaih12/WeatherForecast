package com.example.weatherforecast.homePage.view;

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.ActivityHomeBinding
import com.example.weatherforecast.favoritePage.view.FavoriteActivity
import com.example.weatherforecast.homePage.viewModel.HomeViewModel
import com.example.weatherforecast.homePage.viewModel.HomeViewModelFactory
import com.example.weatherforecast.model.database.LocalDataSource
import com.example.weatherforecast.model.models.Location
import com.example.weatherforecast.model.network.RemoteDataSource
import com.example.weatherforecast.model.repository.WeatherRepository
import com.example.weatherforecast.model.utils.ApiState
import com.example.weatherforecast.model.utils.Constants
import com.example.weatherforecast.model.utils.Converter
import com.example.weatherforecast.model.utils.PreferenceManager
import com.example.weatherforecast.settingPage.SettingActivity
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    lateinit var hourlyAdapter: HourlyAdapter
    lateinit var dailyAdapter: DailyAdapter
    lateinit var homeViewModelFactory: HomeViewModelFactory
    val homeViewModel : HomeViewModel by viewModels { homeViewModelFactory }
    val remoteDataSource = RemoteDataSource()
    private var initialY = 0f
    private var initialHeight = 0
    private val minHeightInDp = 324
    private var isChanged = false


    override fun onResume() {
        super.onResume()

        val language = PreferenceManager.getLanguage(this)
        val unit = PreferenceManager.getTempUnit(this)
        val longitude = PreferenceManager.getLongitude(this)
        val latitude = PreferenceManager.getLatitude(this)

        homeViewModel.getHomeData(latitude.toString(),longitude.toString(),language.toString(), unit.toString())
        lifecycleScope.launch {
            homeViewModel.responseList.collect{
                when(it){
                    is ApiState.Success ->{
                        if(PreferenceManager.getLanguage(this@HomeActivity)==Constants.LANGUAGE_EN){
                            val address = Converter.getAddressEnglish(this@HomeActivity,latitude,longitude)
                            binding.cityName.text = address
                        }else{
                            val address = Converter.getAddressArabic(this@HomeActivity,latitude,longitude)
                            binding.cityName.text = address
                        }
                        if (PreferenceManager.getTempUnit(this@HomeActivity)==Constants.UNITS_CELSIUS){
                            if (PreferenceManager.getLanguage(this@HomeActivity)==Constants.LANGUAGE_EN){
                                val currentTemp = Converter.convertToCelsius(it.weatherItem.current?.temp?.toString())
                                val weatherDesc = it.weatherItem.current?.weather?.get(0)?.description
                                binding.tempDesc.text = "$currentTemp | $weatherDesc"
                            }else{
                                val currentTemp = Converter.convertToCelsiusArabic(it.weatherItem.current?.temp?.toString())
                                val weatherDesc = it.weatherItem.current?.weather?.get(0)?.description
                                binding.tempDesc.text = "$currentTemp | $weatherDesc"
                            }
                        }else if(PreferenceManager.getTempUnit(this@HomeActivity)==Constants.UNITS_FAHRENHEIT){
                            if (PreferenceManager.getLanguage(this@HomeActivity)==Constants.LANGUAGE_EN){
                                val currentTemp = Converter.convertToFahrenheit(it.weatherItem.current?.temp?.toString())
                                val weatherDesc = it.weatherItem.current?.weather?.get(0)?.description
                                binding.tempDesc.text = "$currentTemp | $weatherDesc"
                            }else{
                                val currentTemp = Converter.convertToFahrenheitArabic(it.weatherItem.current?.temp?.toString())
                                val weatherDesc = it.weatherItem.current?.weather?.get(0)?.description
                                binding.tempDesc.text = "$currentTemp | $weatherDesc"
                            }
                        }else{
                            if(PreferenceManager.getLanguage(this@HomeActivity)==Constants.LANGUAGE_EN){
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

                        if (PreferenceManager.getWindUnit(this@HomeActivity)==Constants.WIND_SPEED_KILO){
                            if(PreferenceManager.getTempUnit(this@HomeActivity)==Constants.UNITS_DEFAULT||PreferenceManager.getTempUnit(this@HomeActivity)==Constants.UNITS_CELSIUS){
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
                            if(PreferenceManager.getTempUnit(this@HomeActivity)==Constants.UNITS_DEFAULT||PreferenceManager.getTempUnit(this@HomeActivity)==Constants.UNITS_CELSIUS){
                                val windSpeedMps = it.weatherItem.current?.wind_speed
                                val windSpeedMph = windSpeedMps?.let {
                                    Converter.convertMpsToMph(it)
                                }
                                binding.windSpeed.text = windSpeedMph?.toInt().toString()+" M/hr"
                            }else{
                                val windSpeedMps = it.weatherItem.current?.wind_speed
                                binding.windSpeed.text = windSpeedMps?.toInt().toString()+" M/hr"                            }
                        }


                        switchBetweenHourlyAndDaily(24.0F,18.0F, R.color.white,R.color.grey,View.VISIBLE,View.INVISIBLE)
                        hourlyAdapter.submitList(it.weatherItem.hourly)
                        dailyAdapter.submitList(it.weatherItem.daily)


                        binding.dailyForecast.setOnClickListener {
                            switchBetweenHourlyAndDaily(18.0F,24.0F,R.color.grey,R.color.white,View.INVISIBLE,View.VISIBLE)
                        }
                        binding.hourlyForecast.setOnClickListener {
                            switchBetweenHourlyAndDaily(24.0F,18.0F,R.color.white,R.color.grey,View.VISIBLE,View.INVISIBLE)
                        }
                    }
                    is ApiState.Loading ->{

                    }
                    else ->{
                        Toast.makeText(this@HomeActivity, "error is ${it}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val repository = WeatherRepository.getInstance(localData = LocalDataSource.getInstance(this), remoteData =  remoteDataSource)
        homeViewModelFactory = HomeViewModelFactory(repository)

        hourlyAdapter = HourlyAdapter(this)
        binding.hourlyRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.hourlyRecyclerView.adapter = hourlyAdapter

        dailyAdapter = DailyAdapter(this)
        binding.dailyRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.dailyRecyclerView.adapter = dailyAdapter


        /*binding.bottomSheet.setOnTouchListener { view, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    val layoutParams = view.layoutParams
                    layoutParams.height = dpToPx(324f, this) // Set your desired height in pixels
                    view.layoutParams = layoutParams
                }

                DragEvent.ACTION_DRAG_STARTED -> {
                    Log.i("TAG", "Mostafa: ACTION_DRAG_STARTED")
                    val layoutParams = view.layoutParams
                    if (event.y < 0) {
                        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    }else {
                        layoutParams.height = dpToPx(minHeightInDp.toFloat(), view.context)
                    }

                    view.layoutParams = layoutParams
                    true
                }
                DragEvent.ACTION_DRAG_LOCATION -> {
                    val newY = event.y + initialY
                    view.animate()
                        .y(newY)
                        .setDuration(0)
                        .start()
                    Log.i("TAG", "Mostafa: ACTION_DRAG_ENDED")

                    true
                }else -> false
            }
            true
        }*/



        binding.favorite.setOnClickListener {
            var intent = Intent(this, FavoriteActivity::class.java)
            startActivity(intent)
        }
        binding.setting.setOnClickListener {
            var intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }

        var originalTopToTop: Int = 0
        var originalTopToBottom: Int = 0
        var originalBottomToBottom: Int = 0
        val bottomSheetLayoutParams = binding.bottomSheet.layoutParams as ConstraintLayout.LayoutParams
        originalTopToTop = bottomSheetLayoutParams.topToTop
        originalTopToBottom = bottomSheetLayoutParams.topToBottom
        originalBottomToBottom = bottomSheetLayoutParams.bottomToBottom
        binding.bottomSheet.setOnClickListener {
            val bottomSheet = binding.bottomSheet
            val layoutParams = bottomSheet.layoutParams as ConstraintLayout.LayoutParams
            if (!isChanged) {
                layoutParams.topToBottom = binding.tempDesc.id
                layoutParams.bottomToBottom = binding.fakeView.id
                binding.dataLayout.visibility = View.VISIBLE
                isChanged = true
            } else {
                layoutParams.topToTop = originalTopToTop
                layoutParams.topToBottom = originalTopToBottom
                layoutParams.bottomToBottom = originalBottomToBottom
                binding.dataLayout.visibility = View.GONE
                isChanged = false
            }
            bottomSheet.layoutParams = layoutParams
        }
    }
    private fun switchBetweenHourlyAndDaily(hourlySize: Float, dailySize:Float, hourlyColor: Int, dailyColor:Int, hourlyVisibility: Int, dailyVisibility:Int) {
        binding.hourlyForecast.textSize = hourlySize
        binding.dailyForecast.textSize = dailySize
        binding.hourlyForecast.setTextColor(ContextCompat.getColor(this@HomeActivity, hourlyColor))
        binding.dailyForecast.setTextColor(ContextCompat.getColor(this@HomeActivity, dailyColor))
        binding.hourlyRecyclerView.visibility = hourlyVisibility
        binding.dailyRecyclerView.visibility = dailyVisibility
    }

    private fun dpToPx(dp: Float, context: Context): Int {
        val metrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics).toInt()
    }

    private fun getNewHeight(newHeight: Float): Float {
        if (minHeightInDp > newHeight) {
            return  minHeightInDp.toFloat()
        }else {
            return  newHeight
        }
    }
}