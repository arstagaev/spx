package com.revolve44.solarpanelx.ui.customviews.daylightmap

import timber.log.Timber
import java.lang.Math.abs

data class latLonXY(val X : Float, val Y : Float)

fun gpsToCoordOnMap(lat : Double, lon : Double, width : Float, height : Float) :  latLonXY{
    var x = 0f
    var y = 0f


    if (lat > 0) {
        y = ( (((90.0-lat) ).toFloat()) / 90f )  * (height / 2f )
    }else {
        y = ( (((90.0-lat) ).toFloat()) / 90f )  * (height / 2f )
    }

    if (lon > 0) {
        x = (width / 2f ) + (  ((lon ).toFloat()) / 180f )  * (width / 2f )
    } else {
        x = ( ((180 + lon ).toFloat()) / 180f )  * (width / 2f )
    }
    Timber.i("xyxy location of RED point on map: x:${x} y:${y}  << lat ${lat}; lon ${lon}")
    return latLonXY(x,y)
}