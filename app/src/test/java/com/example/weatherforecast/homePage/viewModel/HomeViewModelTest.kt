package com.example.weatherforecast.homePage.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherforecast.FakeLocalDataSource
import com.example.weatherforecast.FakeRemoteDataSource
import com.example.weatherforecast.FakeRepository
import com.example.weatherforecast.model.database.ILocalDataSource
import com.example.weatherforecast.model.models.Favorite
import com.example.weatherforecast.model.models.Weather
import com.example.weatherforecast.model.network.IRemoteDataSource
import com.example.weatherforecast.model.repository.IWeatherRepository
import com.example.weatherforecast.model.utils.ApiState
import com.example.weatherforecast.model.utils.PreferenceManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class HomeViewModelTest{


    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var repo : IWeatherRepository
    lateinit var localDataSource: ILocalDataSource
    lateinit var remoteDataSource: IRemoteDataSource
    lateinit var homeViewModel: HomeViewModel
    lateinit var weatherData: Weather
    lateinit var favoriteData: Favorite

    @Before
    fun setup(){
        var weather = Weather(null,null,null,null,44.44,null,null,55.55)
        weatherData = Weather(null,null,null,null,44.44,null,null,55.55)

        var favorite = Favorite(44.44,55.55,"",24.0,12.0,30.0)
        favoriteData = Favorite(44.44,55.55,"",24.0,12.0,30.0)
        var favorite1 = Favorite(44.44,55.55,"",24.0,12.0,30.0)

        localDataSource = FakeLocalDataSource(listOf(favorite,favorite1))
        remoteDataSource = FakeRemoteDataSource(weather)
        repo = FakeRepository(localDataSource,remoteDataSource)
        homeViewModel = HomeViewModel(repo)
    }

    @Test
    fun getWeatherData() = runBlockingTest {
        //Given
        var data= weatherData
        //When
        homeViewModel.getHomeData(data.lat.toString(),data.lon.toString(),"en","metric")
        val response = homeViewModel.responseList.first()
        when(response){
            is ApiState.Success ->{
                data = response.weatherItem
            }
            is ApiState.Fail ->{}
            else ->{}
        }
        assertThat(data, `is`(notNullValue()))
    }
    @Test
    fun addToFavorite() = runBlockingTest {
        //Given
        var data = favoriteData
        //When
        homeViewModel.addToFavorite(data)
        //Then
        val response = repo.getAllFavorite().firstOrNull()
        assertThat(response?.contains(data), `is`(true))
    }

}