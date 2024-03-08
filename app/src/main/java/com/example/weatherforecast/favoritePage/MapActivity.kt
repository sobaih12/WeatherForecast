package com.example.weatherforecast.favoritePage;

import android.app.Activity;
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.weatherforecast.databinding.ActivityMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar

class MapActivity : AppCompatActivity(),OnMapReadyCallback {
    lateinit var binding: ActivityMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)
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

        // Set up initial map settings
        val initialLocation = LatLng(30.58, 32.27) // Default location
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 1F))

        // Set up marker click listener
        map.setOnMapClickListener { latLng ->
            map.clear() // Clear previous markers
            map.addMarker(MarkerOptions().position(latLng))

            // Retrieve latitude and longitude
            val latitude = latLng.latitude
            val longitude = latLng.longitude

            // Show Snackbar with action
            val snackbar = Snackbar.make(binding.mapView, "Latitude: $latitude, Longitude: $longitude", Snackbar.LENGTH_INDEFINITE)
            snackbar.setAction("Take", View.OnClickListener {
                // Action to take when "Take" is clicked
                // For example, you can show a Toast with the latitude and longitude
                Toast.makeText(this, "Latitude: $latitude, Longitude: $longitude taken!", Toast.LENGTH_SHORT).show()
            })
            snackbar.show()

            // Use latitude and longitude as needed
            // Example: Log the values
            Log.i("TAG", "onMapReady: $latitude")
            Log.i("TAG", "onMapReady: $longitude")
        }
    }
}