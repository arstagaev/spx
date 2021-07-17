package com.revolve44.solarpanelx.datasource.model.api


import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("3h")
    val h: Double
)