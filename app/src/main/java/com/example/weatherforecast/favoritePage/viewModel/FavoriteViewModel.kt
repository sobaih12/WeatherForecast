package com.example.weatherforecast.favoritePage.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.model.models.Favorite
import com.example.weatherforecast.model.repository.IWeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(var repository: IWeatherRepository) : ViewModel() {
    private var _mutableFavoriteList = MutableLiveData<List<Favorite>>()
    val favoriteList: LiveData<List<Favorite>> = _mutableFavoriteList

    init {
        getFavoriteList()
    }
    fun getFavoriteList(){
        viewModelScope.launch(Dispatchers.IO){
            repository.getAllFavorite().collect{
                _mutableFavoriteList.postValue(it)
            }
        }
    }
    fun deleteFromFavorite(favorite: Favorite){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteFavorite(favorite)
            getFavoriteList()
        }
    }
}