package com.example.optimaltilt.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OrientationSolarPanelViewModel(app : Application) : AndroidViewModel(app) {

    var pitch = MutableLiveData<Float>()
    var directionOfSolarPanel = MutableLiveData<Float>()
    var isPanelOrientationViewMode = MutableLiveData<Boolean>()
    var currentItemInViewPager2 = MutableLiveData<Int>()

    var suggestedAzimuthForSolarPanel = MutableLiveData<Int>()
    var suggestedTiltForSolarPanel = MutableLiveData<Int>()


    //val pitch = MutableLiveData<Float>()


    init {
        isPanelOrientationViewMode.value = true
    }
}