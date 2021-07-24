package com.revolve44.solarpanelx.datasource.local


import android.content.Context
import android.content.SharedPreferences
import com.revolve44.solarpanelx.BuildConfig

object PreferenceMaestro {

    private var NAME = getNameForSharedPref()
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences


    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    fun getNameForSharedPref() : String{
        return "Data_SPX"

    }

    /**
     * SharedPreferences extension function, so we won't need to call edit() and apply()
     * ourselves on every SharedPreferences operation.
     */
    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var timeOfLastDataUpdate: String
        
        get() = preferences.getString("lastupd", "no data").toString()
        
        set(value) = preferences.edit {
            it.putString("lastupd", value)
        }

    var timeOfLastDataUpdateLong: Long

        get() = preferences.getLong("lastupd_L", 1234)

        set(value) = preferences.edit {
            it.putLong("lastupd_L", value)
        }


    var chosenSolarPanelInstallationDate: Int // in years
        get() = preferences.getInt("solarPanelInstallationDate", 1)
        set(value) = preferences.edit {
            it.putInt("solarPanelInstallationDate", value)
        }

    var chosenSolarPanelEfficiency: Int // in %
        get() = preferences.getInt("solarPanelEfficiency", 20)
        set(value) = preferences.edit {
            it.putInt("solarPanelEfficiency", value)
        }




    var calibrationCoeff: Int // 100 is 1.0 coeff
        get() = preferences.getInt("calibration", 100)
        set(value) = preferences.edit {
            it.putInt("calibration", value)
        }

    var lat: Float
        
        get() = preferences.getFloat("lat", 0.0f)
        
        set(value) = preferences.edit {
            it.putFloat("lat", value)
        }

    var lon: Float
        
        get() = preferences.getFloat("lon", 0.0f)
        
        set(value) = preferences.edit {
            it.putFloat("lon", value)
        }

    var leftChartMonthandDay: String
        get() = preferences.getString("leftchartd", "1").toString()
        set(value) = preferences.edit {
            it.putString("leftchartd", value)
        }

    var rightChartMonthandDay: String
        
        get() = preferences.getString("rightchartd", "1").toString()
        
        set(value) = preferences.edit {
            it.putString("rightchartd", value)
        }

    var fourChartMonthandDay: String
        
        get() = preferences.getString("thirdchartdSol", "1").toString()
        
        set(value) = preferences.edit {
            it.putString("thirdchartdSol", value)
        }

    var fiveChartMonthandDay: String
        
        get() = preferences.getString("fourthchartdSol", "1").toString()
        
        set(value) = preferences.edit {
            it.putString("fourthchartdSol", value)
        }



    var averageForecastperThreeHours: Float
        get() = preferences.getFloat("avr", 1.0f)

        set(value) = preferences.edit {
            it.putFloat("avr", value)
        }

//    var lastUpdateDate: String
//
//        get() = preferences.getString("lstupd", "1").toString()
//
//        set(value) = preferences.edit {
//            it.putString("lstupd", value)
//        }

    var temp: Int
        
        get() = (preferences.getInt("temp", 1)).toInt()
        
        set(value) = preferences.edit {
            it.putInt("temp", value)
        }

    var chosenStationNOMINALPOWER: Int
        
        get() = preferences.getInt("nompower", 0)
        
        set(value) = preferences.edit {
            it.putInt("nompower", value)
        }
    var chosenStationNAME: String
        
        get() = preferences.getString("name", "my PV station 1").toString()
        
        set(value) = preferences.edit {
            it.putString("name", value)
        }
    var chosenStationLAT: Float
        
        get() = preferences.getFloat("lat", 0.0f)
        
        set(value) = preferences.edit {
            it.putFloat("lat", value)
        }

    var chosenStationLON: Float
        
        get() = preferences.getFloat("lon", 0.0f)
        
        set(value) = preferences.edit {
            it.putFloat("lon", value)
        }

    var chosenStationCITY: String
        
        get() = preferences.getString("city", "City not defined").toString()
        
        set(value) = preferences.edit {
            it.putString("city", value)
        }

    var solarDayDuration: Int
        
        get() = preferences.getInt("solarDay", 1)
        
        set(value) = preferences.edit {
            it.putInt("solarDay", value)
        }

    var pickedColorofToolbarTitle: Int
        
        get() = preferences.getInt("pickedColor", 0)
        
        set(value) = preferences.edit {
            it.putInt("pickedColor", value)
        }

    var pickedColorofMainScreen: Int
        
        get() = preferences.getInt("pickedColorofMainScreen", 0)
        
        set(value) = preferences.edit {
            it.putInt("pickedColorofMainScreen", value)
        }


    var sunrise: String
        get() = preferences.getString("sunrise", "--:--").toString()
        set(value) = preferences.edit {
            it.putString("sunrise", value)
        }



    var sunset: String
        get() = preferences.getString("sunset", "--:--").toString()
        set(value) = preferences.edit {
            it.putString("sunset", value)
        }

    var sunriseHour: Float
        get() = preferences.getFloat("sunrise_hr", 0f)
        set(value) = preferences.edit {
            it.putFloat("sunrise_hr", value)
        }

    var sunsetHour: Float
        get() = preferences.getFloat("sunset_hr", 0f)
        set(value) = preferences.edit {
            it.putFloat("sunset_hr", value)
        }

    var currentGMTinDefineLocation: Int
        get() = preferences.getInt("currentGMTinDefineLocation", 0)
        set(value) = preferences.edit {
            it.putInt("currentGMTinDefineLocation", value)
        }



    var isFirstStart: Boolean
        get() = preferences.getBoolean("firstStart_spx", true)
        set(value) = preferences.edit {
            it.putBoolean("firstStart_spx", value)
        }

    var forceHelperMainScreen: Boolean
        get() = preferences.getBoolean("forceHelper_mainscreen", true)
        set(value) = preferences.edit {
            it.putBoolean("forceHelper_mainscreen", value)
        }

//    var forecastForNow: Float
//        get() = preferences.getFloat("forecastForNow", 0.0f)
//        set(value) = preferences.edit {
//            it.putFloat("forecastForNow", value)
//        }

    var chosenTimeZone: String
        get() = preferences.getString("timezone", "--:--").toString()
        set(value) = preferences.edit {
            it.putString("timezone", value)
        }
    var chosenCurrency: String
        get() = preferences.getString("currency", "$").toString()
        set(value) = preferences.edit {
            it.putString("currency", value)
        }

    //in US 13.19 cents per kilowatt hour (kWh)
    var pricePerkWh: Float
        get() = preferences.getFloat("ppkWh", 1f)
        set(value) = preferences.edit {
            it.putFloat("ppkWh", value)
        }

    var investmentsToSolarStation: Int
        get() = preferences.getInt("investments", 0)
        set(value) = preferences.edit {
            it.putInt("investments", value)
        }


    var isNightNode: Boolean
        get() = preferences.getBoolean("isNightNode", true)
        set(value) = preferences.edit {
            it.putBoolean("isNightNode", value)
        }

    // metric = 0 , imperial = 1
    var measurementSys : Int
        get() = preferences.getInt("measurementsys", 0)
        set(value) = preferences.edit {
            it.putInt("measurementsys", value)
        }

    ////////////////////////////////////////////////////////////////////////
    var timezoneL: Float
        get() = preferences.getFloat("timezoneF", 0F)
        set(value) = preferences.edit {
            it.putFloat("timezoneF", value)
        }

    var sunriseL: Long
        get() = preferences.getLong("sunriseL", 0L)
        set(value) = preferences.edit {
            it.putLong("sunriseL", value)
        }

    var sunsetL: Long
        get() = preferences.getLong("sunsetL", 0L)
        set(value) = preferences.edit {
            it.putLong("sunsetL", value)
        }

//    var sunsetL: TypeOfSky
//        get() = preferences.all("sunsetL", TypeOfSky.DAY)
//        set(value) = preferences.edit {
//            it.putLong("sunsetL", value)
//        }

    //<<<<<<< HEAD
    var nominalPowerForLightSensor: Int
        
        get() = preferences.getInt("nominal_power_light_sensor", 100)
        
        set(value) = preferences.edit {
            it.putInt("nominal_power_light_sensor", value)
        }


    var coeffForLightSensor: Float // in years
        get() = preferences.getFloat("coeff_calibration_light_sensor", 0.1F)
        set(value) = preferences.edit {
            it.putFloat("coeff_calibration_light_sensor", value)
        }

    var lastStateOfLightSensorCalibrationTool: Int
        get() = preferences.getInt("last_state_calibr_light_sensor", 50)
        set(value) = preferences.edit {
            it.putInt("last_state_calibr_light_sensor", value)
        }

    var lastUpdMoodOfForecast: Int
        get() = preferences.getInt("last_upd_mood", 1)
        set(value) = preferences.edit {
            it.putInt("last_upd_mood", value)
        }

    var lastMood: String
        get() = preferences.getString("last_mood", "☀️").toString()
        set(value) = preferences.edit {
            it.putString("last_mood", value)
        }

    var languageOfApp: String
        get() = preferences.getString("language_app", "en").toString()
        set(value) = preferences.edit {
            it.putString("language_app", value)
        }

}