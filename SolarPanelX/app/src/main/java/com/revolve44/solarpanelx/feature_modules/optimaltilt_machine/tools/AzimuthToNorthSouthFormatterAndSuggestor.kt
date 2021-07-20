package com.revolve44.solarpanelx.feature_modules.optimaltilt_machine.tools

import android.graphics.Color
import android.util.Log
import com.revolve44.solarpanelx.feature_modules.optimaltilt_machine.model.HowWellDirectedSolarPanel

class AzimuthToNorthSouthFormatterAndSuggestor {
    fun calc(azimuth : Float) : String{
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

    fun findOptimalDirection(lat : Float) : String {
        when(lat){
            in 0F..90.0F       ->  { return "S" }
            in -90.0F..0F   ->     { return "N" }

            else -> {
                return "S"
            }
        }

    }

    fun defineWhichCloseToOptimal(lat : Float, actualDirection : Float) : HowWellDirectedSolarPanel {
        Log.i("fff"," fff ${lat} ${actualDirection}")

        when(lat){
            in 0F..90.0F       ->  {
                when(actualDirection){
                    in 330F..360F -> return HowWellDirectedSolarPanel("Excellent",Color.GREEN)
                    in 0F..30F ->    return HowWellDirectedSolarPanel("Excellent",Color.GREEN)

                    in 270F..330F -> return HowWellDirectedSolarPanel("Not very good",Color.YELLOW)
                    in 30F..90F ->  return HowWellDirectedSolarPanel("Not very good",Color.YELLOW)

                    in 90F..270F -> return HowWellDirectedSolarPanel("Bad",Color.RED)

                    else -> {
                        return HowWellDirectedSolarPanel("Error code: 01",Color.MAGENTA)
                    }
                }

            }
            in -90.0F..0F -> {

                when(actualDirection){
                    in 270F..360F -> return HowWellDirectedSolarPanel("Bad",Color.RED)
                    in 0F..90F -> return HowWellDirectedSolarPanel("Bad",Color.RED)

                    in 225F..270F -> return HowWellDirectedSolarPanel("Not very good",Color.YELLOW)
                    in 90F..135F ->  return HowWellDirectedSolarPanel("Not very good",Color.YELLOW)

                    in 135F..225F -> return HowWellDirectedSolarPanel("Excellent",Color.GREEN)

                    else -> {
                        return HowWellDirectedSolarPanel("Predictor is Broken:(",Color.MAGENTA)
                    }
                }
            }
            else -> {
                return HowWellDirectedSolarPanel("Error code: 02",Color.CYAN)

            }
        }

    }
}