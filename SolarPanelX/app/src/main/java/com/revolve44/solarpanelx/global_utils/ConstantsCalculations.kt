package com.revolve44.solarpanelx.global_utils

import android.graphics.Bitmap
import com.revolve44.solarpanelx.datasource.local.PreferenceMaestro
import com.revolve44.solarpanelx.global_utils.enums.TypeOfSky
import com.revolve44.solarpanelx.ui.customviews.daylightmap.WeatherAnim


class ConstantsCalculations {

    companion object{

        const val BASE_URL = "https://api.openweathermap.org/"
        const val API_KEY = "ac79fea59e9d15377b787a610a29b784" // main
        const val API_KEY_RESERVE1 = "1ae4e8cac37ab81430d67c851f915a1e" // info@revolna
        const val API_KEY_RESERVE2 = "7b001046a51938cf04e2105d740e93e0" // info@revolna
        const val API_KEY_RESERVE3 = "d43809644aaea9768c1f34941a08f363" // by US atev777
        const val API_KEY_RESERVE4 = "a361206cc1aa27aae898618eb3dc8f0e" // by US atev777
        const val TEST_WRONG_API_KEY = "WRONGac79fea59e9d15377b787a610a29b784"

        const val DUAL_INDICATOR_SIZEOFFACTORS = 32
        const val SOLAR_PANEL_AREA_FOR_1W =0.00648F

        var CURRENT_TIME_OF_DAY = DayPeriod(TypeOfSky.DAY,true)

        @kotlin.jvm.JvmField
        var switcherMap: Boolean = true

        @JvmField
        var bitmapMain : Bitmap? = null
        //var switcherMap: Boolean? = true
        val WIDTH_CUSTOM_VIEW: Float by lazy  { WeatherAnim.WIDTH   }
        val HEIGHT_CUSTOM_VIEW: Float by lazy { WeatherAnim.HEIGHT  }

        var is_TYPE_ROTATION_VECTOR_SELECTED = PreferenceMaestro.isTypeRotationSensor
        var is_COMPASS_WORKING_FINE = false
        val is_LIGHT_MODE = PreferenceMaestro.isLightMode

        var is_PREMIUM = PreferenceMaestro.isPremiumStatus
        // for Notifications
        const val CHANNEL_ID = "SPXNotifChannel_IMPORTANT"
        const val CHANNEL_ID2 = "SPXNotifChannel2_REGULAR"
        const val CHANNEL_IDNum = 1
        const val CHANNEL_ID2Num = 2



    }
}