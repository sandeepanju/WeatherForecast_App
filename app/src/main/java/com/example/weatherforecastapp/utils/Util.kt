package com.example.weatherforecastapp.utils

import com.example.weatherforecastapp.pojo.MWeather
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.LinkedHashMap

fun getDay(sec: String): String {
    val calendar: Calendar = Calendar.getInstance()
    val format = SimpleDateFormat("EEEE", Locale.getDefault())
    calendar.timeInMillis = sec.toLong() * 1000
    return format.format(calendar.time)
}

fun prepareMapData(data: List<MWeather>): LinkedHashMap<String, ArrayList<Int>> {
    val map = LinkedHashMap<String, ArrayList<Int>>()
    data.forEach {
        val key = getDay(it.dt)
        val value = it.main.temp.toInt()
        if (map.isNotEmpty() && map.containsKey(key)) {
            map[key]?.add(value)
        } else {
            val list = ArrayList<Int>()
            list.add(value)
            map[key] = list
        }
    }
    map.remove(map.keys.first())
    return map
}

fun Double.kelvinToCelsius() = (this - 273.15).toInt()

