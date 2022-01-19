package com.alex_kind.weatherappwithgps

import com.alex_kind.weatherappwithgps.const.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitCreator {

    fun getRetrofit(): RetrofitService {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build().create(RetrofitService::class.java)
        return retrofit
    }
}