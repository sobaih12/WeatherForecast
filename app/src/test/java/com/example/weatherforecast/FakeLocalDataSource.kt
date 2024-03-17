package com.example.weatherforecast

import com.example.weatherforecast.model.database.ILocalDataSource
import com.example.weatherforecast.model.models.Favorite
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeLocalDataSource(private val initialFavoriteList: List<Favorite> = emptyList()) : ILocalDataSource {

    private val _favoriteList = mutableListOf<Favorite>()

    override fun getAll(): Flow<List<Favorite>> {
        return flowOf(_favoriteList.toList())
    }

    override fun insert(favorite: Favorite) {
        _favoriteList.add(favorite)
    }

    override fun delete(favorite: Favorite) {
        _favoriteList.remove(favorite)
    }

    override fun updateAll(favorites: Favorite) {

    }
}
