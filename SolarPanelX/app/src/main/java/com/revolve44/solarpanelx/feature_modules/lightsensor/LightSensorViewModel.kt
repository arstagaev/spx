package com.revolve44.solarpanelx.feature_modules.lightsensor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LightSensorViewModel : ViewModel() {

    val lighter = MutableLiveData<Float>()
    val max = MutableLiveData<Int>()
    val min = MutableLiveData<Int>()
}