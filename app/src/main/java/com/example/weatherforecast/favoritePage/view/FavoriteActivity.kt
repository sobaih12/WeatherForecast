package com.example.weatherforecast.favoritePage.view;

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
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
import com.example.weatherforecast.model.utils.ApiState
import com.example.weatherforecast.model.utils.PreferenceManager
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity(),OnDeleteClickListener {

    lateinit var binding: ActivityFavoriteBinding
    lateinit var favoriteAdapter: FavoriteAdapter
    lateinit var favoriteViewModelFactory: FavoriteViewModelFactory
    lateinit var homeViewModelFactory: HomeViewModelFactory
    val favoriteViewModel:FavoriteViewModel by viewModels { favoriteViewModelFactory }
    val homeViewModel : HomeViewModel by viewModels { homeViewModelFactory }
    val remoteDataSource = RemoteDataSource()
    var isUpdated =false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = WeatherRepository.getInstance(localData = LocalDataSource.getInstance(this), remoteData =  remoteDataSource)
        favoriteViewModelFactory = FavoriteViewModelFactory(repository)
        homeViewModelFactory = HomeViewModelFactory(repository)





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

//        favoriteViewModel.favoriteList.observe(this){
//            if (!isUpdated){
//                it.forEach {
//                    val longitude = it.favLongitude
//                    val latitude = it.favLatitude
//                    val language = PreferenceManager.getLanguage(this)
//                    val temperature = PreferenceManager.getTempUnit(this)
//                    homeViewModel.getHomeData(latitude.toString(),longitude.toString(),language,temperature)
//                    lifecycleScope.launch {
//                        homeViewModel.responseList.collect(){
//                            when(it){
//                                is ApiState.Success->{
//                                    val favorite = Favorite(
//                                        favLatitude = latitude,
//                                        favLongitude = longitude,
//                                        favMain = it.weatherItem.current?.weather?.get(0)?.description,
//                                        favTemp = it.weatherItem.current?.temp.toString().toDouble(),
//                                        favMin = it.weatherItem.daily?.get(0)?.temp?.min.toString().toDouble(),
//                                        favMax = it.weatherItem.daily?.get(0)?.temp?.max.toString().toDouble()
//                                    )
//                                    isUpdated = true
//                                    favoriteViewModel.updateFavoriteList(favorite)
//                                    Toast.makeText(this@FavoriteActivity, "Updated Favorite List Successfully", Toast.LENGTH_SHORT).show()
//                                }
//                                is ApiState.Fail ->{
//                                    Toast.makeText(this@FavoriteActivity, "Failed To Update Favorite List", Toast.LENGTH_SHORT).show()
//                                }
//                                else ->{
//                                    Toast.makeText(this@FavoriteActivity, "Updating Favorite List", Toast.LENGTH_SHORT).show()
//
//                                }
//                            }
//
//                        }
//
//                    }
//
//                }
//            }
//
//        }
    }

    override fun onDeleteFromFavorite(favorite: Favorite) {
        favoriteViewModel.deleteFromFavorite(favorite)
    }

    override fun onNavigateToDetails(favorite: Favorite) {
        val intent = Intent(this, FavoriteCountry::class.java)
        Log.i("TAG", "onNavigateToDetails: "+favorite.favLatitude)
        intent.putExtra("LATITUDE", favorite.favLatitude.toString())
        intent.putExtra("LONGITUDE", favorite.favLongitude.toString())
        startActivity(intent)
    }
}