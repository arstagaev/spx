package com.revolve44.solarpanelx.feature_modules.lightsensor

import com.revolve44.solarpanelx.datasource.local.PreferenceMaestro

fun setterForSeekBar(input : Float) : Float{
    if (input<=100){
        return input
    }else{
        return 100F
    }
}

fun setterRealPower(input : Int) : Int{
    if (input<= PreferenceMaestro.nominalPowerForLightSensor){
        return input
    }else{
        return PreferenceMaestro.nominalPowerForLightSensor
    }
}

//fun roundTo1decimials(num : Float) : Float{
//    val df = DecimalFormat("#.#")
//    df.roundingMode = RoundingMode.CEILING
//
//    return (df.format(num)).replace(",",".").toFloat()
//
//}

//fun roundTo2decimials(num : Float) : Float{
//    val df = DecimalFormat("#.##")
//    df.roundingMode = RoundingMode.CEILING
//
//    return (df.format(num)).replace(",",".").toFloat()
//
//}