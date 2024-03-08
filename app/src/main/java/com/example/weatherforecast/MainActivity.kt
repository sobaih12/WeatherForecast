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
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.weatherForecast.model.utils.Constants
import com.example.weatherforecast.homePage.view.HomeActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
private const val LOCATION_PERMISSION_ID = 18
private var isLocationReceived = false
class MainActivity : AppCompatActivity() {

    private lateinit var myFusedLocationProviderClient: FusedLocationProviderClient
    private var latitude : Double? = 0.0
    private var longitude : Double? = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getLocation()
        Log.i("Location", "onCreate: "+latitude)
        Log.i("Location", "onCreate: "+longitude)


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
                latitude = p0?.lastLocation?.latitude
                longitude = p0?.lastLocation?.longitude
                Log.i("Location", "onLocationResult: "+latitude)
                Log.i("Location", "onLocationResult: "+longitude)
                if(!isLocationReceived) {
                    isLocationReceived=true
                    splash(longitude,latitude)
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

    private fun splash(lon:Double?,lat:Double?){
        Handler().postDelayed({
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("Lon",lon)
            intent.putExtra("Lat",lat)
            startActivity(intent)
            finish()
        }, Constants.SPLASH_DISPLAY_LENGTH)
    }
}