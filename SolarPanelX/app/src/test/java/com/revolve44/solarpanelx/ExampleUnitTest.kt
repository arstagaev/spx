package com.revolve44.solarpanelx

import com.revolve44.solarpanelx.domain.core.defineTimeOfDay
import com.revolve44.solarpanelx.domain.core.getCurrentTimestampSec
import com.revolve44.solarpanelx.domain.core.unxtoHrAndMinutesByDecimial
import com.revolve44.solarpanelx.domain.enums.TypeOfSky
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(11.9,  unxtoHrAndMinutesByDecimial(getCurrentTimestampSec(),true))
    }

    @Test
    fun addition_isCorrect2() {
        assertEquals(1627116894L,  getCurrentTimestampSec())
    }
}