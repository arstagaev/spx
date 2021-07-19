package com.revolve44.solarpanelx.ui.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.lifecycle.*
import com.revolve44.solarpanelx.datasource.SpxRepository
import com.revolve44.solarpanelx.datasource.local.PreferenceMaestro
import com.revolve44.solarpanelx.datasource.model.api.FiveDaysForecastModelParser
import com.revolve44.solarpanelx.datasource.model.db.FirstChartDataTransitor
import com.revolve44.solarpanelx.datasource.model.db.ForecastCell
import com.revolve44.solarpanelx.domain.Resource
import com.revolve44.solarpanelx.domain.core.*
import com.revolve44.solarpanelx.domain.enums.MeasurementSystem
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.io.IOException


class MainViewModel(app: Application, var repoSpx: SpxRepository) : AndroidViewModel(app) {

    var eightForecastingPointsForFirstChart = MutableLiveData<ArrayList<Int>>()
    var eightForecastingPointsForSecondChart = MutableLiveData<ArrayList<Int>>()
    var eightForecastingPointsForThirdChart = MutableLiveData<ArrayList<Int>>()

    // for parsing
    val fiveDaysRequestRes : MutableLiveData<Resource<FiveDaysForecastModelParser>> = MutableLiveData()
    // for send to UI charts
    var allDataForCharts : MutableLiveData<ArrayList<FirstChartDataTransitor>> = MutableLiveData()
    // for display current 3hour forecast
    var forecastNow : MutableLiveData<Int> = MutableLiveData()

    private var allForecastforChart : LiveData<List<ForecastCell>> = repoSpx.getAllForecastCells()




    // Check Internet Connection
    var cm = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    var activeNetwork: NetworkInfo? = cm.activeNetworkInfo
    var isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true


    init {

        // generate DUMMY DATA
        var arr = arrayListOf<FirstChartDataTransitor>(
            FirstChartDataTransitor(arrayListOf("1","1","1","1","1","1","1","1"),
                arrayListOf(1,2,3,4,6,10,2,9)),
        FirstChartDataTransitor(arrayListOf("1","1","1","1","1","1","1","1"),
            arrayListOf(1,2,3,4,6,10,2,9)),
        FirstChartDataTransitor(arrayListOf("1","1","1","1","1","1","1","1"),
            arrayListOf(1,2,3,4,6,10,2,9)))
        allDataForCharts.postValue(arr)


        if (ensureNeedUpdateOrNot()){
            startRequestFor5days()
        }
        fiveDaysRequestRes.postValue(Resource.Init())
    }

    private fun ensureNeedUpdateOrNot() : Boolean {
        Timber.i("~~~~~~~~ init VW  current:${(getCurrentTimestampSec()-3600*3)}  last:${PreferenceMaestro.timeOfLastDataUpdateLong}")
        return PreferenceMaestro.timeOfLastDataUpdateLong == 1234L || (getCurrentTimestampSec()-3600*3)>PreferenceMaestro.timeOfLastDataUpdateLong
    }


    fun manualRequest(){
        //cm = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // check Internet connection
        activeNetwork= cm.activeNetworkInfo
        isConnected = activeNetwork?.isConnectedOrConnecting == true

        startRequestFor5days()
    }

    private fun startRequestFor5days() = viewModelScope.launch {
        safe5daysRequest()
    }


    private suspend fun safe5daysRequest() {
        fiveDaysRequestRes.postValue(Resource.Loading())
        try {
            if (isConnected){
                //checkInternetConnection.postValue()

                Timber.d("start API request")
                val response =  repoSpx.get5daysRequest()
                fiveDaysRequestRes.postValue(handle5daysResponse(response))
                PreferenceMaestro.timeOfLastDataUpdate = "last updated: ${generateTimestampLastUpdate()}"
                PreferenceMaestro.timeOfLastDataUpdateLong = getCurrentTimestampSec()
                //be.postValue(handleBetaResponse(response))

            }else{
                fiveDaysRequestRes.postValue(Resource.Error("NO INTERNET", null))
                Timber.e("NO INTERNET")
                //checkInternetConnection.postValue("NO INTERNET") // i will delete this
            }
        }catch (t: Throwable){
            when(t) {
                is IOException -> fiveDaysRequestRes.postValue(Resource.Error("Network Failure"))
                else -> fiveDaysRequestRes.postValue(Resource.Error("Conversion Error"))
            }
            Timber.e("safeBetaRequest() error: " + t)
        }
    }

    private fun handle5daysResponse(response: Response<FiveDaysForecastModelParser>): Resource<FiveDaysForecastModelParser>? {
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

                    // for mainscreen temp show
                    if (PreferenceMaestro.measurementSys == MeasurementSystem.METRIC.code){
                        PreferenceMaestro.temp = convKelvinToCelsius(resultResponse.list.get(0).main.temp.toFloat())
                    }else{
                        PreferenceMaestro.temp = convKelvinToFahrenheit(resultResponse.list.get(0).main.temp.toFloat())
                    }

                    /** Save Sunshine duration  */
                    PreferenceMaestro.solarDayDuration = ((sunset-sunrise)/3600).toInt()
                    /** Save name of chosen City*/
                    PreferenceMaestro.chosenStationCITY = resultResponse.city.name.toString()
                    /** Save chosen timezone location*/
                    Timber.i("gmt -> " + getTimeZoneGMTstyle(timeZone) + "timezone ->" + timeZone)
                    PreferenceMaestro.chosenTimeZone = getTimeZoneGMTstyle(timeZone)
                    PreferenceMaestro.currentGMTinDefineLocation = unxtoHr(timeZone)

                    //currentHourInForecastLocation = getCurrentTimeInDefineLocation(timeZone)
                    Timber.i("iii ${listx.joinToString()}")
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
                        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
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

    //val repo: LiveData<Repo> = Transformations.switchMap<Any, Repo>(repoIdLiveData,
    //    Function<Any, LiveData<Repo>> { repoId: Any ->
    //        if (repoId.isEmpty()) {
    //            return@switchMap AbsentLiveData.create()
    //        }
    //        repository.loadRepo(repoId)
    //    }
    //)

    fun getForecastForCharts(): MutableLiveData<ArrayList<FirstChartDataTransitor>> {
        var data = repoSpx.getAllForecastCells()

        var forecastArray :ArrayList<Float> = ArrayList()
        var forecastDateArray :ArrayList<Long> = ArrayList()

        data.value?.get(0)?.day.let {
            Timber.i("vvv ${data.value!!.joinToString()}")
            for (i in 0 until data.value!!.size) {

                // need for charts
                forecastArray.add(data.value!!.get(i).forecast.toFloat() * (PreferenceMaestro.calibrationCoeff / 100f))
                forecastDateArray.add(data.value!!.get(i).unixTime)

            }

            allDataForCharts.value!!.add(0, chartDatasortforFirstChart(forecastDateArray, forecastArray))
            allDataForCharts.value!!.add(1, chartDatasort(forecastDateArray, forecastArray, 0))
            allDataForCharts.value!!.add(2, chartDatasort(forecastDateArray, forecastArray, 1))

            return allDataForCharts
        }
    }

    fun getAllForecastForChart() : LiveData<List<ForecastCell>>{

        return allForecastforChart
    }


    private fun saveForecastCell(timestamp: Long, humanTime: String, forecastEnergy: Int) = viewModelScope.launch {
        val forecastCell = ForecastCell(timestamp, 1, humanTime, forecastEnergy)

        repoSpx.saveForecastCell(forecastCell)

    }

    private fun deleteALL_table_ForecastCell() = viewModelScope.launch {

        repoSpx.deleteALL_table_ForecastCell()

    }

}