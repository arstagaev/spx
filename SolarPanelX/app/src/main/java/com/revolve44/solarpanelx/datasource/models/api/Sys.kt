package com.revolve44.solarpanelx.datasource.models.api


import com.google.gson.annotations.SerializedName

data class Sys(
    @SerializedName("pod")
    val pod: String
)