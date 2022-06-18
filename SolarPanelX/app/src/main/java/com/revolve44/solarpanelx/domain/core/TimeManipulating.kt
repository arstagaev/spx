package com.revolve44.solarpanelx.domain.core

import com.revolve44.solarpanelx.datasource.local.PreferenceMaestro
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


/**
 * Attention, first element of generated timestamp will parse, change time mask carefully!
 */
fun generateTimestampLastUpdate() : String{
    return SimpleDateFormat("HH:mm:ss dd-MM-yyyy").format(Date())
}

fun generateCurrentHour() : String{
    return SimpleDateFormat("HH").format(Date())
}

fun getCurrentTimestampSec(): Long {

    return ( (System.currentTimeMillis())/1000 )

}

fun getCurrentDayOfYear(): Int {
    return Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
}

fun ensureNeedUpdateOrNot_PeriodTwoDays() : Boolean {
    Timber.i("~~~~~~~~ init VW  current:${(getCurrentTimestampSec()-86400*2)}  last:${PreferenceMaestro.timeOfLastDataUpdateLong}")
    Timber.i("ensure to upd ${getCurrentTimestampSec()-86400*2} ? ${PreferenceMaestro.timeOfLastDataUpdateLong}  ~~  ${(getCurrentTimestampSec()-86400*2)> PreferenceMaestro.timeOfLastDataUpdateLong}")
    if (PreferenceMaestro.timeOfLastDataUpdateLong == 1234L){
        return true
    }
    if ((getCurrentTimestampSec()-86400*2)> PreferenceMaestro.timeOfLastDataUpdateLong){
        return true
    }
    return false
}

fun getTimeHuman(GMT : String): String {
    val dateFormatGmt = SimpleDateFormat("HH:mm:ss  dd/MM/yyyy")
    dateFormatGmt.timeZone = TimeZone.getTimeZone(GMT)
    return dateFormatGmt.format(Date())
}