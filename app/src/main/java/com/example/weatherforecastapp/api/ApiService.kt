package com.example.weatherforecastapp.api

import com.example.weatherforecastapp.pojo.MForecast
import com.example.weatherforecastapp.pojo.MWeather
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("weather")
    suspend fun getCurrentTemp(@Query("q") city: String, @Query("APPID") appId: String): MWeather

    @GET("forecast")
    suspend fun getForcast(@Query("q") city: String, @Query("APPID") appId: String): MForecast

}
