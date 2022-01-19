package com.alex_kind.weatherappwithgps

import com.alex_kind.weatherappwithgps.model.ModelList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {

    //forecast?q=Minsk&appid=6e298e72d16587b721abb30bbf7c721a&units=metric
    @GET("forecast")
    fun weather(
        @Query("q") q: String,
        @Query("APPID") APPID: String,
        @Query("units") units: String
    ): Call<ModelList>
}