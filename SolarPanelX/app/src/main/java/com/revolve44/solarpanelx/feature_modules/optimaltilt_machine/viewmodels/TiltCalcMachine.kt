package com.revolve44.solarpanelx.feature_modules.optimaltilt_machine.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TiltCalcMachine(
    val app : Application,
) : ViewModelProvider.Factory {
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//
//    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OrientationSolarPanelViewModel(app) as T
    }
}