package com.revolve44.solarpanelx.domain.core

import android.util.Log
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.revolve44.solarpanelx.datasource.local.PreferenceMaestro
import com.revolve44.solarpanelx.datasource.model.db.FirstChartDataTransitor
import timber.log.Timber
import java.text.SimpleDateFormat
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

class MyXAxisValuesFormatter : IAxisValueFormatter {
    private var values : ArrayList<String>
    constructor(values: ArrayList<String>){
        this.values = values
    }
    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
        return values[value.toInt()]
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

