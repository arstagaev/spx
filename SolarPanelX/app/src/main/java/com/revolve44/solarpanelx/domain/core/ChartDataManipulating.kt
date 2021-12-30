package com.revolve44.solarpanelx.domain.core

import android.util.Log
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.revolve44.solarpanelx.datasource.local.PreferenceMaestro
import com.revolve44.solarpanelx.datasource.models.db.FirstChartDataTransitor
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList


fun chartDataHandler(arrayList: ArrayList<Float>, rangeX0 : Int, rangeX1 : Int): ArrayList<Float> {
    var arrayListOUTPUT : ArrayList<Float> = ArrayList()
    for (i in (arrayList.size*rangeX0)/5+1..(arrayList.size*rangeX1)/5){
        arrayListOUTPUT.add(arrayList.get(i))

    }

    Timber.i("vvv* $arrayListOUTPUT")

    return arrayListOUTPUT
}

//forecastDataset: ArrayList<Float>,
/**
 * ->input:
 * timestamps[],  forecasts[], num (num is number of chart which we fill our array on output)
 *
 * <-output:
 * right sorted part of array forecasts[] for define special chart
 */
fun chartDatasort( timestamps: ArrayList<Long>, forecasts: ArrayList<Float>,jumpAboveArray : Int): FirstChartDataTransitor{
    //var repeater = 0
    var arrayListOUTPUT: ArrayList<Int> = ArrayList()
    //xxx
    //var jumpAboveArray = 0

    var countdown = (8-(unxtoHr(timestamps.get(0))/3)) + 8*jumpAboveArray

    for (i in countdown..countdown+7){
        //for (z in countdown..timestamps.)
        arrayListOUTPUT.add(forecasts.get(i).toInt())

    }
    // for description of charts
    if (jumpAboveArray==0){
        PreferenceMaestro.leftChartMonthandDay = unxtoDayandMonth(timestamps.get(countdown+1))
    }else if (jumpAboveArray==1){
        PreferenceMaestro.rightChartMonthandDay = unxtoDayandMonth(timestamps.get(countdown+1))
    }else if (jumpAboveArray==2){
        PreferenceMaestro.fourChartMonthandDay = unxtoDayandMonth(timestamps.get(countdown+1))
    }else if (jumpAboveArray==3){
        PreferenceMaestro.fiveChartMonthandDay = unxtoDayandMonth(timestamps.get(countdown+1))
    }
    var arrayDate = arrayListOf<String>()
    for (i in 0 until arrayListOUTPUT.size){
        arrayDate.add("${i}")
    }

    return FirstChartDataTransitor(arrayDate,arrayListOUTPUT)
}

fun chartDatasortforFirstChart( timestamps: ArrayList<Long>, forecasts: ArrayList<Float>) : FirstChartDataTransitor {
    //var repeater = 0
    var dateOUTPUT: ArrayList<String> = ArrayList()
    var forecastOUTPUT: ArrayList<Int> = ArrayList()
    //xxx
    //var jumpAboveArray = 0

    //var countdown = (8-(unxtoHr(timestamps.get(0))/3)) + 8*jumpAboveArray

    for (i in 0..7){

        var hour = unxtoHr(timestamps.get(i)).toInt()

        Timber.i("unx1 ${hour}")
        if (hour == 0 ){
            Timber.i("qqq "+ unxtoDate(timestamps.get(i)))
            dateOUTPUT.add(unxtoDate(timestamps.get(i)))

        }else{
            dateOUTPUT.add("$hour"+"hr")
        }

        forecastOUTPUT.add((forecasts.get(i)).toInt())
//        dateOUTPUT.add()timestamps.get(i)
//
//        //for (z in countdown..timestamps.)
//        arrayListOUTPUT.add(forecasts.get(i))

    }
    var magicContainer = FirstChartDataTransitor(dateOUTPUT,forecastOUTPUT)
    return magicContainer
}

/** Unix timestamp to hour INTEGER format */

fun unxtoHr(timestamp : Long): Int {
    //SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//    val sdf = java.text.SimpleDateFormat("HH")
//    return (sdf.format(java.util.Date(timestamp * 1000))).toInt()

    val sdf = java.text.SimpleDateFormat("HH", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone(PreferenceMaestro.chosenTimeZone)
    return sdf.format(java.util.Date(timestamp * 1000)).toInt()
}

fun unxtoHrAndMinutesByDecimial(timestampInput : Long, autoSetTimezone : Boolean): Float {
    //SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//    val sdf = java.text.SimpleDateFormat("HH")
//    return (sdf.format(java.util.Date(timestamp * 1000))).toInt()
    var timestamp = timestampInput

    when(timestamp.length() ){
        10 -> timestamp *= 1000
        11 -> timestamp *= 100
        12 -> timestamp *= 10
    }

    // time api android => work only with millisecond
    val hourRaw = java.text.SimpleDateFormat("HH", Locale.getDefault())
    val minRaw = java.text.SimpleDateFormat("mm", Locale.getDefault())


    //if (autoSetTimezone){
    //hourRaw.timeZone = TimeZone.getTimeZone("GMT+3:00")
    //minRaw.timeZone = TimeZone.getTimeZone ("GMT+3:00")

    hourRaw.timeZone = TimeZone.getTimeZone(PreferenceMaestro.chosenTimeZone)
    minRaw.timeZone =  TimeZone.getTimeZone(PreferenceMaestro.chosenTimeZone)
   // }
    //Timber.i("time of day pizdec ${hourRaw.toPattern()} ${minRaw.toPattern()} ")
    var minOutput  =  (minRaw.format(java.util.Date(timestamp))).toFloat() / 60
    var hourOutput =  hourRaw.format(java.util.Date(timestamp)).toFloat()

    return hourOutput+minOutput
}

/** Unix timestamp to date STRING format */

fun unxtoDate(timestamp: Long): String {
//    val sdf = java.text.SimpleDateFormat("HH dd-MMM ")
//    return (sdf.format(java.util.Date(timestamp * 1000)))

    val sdf = java.text.SimpleDateFormat("MM-dd HH:mm", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone(PreferenceMaestro.chosenTimeZone)
    return sdf.format(java.util.Date(timestamp * 1000))
}

/** Unix timestamp to day-month STRING format */

fun unxtoDayandMonth(timestamp: Long): String {
//    val sdf = java.text.SimpleDateFormat("dd MMM ")
//    return (sdf.format(java.util.Date(timestamp * 1000)))
    val sdf = java.text.SimpleDateFormat("MMM-dd", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone(PreferenceMaestro.chosenTimeZone)
    return sdf.format(java.util.Date(timestamp * 1000))
}

fun unxtoHoursAndMinutes(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("HH:mm", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone(PreferenceMaestro.chosenTimeZone)
    return sdf.format(java.util.Date(timestamp * 1000))

//    var days = timestamp / 86400
//    var hoursInSec = timestamp - (days * 86400)
//    var hours = hoursInSec / 3600
//    var minutesInSec =hoursInSec - (hours*3600)
//    var minutes = minutesInSec / 60

    //return "${hours}:${minutes}"
}

class MyXAxisValuesFormatter_1 : IAxisValueFormatter {
    private var values : ArrayList<String>

    constructor(values: ArrayList<String>){
        this.values = values
    }
    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
        if (value < values.size) {
            return values[value.toInt()]
        }else {
            return values[values.size-1]
        }

    }
}


class MyXAxisValuesFormatter : IAxisValueFormatter {
    private var values : ArrayList<String>

    constructor(values: ArrayList<String>){
        this.values = values
    }
    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
        if (value < values.size) {
            return values[value.toInt()]
        }else {
            return values[values.size-1]
        }
    }
}


/** for debugging purposes */

fun unxtoDateDEBUG(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("dd-MMM-yyyy / HH:mm:ss")
    return (sdf.format(java.util.Date(timestamp * 1000)))
}

fun sumOfCharts(inputArray: ArrayList<Int>) : Int{
    try {
        //var preInputArray = inputArray

//        for (i in inputArray.indices){
//            if (inputArray.get(i) == 0.0F){
//                inputArray.removeAt(i)
//            }
//        }
        var PreoutputArray = arrayListOf<Int>()

        for (i in 0 .. inputArray.size-2){
            var a = arrayListOf<Int>()

            a.add(inputArray.get(i))
            a.add(inputArray.get(i+1))

            PreoutputArray.add(a.average().toInt())

            a.clear()

        }

        Log.i("ccc","ly "+PreoutputArray.toString()+" // "+inputArray.toString())

        return PreoutputArray.sum()*2+inputArray.sum().toInt()
    }catch (e : Exception){
        Timber.e("Inside chart error: "+e.message)
        return -1
    }
}
data class ChartParser (var arrEnt :ArrayList<Entry>, var xAxisTimes : ArrayList<String>)
fun smoothLineOfChart(
    //firstHr : Int,
    yPointsEnergy: ArrayList<Int>,
    xAxisTimes_1: ArrayList<String>
) : ChartParser {

    var pairsTimeEnergy = ArrayList<Entry>()
    var pairsTimes= ArrayList<String>()

    var FINAL_Energy = ArrayList<Entry>()
    var FINALxAxisTimes_1 = ArrayList<String>()

    FINAL_Energy.add(Entry(0f, 0f)) //////!!!!!!!!!!!!!!!!!!
    FINALxAxisTimes_1.add(PreferenceMaestro.sunrise)


    var interMed     = 0.3333f // need multiply by hour
    var asdf = (PreferenceMaestro.solarDayDuration / 3f) //* interMed

//    var sunriseCoeff      =  roundTo2decimials(PreferenceMaestro.sunriseHour / 3f).toString().takeLast(2).toFloat()/ 100f
//    var sunsetCoeff       =  roundTo2decimials(PreferenceMaestro.sunsetHour / 3f).toString() .takeLast(2).toFloat()/ 100f

       // sunsetCoeff       =
    Timber.i("~~~~ yPointsEnergySize: ${yPointsEnergy.joinToString()} ")
    Timber.i("~~~~ yPointsEnergySize: ${yPointsEnergy.size} xAxisTimes_1S:${pairsTimes.size}")
    for ( i in 0 until yPointsEnergy.size) {

        if (yPointsEnergy[i].toFloat() != 0f) {
            pairsTimeEnergy.add(Entry(i.toFloat(), yPointsEnergy[i].toFloat()))
            pairsTimes.add(xAxisTimes_1[i])
        }

    }
    Timber.i("~~~~ pairstimeenergy: ${pairsTimeEnergy.joinToString()} ")
    for (i in 0 until pairsTimeEnergy.size) {

        //Timber.i(">>>> sunrise ${sunriseCoeff} |  sunset${sunsetCoeff}")
        FINAL_Energy.add(Entry(i.toFloat()+1f, pairsTimeEnergy[i].y))
        FINALxAxisTimes_1.add(pairsTimes[i])

    }
    FINAL_Energy.add(Entry(FINAL_Energy.size.toFloat(), 0f))
    FINALxAxisTimes_1.add(PreferenceMaestro.sunset)


    Timber.i("~~~~!!!pizdec "+FINALxAxisTimes_1.joinToString() +"FINALxAxisTimes_1: ${FINALxAxisTimes_1.size} ")
    Timber.i("SMOOTH ~~~>>>> output>>> ${FINAL_Energy.joinToString()}  FINALpairsTimeEnergy: ${FINAL_Energy.size}")
    return ChartParser(FINAL_Energy,FINALxAxisTimes_1)
}
//if (pairsTimeEnergy[i].y == 0f && pairsTimeEnergy[i+1].y > 0f) {
//
//                pairsTimeEnergy[i]  .x  = pairsTimeEnergy[i].x + (1f-sunriseCoeff)
//                pairsTimeEnergy[i+1].x = pairsTimeEnergy[i+1].x + 0.5f
//                //asd = pairsTimeEnergy[i+1].x + PreferenceMaestro.solarDayDuration / 3f
//
//            }
//            else if (pairsTimeEnergy[i].y > 0f && pairsTimeEnergy[i+1].y == 0f) {
//
//                pairsTimeEnergy[i+1].x = pairsTimeEnergy[i+1].x - (1f - sunsetCoeff)
//                //pairsTimeEnergy[i].x = pairsTimeEnergy[i].x - 0.5f
//            }
//            else if (pairsTimeEnergy[i].y > 0f && pairsTimeEnergy[i+1].y > 0f) {
//
//                //pairsTimeEnergy[i].y = pairsTimeEnergy[i].y + asdf
//                //asd = pairsTimeEnergy[i].y
//            }
// make compare with sunset and sunrise
fun makeChartLineSmoothAndCompare(arrayData: ArrayList<Int>): ArrayList<Entry> {
    Timber.i("SMOOTH arrayData input>>> ${arrayData.joinToString()}")
    var yValues = ArrayList<Entry>()
    var alreadyNotZeroSUNRISE = false
    var alreadyNotZeroSUNSET = true
    try {

        for (i in 0 until arrayData.size){
            // (number of order, number of value  )
            if (!alreadyNotZeroSUNRISE) {

                if (!alreadyNotZeroSUNRISE && arrayData.get(i) == 0 && arrayData.get(i+1) > 0 ) {

                    yValues.add( Entry(PreferenceMaestro.solarDayDuration / 3f, arrayData.get(i).toFloat()) )

                    alreadyNotZeroSUNRISE = true
                }else {

                    yValues.add(Entry(i.toFloat(), arrayData.get(i).toFloat()))

                }

            }else {

                if (alreadyNotZeroSUNSET && arrayData.get(i) == 0 && arrayData.get(i+1) == 0 ) {

                    yValues.add( Entry(PreferenceMaestro.sunsetHour / 3f, arrayData.get(i).toFloat()) )

                    alreadyNotZeroSUNSET = false
                }else {

                    yValues.add(Entry(i.toFloat(), arrayData.get(i).toFloat()))

                }

            }
        }

    }catch (e: Exception) {
        Timber.e("ERROR in charts on MainScreen  !!!vvv : ${e.message}")
        Timber.e("ERROR in charts on MainScreen  !!!vvv : ${e.message}")
        Timber.e("ERROR in charts on MainScreen  !!!vvv : ${e.message}")

        yValues.clear()

        for (i in 0..arrayData.size-1){
            ///////////////   scale                energy
            yValues.add(Entry(i.toFloat(), arrayData.get(i).toFloat()))
        }

    }
    Timber.i("SMOOTH arrayData output<<< ${yValues.joinToString()}")

    return yValues
}

