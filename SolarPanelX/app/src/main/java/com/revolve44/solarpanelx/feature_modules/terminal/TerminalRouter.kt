package com.revolve44.solarpanelx.feature_modules.terminal

import com.revolve44.solarpanelx.datasource.local.PreferenceMaestro


fun terminalRouter(allBody : String, inputCommand : String): String {
    var answer = ""
    //var needReOpenApp = false
    when(inputCommand){
        "check" -> {
            answer =  "All work fine"
        }
        "hi", "hello" -> {
            answer =  "Hello Human!"
        }
        "arsen" -> {
            answer =  "Yep, he is author"
        }
        "standard" -> {
            PreferenceMaestro.isLightMode = false
            answer = "Now is light mode: ${PreferenceMaestro.isLightMode} " +
                    "\n re-open application!"


        }
        "light" -> {
            PreferenceMaestro.isLightMode = true
            answer = "Now is light mode: ${PreferenceMaestro.isLightMode}"+
                    "\n re-open application!"

        }
        "please set pro!!!" -> {
            PreferenceMaestro.isPremiumStatus = true
            answer = "Now is pro version: ${PreferenceMaestro.isPremiumStatus}"+
                    "\n re-open application!"

        }
        "free" -> {
            PreferenceMaestro.isPremiumStatus = false
            answer = "Now is pro version: ${PreferenceMaestro.isPremiumStatus}"+
                    "\n re-open application!"

        }
        else -> answer = "it is no command :( "


    }
    return allBody+ "\n ${answer}"
}

//fun setterOfTextColor() : Int{
//
//    when () {
//
//    }
//
//}