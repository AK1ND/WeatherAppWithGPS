package com.alex_kind.weatherappwithgps.ui.currentWeatherFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.alex_kind.weatherappwithgps.databinding.NavigationFragmentCurrentWeatherFragmentBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class NavigationFragmentCurrentWeather : Fragment() {
    //binding
    private var bind: NavigationFragmentCurrentWeatherFragmentBinding? = null
    private val binding get() = bind


    private val dataModelCurrentWeather: NavigationFragmentCurrentWeatherViewModel by activityViewModels()


    private lateinit var vm: NavigationFragmentCurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bind = NavigationFragmentCurrentWeatherFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vm =
            ViewModelProvider(this).get(NavigationFragmentCurrentWeatherViewModel::class.java)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("CW", "FRAGMENT created")

        getDataFromVM()
    }

    private fun getDataFromVM() {
        dataModelCurrentWeather.temp.observe(activity as LifecycleOwner, {
            bind?.tvTemp?.text = it
        })
        dataModelCurrentWeather.description.observe(activity as LifecycleOwner, {
            bind?.tvDescription?.text = it
        })
        dataModelCurrentWeather.wind.observe(activity as LifecycleOwner, {
            bind?.tvWind?.text = it
        })
        dataModelCurrentWeather.humidity.observe(activity as LifecycleOwner, {
            bind?.tvHumidity?.text = it
        })
        val picasso = Picasso.get()
        var iconID = ""
        dataModelCurrentWeather.iconID.observe(activity as LifecycleOwner, {
            iconID = it
        })

        picasso.load(
            "https://openweathermap.org/img/wn/$iconID@2x.png"
        )
            .into(bind?.iconWeatherCurrent)
    }
}