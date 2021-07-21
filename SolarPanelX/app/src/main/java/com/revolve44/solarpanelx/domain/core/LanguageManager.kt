package com.revolve44.solarpanelx.domain.core

import android.app.Activity
import android.os.Build
import java.util.*

fun setLocale(activity: Activity, languageCode: String) {
    val resources = activity.resources
    val dm = resources.displayMetrics
    val config = resources.configuration
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        config.setLocale(Locale(languageCode.toLowerCase()))
    } else {
        config.locale = Locale(languageCode.toLowerCase())
    }
    resources.updateConfiguration(config, dm)
}

