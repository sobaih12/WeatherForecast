package com.example.weatherforecast.favoritePage.view;

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.databinding.ActivityFavoriteBinding
import com.example.weatherforecast.favoritePage.viewModel.FavoriteViewModel
import com.example.weatherforecast.favoritePage.viewModel.FavoriteViewModelFactory
import com.example.weatherforecast.homePage.view.DailyAdapter
import com.example.weatherforecast.homePage.view.HomeActivity
import com.example.weatherforecast.homePage.view.HourlyAdapter
import com.example.weatherforecast.homePage.viewModel.HomeViewModel
import com.example.weatherforecast.homePage.viewModel.HomeViewModelFactory
import com.example.weatherforecast.model.database.LocalDataSource
import com.example.weatherforecast.model.models.Favorite
import com.example.weatherforecast.model.network.RemoteDataSource
import com.example.weatherforecast.model.repository.WeatherRepository

class FavoriteActivity : AppCompatActivity(),OnDeleteClickListener {

    lateinit var binding: ActivityFavoriteBinding
    lateinit var favoriteAdapter: FavoriteAdapter
    lateinit var favoriteViewModelFactory: FavoriteViewModelFactory
    val favoriteViewModel:FavoriteViewModel by viewModels { favoriteViewModelFactory }
    val remoteDataSource = RemoteDataSource()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = WeatherRepository.getInstance(localData = LocalDataSource.getInstance(this), remoteData =  remoteDataSource)
        favoriteViewModelFactory = FavoriteViewModelFactory(repository)

        favoriteAdapter = FavoriteAdapter(this,this)
        binding.favoriteRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.favoriteRecyclerView.adapter = favoriteAdapter

        favoriteViewModel.favoriteList.observe(this) {
            favoriteAdapter.submitList(it)
        }

        binding.addToFavorite.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.home.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            
        }
    }

    override fun onDeleteFromFavorite(favorite: Favorite) {
        favoriteViewModel.deleteFromFavorite(favorite)
    }
}