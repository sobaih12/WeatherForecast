package com.example.weatherforecast.favoritePage.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherforecast.FakeLocalDataSource
import com.example.weatherforecast.FakeRemoteDataSource
import com.example.weatherforecast.FakeRepository
import com.example.weatherforecast.homePage.viewModel.HomeViewModel
import com.example.weatherforecast.model.database.ILocalDataSource
import com.example.weatherforecast.model.models.Favorite
import com.example.weatherforecast.model.models.Weather
import com.example.weatherforecast.model.network.IRemoteDataSource
import com.example.weatherforecast.model.repository.IWeatherRepository
import com.example.weatherforecast.model.utils.ApiState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class FavoriteViewModelTest{
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var repo : IWeatherRepository
    lateinit var localDataSource: ILocalDataSource
    lateinit var remoteDataSource: IRemoteDataSource
    lateinit var favoriteViewModel: FavoriteViewModel
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
        favoriteViewModel = FavoriteViewModel(repo)
    }

    @Test
    fun getFavoriteList() = runBlockingTest {
        //Given
        localDataSource.insert(favoriteData)
        //When
        favoriteViewModel.getFavoriteList()
        val response = favoriteViewModel.favoriteList.value
        //Then
        assertThat(response, `is`(notNullValue()))
    } @Test
    fun deleteFromFavorite() = runBlockingTest {
        //Given
        localDataSource.insert(favoriteData)
        //When
        favoriteViewModel.deleteFromFavorite(favoriteData)
        val response = favoriteViewModel.favoriteList.value
        //Then
        assertThat(response, `is`(emptyList()))
    }

}