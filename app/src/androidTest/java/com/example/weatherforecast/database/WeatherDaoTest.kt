package com.example.weatherforecast.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.example.weatherforecast.model.database.WeatherDatabase
import com.example.weatherforecast.model.models.Favorite
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class WeatherDaoTest{
    @get:Rule
    val role = InstantTaskExecutorRule()

    lateinit var weatherDatabase: WeatherDatabase
    @Before
    fun setUp(){
        weatherDatabase = Room.inMemoryDatabaseBuilder(getApplicationContext(), WeatherDatabase::class.java).build()
    }

    @Test
    fun insertFavoriteInDatabaseAndGetAllList () = runBlockingTest{
        //Given
        val favorite = Favorite(44.44,55.55,"main",24.0,12.0,30.0)
        weatherDatabase.getWeatherDao().insert(favorite)
        //When
        val getData = weatherDatabase.getWeatherDao().getAllFavorite().first()
        //Then
        assertThat(getData.first().favLatitude, `is`(favorite.favLatitude))
    }

    @Test
    fun deleteFavoriteFromDatabase() = runBlockingTest {
        //Given
        val favorite = Favorite(44.44,55.55,"main",24.0,12.0,30.0)
        weatherDatabase.getWeatherDao().insert(favorite)

        //When
        weatherDatabase.getWeatherDao().delete(favorite)
        val getData = weatherDatabase.getWeatherDao().getAllFavorite().first().isEmpty()

        //Then
        assertThat(getData,`is`(true))
    }

//    @Test
//    fun updateFavoriteInDatabase() = runBlockingTest {
//        //Given
//        val originalFavorite = Favorite(44.44,55.55,"main",24.0,12.0,30.0)
//        val updatedFavorite = Favorite(444.44,555.55,"main",24.0,12.0,30.0)
//
//        //When
//        weatherDatabase.getWeatherDao().insert(originalFavorite)
//        weatherDatabase.getWeatherDao().updateAll(updatedFavorite)
//
//        //Then
//        val getData = weatherDatabase.getWeatherDao().getAllFavorite().first()
//        assertThat(getData.first().favLatitude, `is`(updatedFavorite.favLatitude))
//    }

    @After
    fun finish(){
        weatherDatabase.close()
    }
}