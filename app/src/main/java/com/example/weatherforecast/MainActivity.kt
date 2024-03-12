package com.example.weatherforecast

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.weatherforecast.favoritePage.viewModel.FavoriteViewModel
import com.example.weatherforecast.favoritePage.viewModel.FavoriteViewModelFactory
import com.example.weatherforecast.homePage.view.HomeActivity
import com.example.weatherforecast.homePage.viewModel.HomeViewModel
import com.example.weatherforecast.homePage.viewModel.HomeViewModelFactory
import com.example.weatherforecast.model.database.LocalDataSource
import com.example.weatherforecast.model.models.Favorite
import com.example.weatherforecast.model.network.RemoteDataSource
import com.example.weatherforecast.model.repository.WeatherRepository
import com.example.weatherforecast.model.utils.ApiState
import com.example.weatherforecast.model.utils.Constants
import com.example.weatherforecast.model.utils.Converter
import com.example.weatherforecast.model.utils.PreferenceManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

private const val LOCATION_PERMISSION_ID = 18
private var isLocationReceived = false
class MainActivity : AppCompatActivity() {

    lateinit var homeViewModelFactory: HomeViewModelFactory
    val homeViewModel : HomeViewModel by viewModels { homeViewModelFactory }

    lateinit var favoriteViewModelFactory: FavoriteViewModelFactory
    val favoriteViewModel: FavoriteViewModel by viewModels { favoriteViewModelFactory }

    val remoteDataSource = RemoteDataSource()

    private lateinit var myFusedLocationProviderClient: FusedLocationProviderClient
    private var latitude : Double? = 0.0
    private var longitude : Double? = 0.0

    var isUpdated =false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getLocation()

        val repository = WeatherRepository.getInstance(localData = LocalDataSource.getInstance(this), remoteData =  remoteDataSource)
        homeViewModelFactory = HomeViewModelFactory(repository)
        favoriteViewModelFactory = FavoriteViewModelFactory(repository)

       favoriteViewModel.favoriteList.observe(this){
           if (!isUpdated){
               it.forEach {
                   val longitude = it.favLongitude
                   val latitude = it.favLatitude
                   val language = PreferenceManager.getLanguage(this)
                   val temperature = PreferenceManager.getTempUnit(this)
                   homeViewModel.getHomeData(latitude.toString(),longitude.toString(),language,temperature)
                   lifecycleScope.launch {
                       homeViewModel.responseList.collect(){
                           when(it){
                               is ApiState.Success->{
                                   val favorite = Favorite(
                                       favLatitude = latitude,
                                       favLongitude = longitude,
                                       favMain = it.weatherItem.current?.weather?.get(0)?.main,
                                       favTemp = it.weatherItem.current?.temp.toString().toDouble(),
                                       favMin = it.weatherItem.daily?.get(0)?.temp?.min.toString().toDouble(),
                                       favMax = it.weatherItem.daily?.get(0)?.temp?.max.toString().toDouble(),
                                       time = 1
                                   )
                                   favoriteViewModel.updateFavoriteList(favorite)
                                   isUpdated = true
                                   Toast.makeText(this@MainActivity, "Updated Favorite List Successfully", Toast.LENGTH_SHORT).show()

                               }
                               is ApiState.Fail ->{
                                   Toast.makeText(this@MainActivity, "Failed To Update Favorite List", Toast.LENGTH_SHORT).show()
                               }
                               else ->{
                                   Toast.makeText(this@MainActivity, "Updating Favorite List", Toast.LENGTH_SHORT).show()

                               }
                           }

                       }
                   }

               }
           }

       }

    }

    override fun onResume() {
        super.onResume()
        getLocation()
    }

    private fun checkPermission() :Boolean{
        var result = false
        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            result = true
        }
        return result
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocation(){
        myFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        var myLocationRequest = LocationRequest()
        myLocationRequest.setInterval(0)
        myLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        var myLocationCallBack = object : LocationCallback(){
            override fun onLocationResult(p0: LocationResult) {
                latitude = p0.lastLocation?.latitude
                longitude = p0.lastLocation?.longitude
                //check if the boolean is false
                if(!isLocationReceived) {
                    // make the boolean true
                    isLocationReceived=true
                    //do what you want here
                    // your logic here
                    PreferenceManager.saveLatitude(this@MainActivity,latitude.toString().toDouble())
                    PreferenceManager.saveLongitude(this@MainActivity,longitude.toString().toDouble())
                    splash()
                }
            }
        }

        myFusedLocationProviderClient.requestLocationUpdates(myLocationRequest,myLocationCallBack,
            Looper.myLooper())
    }

    private fun requestPermission (){
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_ID)
    }

    private fun isLocationEnabled():Boolean{
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

    }

    @SuppressLint("MissingPermission")
    private fun getLocation(){
        if(checkPermission()){
            if(isLocationEnabled()){
                requestNewLocation()
            }else{
                var intent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }else{
            requestPermission()
        }

    }

    private fun splash(){
        Handler().postDelayed({
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }, Constants.SPLASH_DISPLAY_LENGTH)
    }
}