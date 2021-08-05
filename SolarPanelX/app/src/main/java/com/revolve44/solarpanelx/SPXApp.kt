package com.revolve44.solarpanelx

import android.app.Application
import cat.ereza.customactivityoncrash.config.CaocConfig
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

        CaocConfig.Builder.create()
            .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
            //.enabled(false) //default: true
            //.showErrorDetails(false) //default: true
            //.showRestartButton(false) //default: true
            //.logErrorOnRestart(false) //default: true
            //.trackActivities(true) //default: false
            //.minTimeBetweenCrashesMs(2000) //default: 3000
            //.errorDrawable(R.drawable.ic_custom_drawable) //default: bug image
            //.restartActivity(YourCustomActivity::class.java) //default: null (your app's launch activity)
            //.errorActivity( CrashActivity::class.java) //default: null (default error activity)
            //.eventListener(null) //default: null
            .apply()

        //!!!
//        PreferenceMaestro.chosenStationNOMINALPOWER = 100000
//        PreferenceMaestro.lat = 55.7558F
//        PreferenceMaestro.lon = 37.6173F

    }
}