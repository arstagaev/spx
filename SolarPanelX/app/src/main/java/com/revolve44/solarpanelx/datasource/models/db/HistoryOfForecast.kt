package com.revolve44.solarpanelx.datasource.models.db

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "history_of_forecasts"
)
data class HistoryOfForecast(
    @PrimaryKey(autoGenerate = false)
    val idOfStation: Int,
    val unixTime: Long,
    val humanDate: String,
    val forecastValue: Int,
    val realOutputValue: Int,
    val precipitation : Float
)