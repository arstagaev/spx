package com.revolve44.solarpanelx.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.revolve44.solarpanelx.repository.SolarRepository
import com.revolve44.solarpanelx.ui.viewmodels.MainViewModel

class MassiveViewModelProviderFactory(
    val app : Application,
    val repository: SolarRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(app,repository) as T
    }
}