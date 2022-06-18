package com.revolve44.solarpanelx.feature_modules.optimaltilt_machine.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class OrientationSolarPanelViewModel(app : Application) : AndroidViewModel(app) {

    var pitchRotationVector = MutableLiveData<Float>()
    var pitchAccelerometer = MutableLiveData<Float>()

    var azimuthDirectionOfSolarPanel = MutableLiveData<Float>()

    var isPanelOrientationViewMode = MutableLiveData<Boolean>()
    var currentItemInViewPager2 = MutableLiveData<Int>()

    var suggestedAzimuthForSolarPanel = MutableLiveData<Int>()
    var suggestedTiltForSolarPanel = MutableLiveData<Int>()


    //val pitch = MutableLiveData<Float>()


    init {
        isPanelOrientationViewMode.value = true
    }
}