package com.example.weatherforecast.model.database

import com.example.weatherforecast.model.models.Favorite
import kotlinx.coroutines.flow.Flow

interface ILocalDataSource {
    fun getAll(): Flow<List<Favorite>>
    fun insert(favorite: Favorite)
    fun delete(favorite: Favorite)
    fun updateAll(favorites: Favorite)
}