package com.revolve44.solarpanelx.ui.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.net.NetworkInfo
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.revolve44.postmakermassive.models.alfa.CurrentForecastMain
import com.revolve44.postmakermassive.models.beta.FiveDaysForecastMain
import com.revolve44.solarpanelx.SolarApp
import com.revolve44.solarpanelx.core.*
import com.revolve44.solarpanelx.datasource.SpxRepository
import com.revolve44.solarpanelx.datasource.local.PreferenceMaestro
import com.revolve44.solarpanelx.global_utils.Resource
//import com.revolve44.solarpanelsx1.core.Converters
import com.revolve44.solarpanelx.models.for_storage.ForecastCell
import com.revolve44.solarpanelx.repository.SolarRepository
import com.revolve44.solarpanelx.storage.PreferenceMaestro
import com.revolve44.solarpanelx.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class MainViewModel(app: Application, val mrepository: SpxRepository) :AndroidViewModel(app) {

    private val alphaRequest : MutableLiveData<Resource<CurrentForecastMain>> = MutableLiveData()
    val fiveDaysRequest : MutableLiveData<Resource<FiveDaysForecastMain>> = MutableLiveData()
    val forecastforChart : MutableLiveData<Resource<FiveDaysForecastMain>> = MutableLiveData()
    var forecastPowerPerHour = MutableLiveData<Float>()
    var forecastPowerPerWeek = MutableLiveData<Float>()
    var forecastNow = MutableLiveData<Float>()

    var cm = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    var activeNetwork: NetworkInfo? = cm.activeNetworkInfo
    var isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

    //var currentUTCTime = MutableLiveData<Long>()


    var dataHasBeenChanged = MutableLiveData<Boolean>()

    //private val cnvrt : Converters = Converters()

    private var allForecastforChart : LiveData<List<ForecastCell>> = mrepository.getAllForecastCells()
    var checkInternetConnection = MutableLiveData<String>()

    var currentHourInForecastLocation = MutableLiveData<Int>()

    var showHelpLayout = MutableLiveData<Boolean>()
    var percentGeneration = MutableLiveData<Int>()

    override fun onCleared() {
        super.onCleared()
        Timber.i("lifecycle viewmodel cleared")
    }


    init {


        startRequestFor5days()
        Timber.i("init viewModel")
        showHelpLayout.value = false
    }

    fun manualRequest(){
        //cm = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        activeNetwork= cm.activeNetworkInfo
        isConnected = activeNetwork?.isConnectedOrConnecting == true

        startRequestFor5days()
    }


    private fun startAlphaRequest() = viewModelScope.launch {
         safeAplhaRequest()
    }

    private fun startRequestFor5days() = viewModelScope.launch {
        safe5daysRequest()
    }

    private suspend fun safeAplhaRequest() {
        alphaRequest.postValue(Resource.Loading())

        try {
            if (isConnected){
                Timber.d("start Alpha request")
                val response = mrepository.getAlpha()
                alphaRequest.postValue(handleAlphaResponse(response))
            }
        }catch (e: Exception){
            Timber.e("A " + e)
        }

    }

    private suspend fun safe5daysRequest() {
        fiveDaysRequest.postValue(Resource.Loading())
        try {
            if (isConnected){
                //checkInternetConnection.postValue()

                Timber.d("start API request")
                val response =  mrepository.get5daysRequest()
                fiveDaysRequest.postValue(handle5daysResponse(response))
                //be.postValue(handleBetaResponse(response))
            }else{
                fiveDaysRequest.postValue(Resource.Error("NO INTERNET", null))
                Timber.e("NO INTERNET")
                checkInternetConnection.postValue("NO INTERNET") // i will delete this
            }
        }catch (t: Throwable){
            when(t) {
                is IOException -> fiveDaysRequest.postValue(Resource.Error("Network Failure"))
                else -> fiveDaysRequest.postValue(Resource.Error("Conversion Error"))
            }
            Timber.e("safeBetaRequest() error: " + t)
        }
    }

    private fun handleAlphaResponse(response: Response<CurrentForecastMain>): Resource<CurrentForecastMain>? {
        //catchresponse
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                try {
                    Timber.d("answer A: " + resultResponse.clouds)
                    //Log.d("pizdec ",resultResponse.name+" //")
                }catch (e: Exception){
                    Timber.e("asd Error in ViewModel: " + e)
                }
            }
        }else{
            Timber.e("asd Response is NOT successful" + response.message())
        }
        return Resource.Error(response.message())
    }

    private fun handle5daysResponse(response: Response<FiveDaysForecastMain>): Resource<FiveDaysForecastMain>? {
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                try {
                    //var arrayx = ArrayList<Double>()

                    Timber.i("answer B: " + resultResponse.list)
                    val listx = resultResponse.list

                    var cloudcoeff = 0.0
                    var timestamp : Long = 0
                    var sunrise : Long = 0
                    var sunset : Long = 0
                    var timeZone : Long = 0

                    deleteALL_table_ForecastCell()
                    sunrise = resultResponse.city.sunrise.toLong()
                    sunset = resultResponse.city.sunset.toLong()
                    timeZone = resultResponse.city.timezone.toLong()

                    PreferenceMaestro.sunriseL = sunrise
                    PreferenceMaestro.sunsetL = sunset
                    PreferenceMaestro.timezoneL = timeZone

                    /** Save Sunshine duration  */
                    PreferenceMaestro.solarDayDuration = ((sunset-sunrise)/3600).toInt()
                    /** Save name of chosen City*/
                    PreferenceMaestro.chosenStationCITY = resultResponse.city.name.toString()
                    /** Save chosen timezone location*/
                    Timber.i("gmt -> " + getTimeZoneGMTstyle(timeZone) + "timezone ->" + timeZone)
                    PreferenceMaestro.chosenTimeZone = getTimeZoneGMTstyle(timeZone)
                    PreferenceMaestro.currentGMTinDefineLocation = unxtoHr(timeZone)

                    //currentHourInForecastLocation = getCurrentTimeInDefineLocation(timeZone)

                    var arrayX : ArrayList<Long> = ArrayList()

                    for (i in listx){
                        timestamp = i.dt.toLong()
                        cloudcoeff = i.clouds.all.toDouble()

                        /**
                         * save in db
                         *
                         *  @param timestamp
                         *  @param humantime
                         *  @param forecast
                         *
                         */
                        saveForecastCell(
                            timestamp, unixtoHumanTime(timestamp),
                            getForecast(cloudcoeff, sunrise, sunset, timeZone, timestamp)
                        )

                        arrayX.add(timestamp + timeZone)

                    }
                }catch (e: Exception){
                    Timber.e("beta error: " + e)
                }
                return Resource.Success(resultResponse)
            }
        }else{
            Timber.e("not successful " + response.message())
        }
        return Resource.Error(response.message())
    }

    private fun deleteALL_table_ForecastCell() = viewModelScope.launch {
        mrepository.deleteALL_table_ForecastCell()
    }

    private fun saveForecastCell(timestamp: Long, humanTime: String, forecastEnergy: Int) = viewModelScope.launch {
        val forecastCell = ForecastCell(timestamp, 1, humanTime, forecastEnergy)

        mrepository.saveForecastCell(forecastCell)
    }

    fun getAllForecastForChart() : LiveData<List<ForecastCell>>{

        return allForecastforChart
    }




    // check internet connection
    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<SolarApp>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}