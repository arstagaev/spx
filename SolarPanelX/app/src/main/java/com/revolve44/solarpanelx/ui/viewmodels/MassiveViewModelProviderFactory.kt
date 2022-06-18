package com.revolve44.solarpanelx.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.revolve44.solarpanelx.datasource.SpxRepository

class MassiveViewModelProviderFactory(
    val app : Application,
    val repository: SpxRepository
) : ViewModelProvider.Factory {
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        return MainViewModel(app,repository) as T
//    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(app,repository) as T
    }
}