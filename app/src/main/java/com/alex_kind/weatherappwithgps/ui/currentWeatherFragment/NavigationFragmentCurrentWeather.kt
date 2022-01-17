package com.alex_kind.weatherappwithgps.ui.currentWeatherFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.alex_kind.weatherappwithgps.DataModel
import com.alex_kind.weatherappwithgps.RetrofitCreator
import com.alex_kind.weatherappwithgps.databinding.NavigationFragmentCurrentWeatherFragmentBinding
import kotlinx.coroutines.*
import retrofit2.awaitResponse

@DelicateCoroutinesApi
class NavigationFragmentCurrentWeather : Fragment() {
    //binding
    private var bind: NavigationFragmentCurrentWeatherFragmentBinding? = null
    private val binding get() = bind


    var cityName = ""
    var retrofitBuilder = RetrofitCreator().getRetrofit()
    private val dataModel: DataModel by activityViewModels()



    companion object {
        fun newInstance() = NavigationFragmentCurrentWeather()
    }

    private lateinit var viewModel: NavigationFragmentCurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bind = NavigationFragmentCurrentWeatherFragmentBinding.inflate(inflater, container, false)
        return binding?.root


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProvider(this).get(NavigationFragmentCurrentWeatherViewModel::class.java)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GlobalScope.launch(Dispatchers.Main) {
            getCityName()
            delay(100) // For a successful transfer cityName param
            weather()
        }

    }

    private fun getCityName() {
        dataModel.cityName.observe(activity as LifecycleOwner, {
             cityName = it
        })
    }

    private fun weather() {

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = retrofitBuilder.weather(
                    cityName,
                    "6e298e72d16587b721abb30bbf7c721a",
                    "metric"
                ).awaitResponse()
                if (response.isSuccessful) {
                    val data = response.body()!!
                    Log.d("MF", "Response is successful")
//                withContext(Dispatchers.Main) {
                    bind?.tvTemp?.text = data.list[0].main.temp.toString() + "°C"
                    bind?.tvDescription?.text = data.list[0].weather[0].description
                    val windSpeed = data.list[0].wind.speed
                    val windSpeedInKM = data.list[0].wind.speed * 3.6
                    bind?.tvWind?.text = windSpeed.toString() + " m/s" + "\n(" +
                            String.format("%.2f", windSpeedInKM) + " km/h)"
//                    "\n(${windSpeed*3.6} km/h)"
                    bind?.tvHumidity?.text = data.list[0].main.humidity.toString() + "%"
//                }
                }

            } catch (e: Exception) {
                Log.d("Error", "ERROR")
                Toast.makeText(activity, "ERROR", Toast.LENGTH_SHORT).show()
            }
        }
    }


}