package com.example.weatherForecast.homePage.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherForecast.model.repository.IWeatherRepository
import com.example.weatherForecast.model.utils.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(var repository: IWeatherRepository) :ViewModel() {

//    private var mutableResponseList = MutableLiveData<Weather>()
//    val responseBody: LiveData<Weather> = mutableResponseList
    private var _mutableResponseList: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Loading)
    var responseList: StateFlow<ApiState> = _mutableResponseList


    fun getHomeData(lat: String?,
                    lon: String?,
                    lang: String,
                    units: String){
        viewModelScope.launch(Dispatchers.IO){
           repository.getCurrentWeather(lat,lon,lang,units)
               .catch { err -> _mutableResponseList.value = ApiState.Fail(err)}
               .collect{ success -> _mutableResponseList.value = ApiState.Success(success)}

        }
    }

}