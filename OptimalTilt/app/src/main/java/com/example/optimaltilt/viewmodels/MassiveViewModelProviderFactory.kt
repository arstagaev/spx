package com.example.optimaltilt.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MassiveViewModelProviderFactory(
    val app : Application,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return OrientationSolarPanelViewModel(app) as T
    }
}