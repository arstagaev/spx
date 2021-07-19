package com.revolve44.solarpanelx.domain.core

import java.text.SimpleDateFormat
import java.util.*


/**
 * Attention, first element of generated timestamp will parse, change time mask carefully!
 */
fun generateTimestampLastUpdate() : String{
    return SimpleDateFormat("HH:mm:ss dd-MM-yyyy").format(Date())
}

fun getCurrentTimestampSec(): Long {

    return (System.currentTimeMillis())/1000

}