package com.example.weatherforecast

import com.example.weatherforecast.model.database.ILocalDataSource
import com.example.weatherforecast.model.models.Favorite
import com.example.weatherforecast.model.models.Weather
import com.example.weatherforecast.model.network.IRemoteDataSource
import com.example.weatherforecast.model.repository.IWeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeRepository(private var localDataSource: ILocalDataSource, private var remoteDataSource: IRemoteDataSource):IWeatherRepository {
    override fun getAllFavorite(): Flow<List<Favorite>> {
        return localDataSource.getAll()
    }

    override fun insertFavorite(favorite: Favorite) {
        localDataSource.insert(favorite)
    }

    override fun deleteFavorite(favorite: Favorite) {
        localDataSource.delete(favorite)
    }

    override fun updateAll(favorites: Favorite) {

    }

    override suspend fun getCurrentWeather(
        lat: String?,
        lon: String?,
        lang: String?,
        units: String?
    ): Flow<Weather> {
        return flowOf(remoteDataSource.getCurrentWeather(lat,lon,"",lang,units))
    }
}