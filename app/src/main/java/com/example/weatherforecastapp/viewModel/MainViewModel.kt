package com.example.weatherforecastapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapp.pojo.MWeather
import com.example.weatherforecastapp.pojo.ViewState
import com.example.weatherforecastapp.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {
    val dataObserve: StateFlow<ViewState<List<MWeather>>> get() = _dataObserve.asStateFlow()
    private val _dataObserve: MutableStateFlow<ViewState<List<MWeather>>> =
        MutableStateFlow(ViewState.Loading)
    val todaysDataObserve: StateFlow<ViewState<MWeather>> get() = _todaysDataObserve.asStateFlow()
    private val _todaysDataObserve: MutableStateFlow<ViewState<MWeather>> =
        MutableStateFlow(ViewState.Loading)


    fun getWeatherForecast() {
        viewModelScope.launch {
            _dataObserve.value = ViewState.Loading
            try {
                val dataList = repository.getWeatherForecast()
                _dataObserve.value = ViewState.Success(dataList)
            } catch (e: Exception) {
                if (e is IOException) {
                    _dataObserve.value = ViewState.NetworkError
                } else {
                    _dataObserve.value = ViewState.Error(e.toString())
                }
            }
        }
    }

    fun getTodaysTemp() {
        viewModelScope.launch {
            _todaysDataObserve.value = ViewState.Loading
            try {
                val data = repository.getTodaysTemp()
                _todaysDataObserve.value = ViewState.Success(data)
            } catch (e: Exception) {
                if (e is IOException) {
                    _todaysDataObserve.value = ViewState.NetworkError
                } else {
                    _todaysDataObserve.value = ViewState.Error(e.toString())
                }
            }
        }
    }

}
