package com.alex_kind.weatherappwithgps.ui.currentWeatherFragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alex_kind.weatherappwithgps.RetrofitCreator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class NavigationFragmentCurrentWeatherViewModel : ViewModel() {

    var retrofitBuilder = RetrofitCreator().getRetrofit()

    val cityName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val temp = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val wind = MutableLiveData<String>()
    val humidity = MutableLiveData<String>()
    val iconID = MutableLiveData<String>()


    init {
        Log.d("CW", "VM created")
    }

    override fun onCleared() {
        Log.d("CW", "VM cleared")
        super.onCleared()
    }

    fun responseVM() {
        GlobalScope.launch(Dispatchers.Main) {
            val response = retrofitBuilder.weather(
                "$cityName",
                "6e298e72d16587b721abb30bbf7c721a",
                "metric"
            ).awaitResponse()
            if (response.isSuccessful) {
                val data = response.body()!!
                Log.d("CW", "Response is successful")
                temp.value = data.list[0].main.temp.toString() + "°C"
                description.value = data.list[0].weather[0].description
                val windSpeed = data.list[0].wind.speed
                val windSpeedInKM = data.list[0].wind.speed * 3.6
                wind.value = windSpeed.toString() + " m/s" + "\n(" +
                        String.format("%.2f", windSpeedInKM) + " km/h)"
                humidity.value = data.list[0].main.humidity.toString() + "%"
            }
        }
    }
}