package com.example.optimaltilt.tools

import kotlin.math.abs

class TiltSuggestor() {

    enum class Season(){
        WINTER, OFF_SEASON, SUMMER
    }

    //(34 * 0.9) + 29 = 59.6°
    fun defineOptimalTilt(lat: Double, season: Season) : Int {
        val lat = abs(lat)
        when(season){
            Season.WINTER ->     return defineWhenWinter(lat)
            Season.OFF_SEASON -> return defineWhenOFFSeason(lat)
            Season.SUMMER ->     return defineWhenSummer(lat)
        }
    }

    fun defineWhenSummer(lat: Double) : Int{
        when(lat){
            in 0.0  .. 15.0 -> return (15.0).toInt()
            in 15.0 .. 25.0 -> return ((lat*0.5F)).toInt()
            in 25.0 .. 30.0 -> return ((lat*0.5F)+5.0 ).toInt()
            in 30.0 .. 35.0 -> return ((lat*0.5F)+10.0).toInt()
            in 35.0 .. 40.0 -> return ((lat*0.5F)+15.0).toInt()
            else ->            return  ((lat*0.5F)+20.0).toInt()

        }
    }

    fun defineWhenOFFSeason(lat: Double) : Int{
        when(lat){
            in 0.0  .. 15.0 -> return (15.0).toInt()
            in 15.0 .. 25.0 -> return ((lat*0.6F)).toInt()
            in 25.0 .. 30.0 -> return ((lat*0.6F)+5.0 ).toInt()
            in 30.0 .. 35.0 -> return ((lat*0.6F)+10.0).toInt()
            in 35.0 .. 40.0 -> return ((lat*0.6F)+15.0).toInt()
            else ->            return  ((lat*0.6F)+20.0).toInt()

        }

    }

    fun defineWhenWinter(lat: Double) : Int{
        when(lat){
            in 0.0  .. 15.0 -> return (15.0).toInt()
            in 15.0 .. 25.0 -> return ((lat*0.8F)).toInt()
            in 25.0 .. 30.0 -> return ((lat*0.8F)+5.0 ).toInt()
            in 30.0 .. 35.0 -> return ((lat*0.8F)+10.0).toInt()
            in 35.0 .. 40.0 -> return ((lat*0.8F)+15.0).toInt()
            else ->            return  ((lat*0.8F)+20.0).toInt()

        }
    }

}