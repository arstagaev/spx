package com.revolve44.solarpanelx.ui.lightsensor

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LightSensorViewModel : ViewModel() {

    val lighter = MutableLiveData<Float>()
    val max = MutableLiveData<Int>()
    val min = MutableLiveData<Int>()
}