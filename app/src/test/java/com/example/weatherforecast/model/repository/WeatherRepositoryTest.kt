package com.example.weatherforecast.model.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherforecast.FakeLocalDataSource
import com.example.weatherforecast.FakeRemoteDataSource
import com.example.weatherforecast.FakeRepository
import com.example.weatherforecast.model.database.ILocalDataSource
import com.example.weatherforecast.model.models.Favorite
import com.example.weatherforecast.model.models.Weather
import com.example.weatherforecast.model.network.IRemoteDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class WeatherRepositoryTest{
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var repo : IWeatherRepository
    lateinit var localDataSource: ILocalDataSource
    lateinit var remoteDataSource: IRemoteDataSource

    @Before
    fun setup(){
        var weather = Weather(null,null,null,null,44.44,null,null,55.55)
        var favorite = Favorite(44.44,55.55,"",24.0,12.0,30.0)
        var favorite1 = Favorite(44.44,55.55,"",24.0,12.0,30.0)

        localDataSource = FakeLocalDataSource(listOf(favorite,favorite1))
        remoteDataSource = FakeRemoteDataSource(weather)
        repo = FakeRepository(localDataSource,remoteDataSource)
    }
    @Test
    fun getWeatherData() = runBlockingTest {
        //Given
        var longitude = "44.44"
        var latitude = "55.55"
        //When
        var response = repo.getCurrentWeather(latitude,longitude).first()
        //Then
        assertThat(response.lat.toString(),`is`(latitude))
    }
    @Test
    fun insertToFavoriteAndGetAllFavorite() = runBlockingTest {
        //Given
        var favorite = Favorite(44.44,55.55,"",24.0,12.0,30.0)
        //When
        repo.insertFavorite(favorite)
        var response = repo.getAllFavorite().first()
        //Then
        assertThat(response.get(0).favLatitude, `is`(localDataSource.getAll().first().get(0).favLatitude))
    }
    @Test
    fun deleteFromFavorite() = runBlockingTest {
        //Given
        var favorite = Favorite(44.44,55.55,"",24.0,12.0,30.0)
        //When
        repo.deleteFavorite(favorite)
        val response = repo.getAllFavorite().firstOrNull()
        //Then
        assertThat(response, `is`(emptyList()))
    }
}