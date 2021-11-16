package com.revolve44.solarpanelx.domain.core

import com.revolve44.solarpanelx.datasource.local.PreferenceMaestro
import timber.log.Timber
import kotlin.collections.ArrayList
import com.revolve44.solarpanelx.global_utils.enums.TypeOfSky
import com.revolve44.solarpanelx.global_utils.ConstantsCalculations.Companion.CURRENT_TIME_OF_DAY
import com.revolve44.solarpanelx.global_utils.ConstantsCalculations.Companion.SOLAR_PANEL_AREA_FOR_1W
import com.revolve44.solarpanelx.global_utils.DayPeriod
import java.util.*

//////////////////////////////////////
//        Mars Core ver. 1.4        //
//////////////////////////////////////
/**                                  [seconds]         [seconds]      [seconds]           [seconds?]
 *               CLOUDNESS            SUNRISE           SUNSET        Time Zone      time of current portion forecast
 */
fun getForecast(cloudiness: Double, sunrise : Long, sunset : Long, timeZone: Long, unixTime : Long) : Int {

    var nominalPower : Int = PreferenceMaestro.chosenStationNOMINALPOWER
    var currentTime = unxtoHrAndMinutesByDecimial(unixTime,true)
    var solarPositionFactor : Double = 1.0

    var snrs = PreferenceMaestro.sunriseHour
    var snst = PreferenceMaestro.sunsetHour

//    PreferenceMaestro.sunriseHour = snrs.toFloat()
//    PreferenceMaestro.sunsetHour = snst.toFloat()

    PreferenceMaestro.sunrise = unxtoHoursAndMinutes(sunrise)
    PreferenceMaestro.sunset  = unxtoHoursAndMinutes(sunset)

    //                 for example: sunrise: 8 - frcst.time: 6 -  sunset: 16  TimeZone: 10800


    // is Night or not (Global of method if/else construction)?
    if (currentTime in snrs..snst){

        var diff : Int = (currentTime-snrs).toInt()

        var diff2 = diff.toDouble()/(PreferenceMaestro.solarDayDuration).toDouble()
        /**
        diff2 = (Current Time - Sunrise Time) / Sun day duration

                           6 hr
        for example: (12:00 - 6:00) / 10 hr = 0.6
         */

        when(diff2){
            in 0.1..0.2 -> solarPositionFactor = 0.2
            in 0.2..0.3 -> solarPositionFactor = 0.3
            in 0.3..0.4 -> solarPositionFactor = 0.6
            in 0.4..0.5 -> solarPositionFactor = 0.9

            in 0.5..0.6 -> solarPositionFactor = 1.0
            in 0.6..0.7 -> solarPositionFactor = 1.0

            in 0.7..0.8 -> solarPositionFactor = 0.9
            in 0.8..0.9 -> solarPositionFactor = 0.6
            in 0.9..9.5 -> solarPositionFactor = 0.3
            in 9.5..1.0 -> solarPositionFactor = 0.2
            else ->{ solarPositionFactor = 0.1 }
        }

        /**
         * Here we have a define coeff. with their own weights: (modularity)
         *
         * 1st module. Cloudiness = nominalPower * [specific weight] * cloudiness coeff.
         *
         * 2nd module. Solar Position = nominalPower * [specific weight] * solar position factor
         *
         * 3rd module. Efficiency coefficient
         */
        Timber.i("yyy sunrise: $snrs - frcst.time: $currentTime -  sunset: $snst  TimeZone: $timeZone  isDay: ${currentTime in snrs..snst}" )

        return ((nominalPower
                - nominalPower * 0.5f*(cloudiness/100)
                - nominalPower * 0.2f*solarPositionFactor
                - nominalPower * ((PreferenceMaestro.chosenSolarPanelEfficiency.toFloat()-PreferenceMaestro.chosenSolarPanelInstallationDate.toFloat()*0.2f)/100f)
                ).toInt())


    }else {
        Timber.i("yyy sunrise: $snrs - frcst.time: $currentTime -  sunset: $snst  TimeZone: $timeZone  isDay: ${currentTime in snrs..snst}" )
        return 0
    }
}

fun setForecastForNow(forecastFor20hr: ArrayList<Int>) : Int {


    var firstIndex = 0
    var secondIndex = 0
    var alreadyFindIndexes = false

    for (i in 0 until forecastFor20hr.size){
        if(i == 0 && forecastFor20hr.get(i) != 0 && !alreadyFindIndexes){
            firstIndex = i
            secondIndex = i+1
            alreadyFindIndexes = true
        }

        if(i != 0 && forecastFor20hr.get(i) != 0 && !alreadyFindIndexes){
            secondIndex = i
            firstIndex = i-1
            alreadyFindIndexes = true
            //break
        }
    }

    if (firstIndex < 0 ){
        firstIndex = 0
    }
    //var timeOfDay =  defineTimeOfDay()
    CURRENT_TIME_OF_DAY.typeOfSky = defineTimeOfDay()
    //CURRENT_TIME_OF_DAY = DayPeriod(timeOfDay,false,false) // FIXME

    when (CURRENT_TIME_OF_DAY.typeOfSky){
        TypeOfSky.NIGHT        -> { return  0}
        TypeOfSky.EARLY_MORNING-> { return  (forecastFor20hr.get(firstIndex)+forecastFor20hr.get(secondIndex))/3   }
        TypeOfSky.MORNING      -> { return  (forecastFor20hr.get(firstIndex)+forecastFor20hr.get(secondIndex))/2   }
        TypeOfSky.DAY          -> { return  forecastFor20hr.get(0)                                                 }
        TypeOfSky.EVENING      -> { return  (forecastFor20hr.get(firstIndex)+forecastFor20hr.get(secondIndex))/2   }
        TypeOfSky.LATE_EVENING -> { return  (forecastFor20hr.get(firstIndex)+forecastFor20hr.get(secondIndex))/3   }
    }

    if (forecastFor20hr.get(0) == 0){

    }else {
        return  forecastFor20hr.get(0)
    }
}

fun isWeGetNewTypeOfDay(timeOfDay: TypeOfSky): Boolean {
    return CURRENT_TIME_OF_DAY.typeOfSky != timeOfDay
}

/**
 * to real fit of solar panel
 */
fun toRealFit(fnum : Float) : Float{
    if (fnum <= PreferenceMaestro.chosenStationNOMINALPOWER){
        return fnum
    }else{
        return PreferenceMaestro.chosenStationNOMINALPOWER.toFloat()
    }
}

/**
 * For Statistics Screen
 */
fun getYearlyGeneration() : Int{
    var equatorCoeff : Float = 0.0F
    var latitute = PreferenceMaestro.chosenStationLAT.toInt()
    when(latitute){
        in 90..100 -> equatorCoeff =0.4F
        in 70..80 -> equatorCoeff =0.5F
        in 60..70 -> equatorCoeff =0.5F
        in 50..80 -> equatorCoeff =0.6F
        in 40..60 -> equatorCoeff =0.6F
        in 30..50 -> equatorCoeff =0.7F
        in 20..40 -> equatorCoeff =0.7F
        in 10..20 -> equatorCoeff =0.8F

        in 0..10 -> equatorCoeff =1.0F
        in -10..0 -> equatorCoeff=1.0F

        in -20..-10 -> equatorCoeff=0.8F
        in -30..-20 -> equatorCoeff=0.8F
        in -40..-30 -> equatorCoeff=0.7F
        in -50..-40 -> equatorCoeff=0.6F
        in -60..-50 -> equatorCoeff=0.6F
        in -70..-60 -> equatorCoeff=0.5F
        in -80..-70 -> equatorCoeff=0.4F
        in -90..-80 -> equatorCoeff=0.3F
    }
    var yearlyGeneration : Int = (PreferenceMaestro.chosenStationNOMINALPOWER*4*365*equatorCoeff).toInt()
//    if (yearlyGeneration<1000){
//
//        return "${yearlyGeneration}W"
//
//    }else if (yearlyGeneration<1000000){
//
//        return "${yearlyGeneration/1000}kW"
//
//    }else if (yearlyGeneration>1000000){
//
//        return "${yearlyGeneration/1000000}MW"
//    }else{
//        return "${yearlyGeneration}"
//    }
    return yearlyGeneration

}

fun getSolarRadiationInDefineLocation() : Int{
    var solarRadiation : Int = 0
    var latitute = PreferenceMaestro.chosenStationLAT.toInt()
    when(latitute){
        in 90..100 -> solarRadiation =60
        in 70..80 -> solarRadiation=90
        in 60..70 -> solarRadiation =95
        in 50..80 -> solarRadiation=110
        in 40..60 -> solarRadiation =120
        in 30..50 -> solarRadiation=130
        in 20..40 -> solarRadiation =150
        in 10..20 -> solarRadiation=200
        in 0..10 -> solarRadiation =260
        in -10..0 -> solarRadiation=220
        in -20..-10 -> solarRadiation =200
        in -30..-20 -> solarRadiation=190
        in -40..-30 -> solarRadiation =180
        in -50..-40 -> solarRadiation=170
        in -60..-50 -> solarRadiation =160
        in -70..-60 -> solarRadiation=150
        in -80..-70 -> solarRadiation =140
        in -90..-80 -> solarRadiation=100
    }

    return solarRadiation
}

fun getSolarPanelArea() : Float{
    var solarPanelArea : Float = PreferenceMaestro.chosenStationNOMINALPOWER.toFloat()* SOLAR_PANEL_AREA_FOR_1W

    return solarPanelArea
}

fun getPaybackPeriod() : Float{
    // nominal power divide by 1000 is normal, coz we gonna take a price per 1 kWh
    var paybackPeriod : Float =
        PreferenceMaestro.investmentsToSolarStation/ ((PreferenceMaestro.chosenStationNOMINALPOWER/1000) * (PreferenceMaestro.pricePerkWh) * 5 * 365)
    Timber.i("fun getPaybackPeriod "+paybackPeriod +" = "+" invest is ${PreferenceMaestro.investmentsToSolarStation}"+" chosenStationNOMINALPOWER is "+PreferenceMaestro.chosenStationNOMINALPOWER/1000F+" * "+(PreferenceMaestro.pricePerkWh))
    if (paybackPeriod<60.0F){
        return roundTo2decimials(paybackPeriod)
    }else{
        return -1.0F
    }
}

fun getInvestmentsToPVStation(c: CharSequence?, chosenCurrency: String) : String {
    var pricePer1WattInDefineCurrency : Float = 0F
    var costOfPVStation = 0
    when (chosenCurrency) {
        "$" -> {
            pricePer1WattInDefineCurrency = 1.8F
        }
        "€" -> {
            pricePer1WattInDefineCurrency = 2.4F
        }
        "₹" -> {

            pricePer1WattInDefineCurrency = 50.0F
        }
        "₽" -> {
            pricePer1WattInDefineCurrency = 50.0F
        }
        "SAR" -> {
            pricePer1WattInDefineCurrency = 3.0F
        }
        "£" -> {
            pricePer1WattInDefineCurrency = 3.0F
        }
        "¥" -> {
            pricePer1WattInDefineCurrency = 90.0F // ??? no sure
        }
        else-> pricePer1WattInDefineCurrency = 1.8F // by default dollar
    }
    if (c.toString().isNotEmpty()){
        return (c.toString().toInt() * pricePer1WattInDefineCurrency).toInt().toString()
    }else{
        return (0 * pricePer1WattInDefineCurrency).toInt().toString()
    }


}

fun nowIsDayOrNot(forecastPerPeriod: Float) : Float{
    val rightNow: Calendar = Calendar.getInstance()
    val currentHourIn24Format: Int = rightNow.get(Calendar.HOUR_OF_DAY)
    if (PreferenceMaestro.sunriseHour > PreferenceMaestro.currentGMTinDefineLocation+currentHourIn24Format || PreferenceMaestro.currentGMTinDefineLocation+currentHourIn24Format > PreferenceMaestro.sunsetHour){
        return 0F
    }else{
        return forecastPerPeriod
    }
}

fun defineTimeOfDay() : TypeOfSky {
    var typeOfSky: TypeOfSky = TypeOfSky.DAY

    //var timezone = 0f

    var sunrise : Float = 0f
    var sunset  : Float = 0f

    //timezone = PreferenceMaestro.timezoneL

    sunrise = PreferenceMaestro.sunriseHour//+timezone
    sunset  =  PreferenceMaestro.sunsetHour//+timezone


    //var currentTime = generateCurrentHour().toFloat()
    var a = getCurrentTimestampSec()
    var currentTime = unxtoHrAndMinutesByDecimial(a,true)
    var duration = (sunset - sunrise) / 5  // sunshine duration ~ 5-13 hr. just duration is min 1, max 3

    when(currentTime){
         //    from           ->  to                                                          approximate hours  , and sunshine duration 14 hr for example
        in 0f                 .. sunrise            -> { typeOfSky = TypeOfSky.NIGHT        } //  0-6   hr |  0:00
        in sunrise            .. sunrise+duration*1 -> { typeOfSky = TypeOfSky.EARLY_MORNING} //  6-8   hr |  from sunrise
        in sunrise+duration   .. sunrise+duration*2 -> { typeOfSky = TypeOfSky.MORNING      } //  8-10  hr |
        in sunrise+duration*2 .. sunrise+duration*3 -> { typeOfSky = TypeOfSky.DAY          } //  12-14 hr |
        in sunrise+duration*3 .. sunrise+duration*4 -> { typeOfSky = TypeOfSky.EVENING      } //  14-16 hr |
        in sunrise+duration*4 .. sunset             -> { typeOfSky = TypeOfSky.LATE_EVENING } //  16-20 hr |  to sunset
        in sunset             .. 24f                -> { typeOfSky = TypeOfSky.NIGHT        } //  20-24 hr |  0:00
    }

    Timber.i("time of day current =${currentTime} sunrise=$sunrise and $sunset , timezone ${PreferenceMaestro.chosenTimeZone} | cur timestamp ${getCurrentTimestampSec()} type:${typeOfSky}")

    /**                                       12             18
     *   |__________|_____________|___________|______________|___________|_____________|_______|
     *   |Night      Early Morning   Morning       Day          Evening   Late Evening   Night
     *
     */
    //Timber.i(" ~~~[ ${typeOfSky.name} ] ")


    return typeOfSky
}


//fun unixToHourInteger(timestamp : Long) : Int{
////    val sdf = java.text.SimpleDateFormat("HH")
////    val integerHour : Int = sdf.format(java.util.Date(timestamp * 1000)).toInt()
//
//    val sdf = java.text.SimpleDateFormat("MM-dd HH:mm ZZZZ")
//    sdf.timeZone = TimeZone.getTimeZone(PreferenceMaestro.chosenTimeZone)
//    var str : String = sdf.format(java.util.Date(timestamp * 1000))
//
//
////    val sdf2 = java.text.SimpleDateFormat("MM-dd // HH:mm")
////    val integerHour2 : Int = sdf2.format(java.util.Date(timestamp * 1000)).toInt()
////    Timber.i("ccc = " +integerHour )
////    Timber.d("ccc")
//
//    return hours.toInt()
//}

fun getAverageNumOfArray(array: ArrayList<Float>) : Float {

    return  (array.sum()/array.size).toFloat()

}

fun checkPercent(watts : Int) : String{
    Timber.i("nnn wats:"+watts+" pref nominal: "+PreferenceMaestro.chosenStationNOMINALPOWER)
    return when((watts.toFloat() / (PreferenceMaestro.chosenStationNOMINALPOWER).toFloat() )*100f){
        in 50f..100f ->{
            "☀️"
        }
        in 14f..50f -> {
            "⛅"
        }
        in 1f..13f ->  {
            "☁️"
        }
        0f ->          {
            "\uD83C\uDF19" // night
        }
        else -> {
            "\uD83C\uDF0D" // earth
        }
    }


}