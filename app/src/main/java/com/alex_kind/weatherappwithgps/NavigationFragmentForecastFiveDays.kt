package com.alex_kind.weatherappwithgps

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import com.alex_kind.weatherappwithgps.databinding.NavigationFragmentForecastFiveDaysFragmentBinding

class NavigationFragmentForecastFiveDays : Fragment() {
    private val dataModel: DataModel by activityViewModels()

    private var bind: NavigationFragmentForecastFiveDaysFragmentBinding? = null
    private val binding get() = bind

    companion object {
        fun newInstance() = NavigationFragmentForecastFiveDays()
    }

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

        dataModel.cityName.observe(activity as LifecycleOwner, {
            bind?.forecast?.text = it
        })
    }

}