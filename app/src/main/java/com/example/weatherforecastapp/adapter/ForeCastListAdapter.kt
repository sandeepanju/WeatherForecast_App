package com.example.weatherforecastapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.databinding.ItemListBinding
import com.example.weatherforecastapp.utils.kelvinToCelsius


class ForeCastListAdapter(
    private val foreCastMap: LinkedHashMap<String, ArrayList<Int>>,
    private val forecastDataList: ArrayList<String>
) :
    RecyclerView.Adapter<ForeCastListAdapter.ViewHolder>() {

    class ViewHolder(internal val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_list, parent, false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dayName = forecastDataList[position]
        holder.binding.apply {
            tvDay.text = dayName
            tvTemp.text = "${foreCastMap[dayName]?.average()?.kelvinToCelsius()}°"
        }
    }

    override fun getItemCount() = forecastDataList.size
    fun addMapData(prepareMapData: java.util.LinkedHashMap<String, java.util.ArrayList<Int>>) {
        foreCastMap.putAll(prepareMapData)
    }
}