package com.revolve44.solarpanelx.domain.core

import com.revolve44.solarpanelx.datasource.local.PreferenceMaestro
import timber.log.Timber


fun mainTerminalGate(command :String) : String{

    Timber.i("input to Command ${command}")

    when (command){
        "pro", "premium"->{ return "is Premium: ${PreferenceMaestro.isPremiumStatus}"}
        "please ars set pro version" ->{
            PreferenceMaestro.isPremiumStatus = true
            return "now premium: ${PreferenceMaestro.isPremiumStatus}"
        }
        else -> {return "don`t understand"}
    }
}