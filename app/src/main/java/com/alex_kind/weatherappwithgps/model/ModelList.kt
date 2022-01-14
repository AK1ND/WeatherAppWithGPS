package com.alex_kind.weatherappwithgps.model

data class ModelList(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<Model>,
    val message: Int
)