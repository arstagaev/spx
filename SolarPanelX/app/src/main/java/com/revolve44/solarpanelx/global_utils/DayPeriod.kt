package com.revolve44.solarpanelx.global_utils

import com.revolve44.solarpanelx.global_utils.enums.TypeOfSky

data class DayPeriod(
    var typeOfSky: TypeOfSky,
    var isChangeColorAlreadyHappen : Boolean // for change color dont happen 100500 times
)
