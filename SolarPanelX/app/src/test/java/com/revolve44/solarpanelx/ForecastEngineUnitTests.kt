package com.revolve44.solarpanelx

import com.revolve44.solarpanelx.datasource.local.PreferenceMaestro
import com.revolve44.solarpanelx.domain.core.getCurrentTimestampSec
import com.revolve44.solarpanelx.domain.core.getForecast
import org.junit.Assert
import org.junit.Test

class ForecastEngineUnitTests {
    init {
        // init sharedpref
        //PreferenceMaestro.init(this@ForecastEngineUnitTests)
    }

    @Test
    fun forecastEngineTest() {
        //  Friday, November 12, 2021 7:27:32 AM ||cur: Friday, November 12, 2021 2:27:32 PM ||  Friday, November 12, 2021 5:27:32 PM
        /**
         * 1636727252   Friday, November 12, 2021 2:27:32 PM
         *
         * 1636745252   Friday, November 12, 2021 7:27:32 PM
         */
        PreferenceMaestro.chosenStationNOMINALPOWER = 100
        PreferenceMaestro.solarDayDuration = ((1636738052L-1636702052L)/3600).toInt()

        Assert.assertEquals(0, getForecast(0.5,1636702052,1636738052,10800,1636745252))
    }
}