package com.revolve44.solarpanelx.core.extensions


// remove the decimal part
fun Float.roundTo(numFractionDigits: Int): Float {
    val factor = 10.0*numFractionDigits

    //return ((Math.round(this * factor))/ factor).toFloat()
    return "%.${numFractionDigits}f".format(this).toFloat()
}

fun Int.plusString(s: String): String {
    return ("$this $s").toString()

}

fun Float.plusString(s: String): String {
    return ("$this $s").toString()
}