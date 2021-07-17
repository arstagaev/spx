package com.revolve44.solarpanelx.global_utils

import android.graphics.Bitmap
import com.revolve44.solarpanelx.ui.customviews.daylightmap.WeatherAnim


class Constants {

    companion object{

        @JvmField
        var bitmapMain: Bitmap? = null
        var switcherMap: Boolean = true
        val WIDTH_CUSTOM_VIEW: Float by lazy { WeatherAnim.WIDTH }
        val HEIGHT_CUSTOM_VIEW: Float by lazy { WeatherAnim.HEIGHT }

    }
}