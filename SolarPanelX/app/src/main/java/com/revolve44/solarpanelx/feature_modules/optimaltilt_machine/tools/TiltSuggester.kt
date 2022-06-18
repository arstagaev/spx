package com.revolve44.solarpanelx.feature_modules.optimaltilt_machine.tools

import kotlin.math.abs

class TiltSuggester() {

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
    fun azimuthToDirections(azimuth : Float) : String {

        when(azimuth){
            0.0F -> {return "--"}
            in 0F..11.25F, in 348.75F..360F ->   { return "N"   }
            in 11.25F..33.75F   ->  { return "NNE" }
            in 33.75F..56.25F   ->  { return "NE"  }
            in 56.25F..78.75F   ->  { return "ENE" }

            in 78.75F..101.25F  ->  { return "E"   }
            in 101.25F..123.75F ->  { return "ESE" }
            in 123.75F..146.25F ->  { return "SE"  }
            in 146.25F..168.75F ->  { return "SSE" }

            in 168.75F..191.25F ->  { return "S"   }
            in 191.25F..213.75F ->  { return "SSW" }
            in 213.75F..236.25F ->  { return "SW"  }
            in 236.25F..258.75F ->  { return "WSW" }

            in 258.75F..281.25F ->  { return "W"   }
            in 281.25F..303.75F ->  { return "WNW" }
            in 303.75F..326.25F ->  { return "NW"  }
            in 326.25F..348.75F ->  { return "NNW" }

            else -> {
                return "NON"
            }
        }
    }

}