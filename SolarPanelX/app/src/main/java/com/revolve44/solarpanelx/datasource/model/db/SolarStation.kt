package com.revolve44.solarpanelx.datasource.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "solarstation"
)
data class SolarStation (
    @PrimaryKey(autoGenerate = true)
    var id : Int,
    val nominalPower : Int,
    val name : String,
    val lat : Float,
    val lon : Float,
    var notification : Boolean
)