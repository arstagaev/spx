package com.revolve44.solarpanelx.ui.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.net.NetworkInfo
import android.os.Build
import androidx.lifecycle.*

import com.revolve44.solarpanelx.datasource.SpxRepository
import com.revolve44.solarpanelx.datasource.local.PreferenceMaestro
import com.revolve44.solarpanelx.datasource.model.db.FirstChartDataTransitor
import com.revolve44.solarpanelx.domain.Resource
//import com.revolve44.solarpanelsx1.core.Converters
import com.revolve44.solarpanelx.datasource.model.db.ForecastCell
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class MainViewModel() : ViewModel() {

    var eightForecastingPointsForFirstChart = MutableLiveData<ArrayList<Int>>()
    var eightForecastingPointsForSecondChart = MutableLiveData<ArrayList<Int>>()
    var eightForecastingPointsForThirdChart = MutableLiveData<ArrayList<Int>>()

    var allDataForCharts : MutableLiveData<ArrayList<FirstChartDataTransitor>> = MutableLiveData()

    init {
        var arr = arrayListOf<FirstChartDataTransitor>(FirstChartDataTransitor(
            arrayListOf("1","1","1","1","1","1","1","1"),
            arrayListOf(1,2,3,4,6,10,2,9)))
        allDataForCharts.postValue(arr)
    }

}