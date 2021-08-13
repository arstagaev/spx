package com.revolve44.solarpanelx.datasource.models.db

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
        tableName = "forecastcell"
)
data class ForecastCell(
    @PrimaryKey(autoGenerate = false)
    val unixTime:Long,
    val day: Int,
    val HumanTime: String,
    val forecast: Int
)