package com.alex_kind.weatherappwithgps

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class DataModel: ViewModel() {

    val cityName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

}