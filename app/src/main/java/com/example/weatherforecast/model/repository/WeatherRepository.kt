package com.example.weatherforecast.model.repository


import com.example.weatherforecast.model.database.ILocalDataSource
import com.example.weatherforecast.model.models.Favorite
import com.example.weatherforecast.model.models.Weather
import com.example.weatherforecast.model.network.IRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRepository private constructor(
    var localData: ILocalDataSource,
    var remoteData: IRemoteDataSource
):IWeatherRepository {

    companion object{
        @Volatile
        private var INSTANCE: WeatherRepository? = null
        fun getInstance (localData: ILocalDataSource, remoteData: IRemoteDataSource): WeatherRepository{
            return INSTANCE ?: synchronized(this) {
                val instance =WeatherRepository(localData,remoteData)
                INSTANCE = instance
                instance }
        }
    }

    override fun getAllFavorite(): Flow<List<Favorite>> {
        return localData.getAll()
    }

    override fun insertFavorite(favorite: Favorite) {
        localData.insert(favorite)
    }

    override fun deleteFavorite(favorite: Favorite) {
        localData.delete(favorite)
    }

    override suspend fun getCurrentWeather(
        lat: String?,
        lon: String?,
        lang: String,
        units: String
    ): Flow<Weather> = flow{
         emit(remoteData.getCurrentWeather(lat = lat, lon =lon, lang =lang, units = units))
    }
}