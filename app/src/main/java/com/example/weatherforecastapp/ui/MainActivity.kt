package com.example.weatherforecastapp.ui

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.adapter.ForeCastListAdapter
import com.example.weatherforecastapp.databinding.ActivityMainBinding
import com.example.weatherforecastapp.pojo.MWeather
import com.example.weatherforecastapp.pojo.ViewState
import com.example.weatherforecastapp.utils.kelvinToCelsius
import com.example.weatherforecastapp.utils.prepareMapData
import com.example.weatherforecastapp.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val loaderAnim by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_clockwise
        )
    }

    private val bottomUpAnim by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.slide_up
        )
    }
    private val controller by lazy {
        AnimationUtils.loadLayoutAnimation(
            this,
            R.anim.animation_bottom_to_up
        )
    }
    val binding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }
    private val foreCastListAdapter by lazy { ForeCastListAdapter(forecastMap, forecastDataList) }
    private val mainViewModel: MainViewModel by viewModels()
    private var forecastDataList: ArrayList<String> = ArrayList()
    private var forecastMap: LinkedHashMap<String, ArrayList<Int>> = LinkedHashMap()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        binding.successContent.rvForecast.apply {
            layoutAnimation = controller
            adapter = foreCastListAdapter
            addItemDecoration(
                DividerItemDecoration(
                    this@MainActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
        callApi()
        collectForeCastData()
        collectTodaysData()
        listener()
    }

    private fun listener() {
        binding.errorContent.btnRetry.setOnClickListener {
            callApi()
        }
    }

    private fun callApi() {
        mainViewModel.getTodaysTemp()
        mainViewModel.getWeatherForecast()
    }

    private fun collectTodaysData() {
        lifecycleScope.launch {
            mainViewModel.todaysDataObserve.collect {
                when (it) {
                    is ViewState.Loading -> showLoader()
                    is ViewState.Success -> setTodaysData(it.data)
                    is ViewState.Error, ViewState.NetworkError -> showErrorScreen()
                }
            }
        }
    }

    private fun showLoader() {
        binding.stateLoadingRun.visibility = View.GONE
        binding.imgLoader.apply {
            visibility = View.VISIBLE
            startAnimation(loaderAnim)
        }
    }

    private fun collectForeCastData() {
        lifecycleScope.launch {
            mainViewModel.dataObserve.collect {
                when (it) {
                    is ViewState.Loading -> showLoader()
                    is ViewState.Success -> notifyForecastData(it.data)
                    is ViewState.Error, ViewState.NetworkError -> showErrorScreen()
                }
            }
        }
    }

    private fun showErrorScreen() {
        binding.stateErrorRun.visibility = View.GONE
        binding.errorContent.root.visibility = View.VISIBLE
    }

    private fun notifyForecastData(data: List<MWeather>) {
        foreCastListAdapter.addMapData(prepareMapData(data))
        forecastMap.forEach {
            forecastDataList.add(it.key)
        }
        binding.stateSuccessRun.visibility = View.GONE
        binding.successContent.root.visibility = View.VISIBLE
        binding.successContent.rvForecast.apply {
            adapter?.notifyDataSetChanged()
            scheduleLayoutAnimation()
            startAnimation(bottomUpAnim)
            visibility = View.VISIBLE
        }
    }

    private fun setTodaysData(data: MWeather) {
        binding.successContent.apply {
            tvTodayTemp.text = "${data.main.temp.kelvinToCelsius()}°"
            tvCityName.text = data.name
        }
    }
}