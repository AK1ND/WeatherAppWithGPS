package com.alex_kind.weatherappwithgps.ui.forecastFragment

import android.annotation.SuppressLint
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
import androidx.recyclerview.widget.RecyclerView
import com.alex_kind.weatherappwithgps.R
import com.alex_kind.weatherappwithgps.RecyclerAdapter
import com.alex_kind.weatherappwithgps.RetrofitCreator
import com.alex_kind.weatherappwithgps.databinding.NavigationFragmentForecastFiveDaysFragmentBinding
import com.alex_kind.weatherappwithgps.model.ModelList
import com.alex_kind.weatherappwithgps.ui.currentWeatherFragment.NavigationFragmentCurrentWeatherViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class NavigationFragmentForecastFiveDays : Fragment() {
    //    private val dataModel: DataModel by activityViewModels()
    private val dataModelCurrentWeather: NavigationFragmentCurrentWeatherViewModel by activityViewModels()

    private var bind: NavigationFragmentForecastFiveDaysFragmentBinding? = null
    private val binding get() = bind


    lateinit var recyclerView: RecyclerView
    lateinit var adapter: RecyclerAdapter
    lateinit var responseBody: ModelList
    var retrofitBuilder = RetrofitCreator().getRetrofit()

    var cityName = ""

//    companion object {
//        fun newInstance() = NavigationFragmentForecastFiveDays()
//    }

    private lateinit var viewModel: NavigationFragmentForecastFiveDaysViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = NavigationFragmentForecastFiveDaysFragmentBinding.inflate(inflater, container, false)
        return binding?.root


//        return inflater.inflate(
//            R.layout.navigation_fragment_forecast_five_days_fragment,
//            container,
//            false
//        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProvider(this).get(NavigationFragmentForecastFiveDaysViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_in_fragment)

        GlobalScope.launch(Dispatchers.Main) {
            getCityName()
            delay(100)
            weather()
        }

    }

    private fun getCityName() {
//        dataModel.cityName.observe(activity as LifecycleOwner, {
//            cityName = it
//        })
        dataModelCurrentWeather.cityName.observe(activity as LifecycleOwner, {
            cityName = it
        })
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun weather() {

        GlobalScope.launch(Dispatchers.Main) {
            try {

                val response = retrofitBuilder.weather(
                    cityName,
                    "6e298e72d16587b721abb30bbf7c721a",
                    "metric"
                ).awaitResponse()
                if (response.isSuccessful) {
                    responseBody = response.body()!!
                    adapter = RecyclerAdapter(responseBody)
                    adapter.notifyDataSetChanged()
                    recyclerView.adapter = adapter

                }

            } catch (e: Exception) {
                Log.d("error response", "ERROR RESPONSE")
                Toast.makeText(
                    activity,
                    "ERROR CONNECTION TO API", Toast.LENGTH_LONG
                ).show()
            }
        }

    }

}