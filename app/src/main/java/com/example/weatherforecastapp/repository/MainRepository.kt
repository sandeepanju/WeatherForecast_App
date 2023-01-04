package com.example.weatherforecastapp.repository

import com.example.weatherforecastapp.api.ApiService
import com.example.weatherforecastapp.pojo.MWeather
import com.example.weatherforecastapp.utils.Constant.APP_ID
import com.example.weatherforecastapp.utils.Constant.CITY_NAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainRepository @Inject constructor(val api: ApiService) {
    private var weatherList = ArrayList<MWeather>()
    suspend fun getWeatherForecast(): List<MWeather> {
        withContext(Dispatchers.IO) {
            delay(5000L)
            val response = api.getForcast(CITY_NAME, APP_ID)
            weatherList.addAll(response.list)
        }
        return weatherList
    }

    suspend fun getTodaysTemp(): MWeather {
        return withContext(Dispatchers.IO) {
            delay(5000L)
            return@withContext api.getCurrentTemp(CITY_NAME, APP_ID)
        }
    }
}
