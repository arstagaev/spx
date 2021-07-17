package com.revolve44.solarpanelx.datasource.model.api


import com.google.gson.annotations.SerializedName

data class FiveDaysForecastModel(
    @SerializedName("cod")
    val cod: String,
    @SerializedName("message")
    val message: Int,
    @SerializedName("cnt")
    val cnt: Int,
    @SerializedName("list")
    val list: List<ElementOfList>,
    @SerializedName("city")
    val city: City
)