package com.revolve44.solarpanelx.datasource.models.api


import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all")
    val all: Int
)