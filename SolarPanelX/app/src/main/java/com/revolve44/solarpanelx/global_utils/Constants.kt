package com.revolve44.solarpanelx.global_utils

import android.graphics.Bitmap
import com.revolve44.solarpanelx.ui.customviews.daylightmap.WeatherAnim


class Constants {

    companion object{

        const val BASE_URL = "https://api.openweathermap.org/"
        const val API_KEY = "ac79fea59e9d15377b787a610a29b784"

        const val DUAL_INDICATOR_SIZEOFFACTORS = 32
        const val SOLAR_PANEL_AREA_FOR_1W =0.00648F

        @kotlin.jvm.JvmField
        var switcherMap: Boolean = true

        @JvmField
        var bitmapMain : Bitmap? = null
        //var switcherMap: Boolean? = true
        val WIDTH_CUSTOM_VIEW: Float by lazy { WeatherAnim.WIDTH }
        val HEIGHT_CUSTOM_VIEW: Float by lazy { WeatherAnim.HEIGHT }

    }
}