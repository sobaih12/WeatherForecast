package com.example.weatherforecast.favoritePage;

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weatherforecast.databinding.ActivityFavoriteBinding
import com.example.weatherforecast.homePage.view.HomeActivity

class FavoriteActivity : AppCompatActivity() {
    lateinit var binding: ActivityFavoriteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addToFavorite.setOnClickListener {
            val intent = Intent(this,MapActivity::class.java)
            startActivity(intent)
        }
        binding.home.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

    }
}