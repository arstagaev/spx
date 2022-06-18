package com.revolve44.solarpanelx

import com.revolve44.solarpanelx.domain.core.getCurrentTimestampSec
import com.revolve44.solarpanelx.domain.core.unxtoHrAndMinutesByDecimial
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

    @Test
    fun addition_isCorrect3() {
        assertEquals(26.0f, ((260f / 1000f )*100f))
        //assertEquals(26.0, roundTo1decimials((260 / 1000 )*100f))
    }
}