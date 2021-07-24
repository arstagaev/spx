package com.revolve44.solarpanelx

import android.app.Application
import com.revolve44.solarpanelx.datasource.local.PreferenceMaestro
import timber.log.Timber

class SPXApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // init sharedpref
        PreferenceMaestro.init(this)
        // init timber
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Timber.tag("spx")
        }
        //!!!
//        PreferenceMaestro.chosenStationNOMINALPOWER = 100000
//        PreferenceMaestro.lat = 55.7558F
//        PreferenceMaestro.lon = 37.6173F

    }
}