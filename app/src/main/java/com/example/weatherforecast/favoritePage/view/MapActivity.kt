package com.example.weatherforecast.favoritePage.view;

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.weatherforecast.databinding.ActivityMapBinding
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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class MapActivity : AppCompatActivity(),OnMapReadyCallback {
    lateinit var binding: ActivityMapBinding
    lateinit var homeViewModelFactory: HomeViewModelFactory
    val homeViewModel : HomeViewModel by viewModels { homeViewModelFactory }
    val remoteDataSource = RemoteDataSource()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)

        val repository = WeatherRepository.getInstance(localData = LocalDataSource.getInstance(this), remoteData =  remoteDataSource)
        homeViewModelFactory = HomeViewModelFactory(repository)
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val map = googleMap

        val initialLocation = LatLng(PreferenceManager.getLatitude(this),PreferenceManager.getLongitude(this))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 5F))

        map.addMarker(MarkerOptions().position(initialLocation))
        // Set up marker click listener
        map.setOnMapClickListener { latLng ->
            map.clear() // Clear previous markers
            map.addMarker(MarkerOptions().position(latLng))

            // Retrieve latitude and longitude
            val latitude = latLng.latitude
            val longitude = latLng.longitude

            // Show Snackbar with action
            val snackbar = Snackbar.make(binding.mapView, "Save ${Converter.getAddressEnglish(this@MapActivity,latitude,longitude)} ?", Snackbar.LENGTH_INDEFINITE)
            snackbar.setAction("Save", View.OnClickListener {
                homeViewModel.getHomeData(latitude.toString(),longitude.toString(), Constants.LANGUAGE_EN, Constants.UNITS_CELSIUS)
                lifecycleScope.launch {
                    homeViewModel.responseList.collect{
                        when(it){
                            is ApiState.Success ->{
                                val favorite = Favorite(
                                    favLatitude = latitude,
                                    favLongitude = longitude,
                                    favMain = it.weatherItem.current?.weather?.get(0)?.main,
                                    favTemp = it.weatherItem.current?.temp.toString().toDouble(),
                                    favMin = it.weatherItem.daily?.get(0)?.temp?.min.toString().toDouble(),
                                    favMax = it.weatherItem.daily?.get(0)?.temp?.max.toString().toDouble()
                                )
                                homeViewModel.addToFavorite(favorite)
                                Toast.makeText(this@MapActivity, "${Converter.getAddressEnglish(this@MapActivity,latitude,longitude)} Added to Favorite Model", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@MapActivity,FavoriteActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            is ApiState.Loading ->{
                                Toast.makeText(this@MapActivity, "Getting Data From Api", Toast.LENGTH_SHORT).show()
                            }
                            else ->{
                                Toast.makeText(this@MapActivity, "Failed to get data from Api", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            })
            snackbar.show()

        }
    }
}