package com.revolve44.solarpanelx.ui.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.CountDownTimer
import androidx.lifecycle.*
import com.revolve44.solarpanelx.datasource.SpxRepository
import com.revolve44.solarpanelx.datasource.local.PreferenceMaestro
import com.revolve44.solarpanelx.datasource.models.api.FiveDaysForecastModelParser
import com.revolve44.solarpanelx.datasource.models.db.FirstChartDataTransitor
import com.revolve44.solarpanelx.datasource.models.db.ForecastCell
import com.revolve44.solarpanelx.domain.Resource
import com.revolve44.solarpanelx.domain.core.*
import com.revolve44.solarpanelx.domain.enums.MeasurementSystem
import com.revolve44.solarpanelx.global_utils.ConstantsCalculations.Companion.API_KEY
import com.revolve44.solarpanelx.global_utils.ConstantsCalculations.Companion.API_KEY_RESERVE1
import com.revolve44.solarpanelx.global_utils.ConstantsCalculations.Companion.API_KEY_RESERVE2
import com.revolve44.solarpanelx.global_utils.ConstantsCalculations.Companion.API_KEY_RESERVE3
import com.revolve44.solarpanelx.global_utils.ConstantsCalculations.Companion.API_KEY_RESERVE4
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.io.IOException


class MainViewModel(app: Application, var repoSpx: SpxRepository) : AndroidViewModel(app) {

    var eightForecastingPointsForFirstChart  = MutableLiveData<ArrayList<Int>>()
    var eightForecastingPointsForSecondChart = MutableLiveData<ArrayList<Int>>()
    var eightForecastingPointsForThirdChart  = MutableLiveData<ArrayList<Int>>()

    // for parsing
    val fiveDaysRequestRes : MutableLiveData<Resource<FiveDaysForecastModelParser>> = MutableLiveData()
    // for send to UI charts
    var allDataForCharts : MutableLiveData<ArrayList<FirstChartDataTransitor>> = MutableLiveData()
    // for display current 3hour forecast
    var forecastNow : MutableLiveData<Int> = MutableLiveData()
    var timeNow : MutableLiveData<String> = MutableLiveData()
    var financeM1Now : MutableLiveData<String> = MutableLiveData()

    private var allForecastforChart : LiveData<List<ForecastCell>> = repoSpx.getAllForecastCells()

    var cm = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    var isConnected: Boolean = false

    private fun checkConnection(){

        var activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        isConnected = activeNetwork?.isConnectedOrConnecting == true
    }
    // Check Internet Connection




    init {

        // generate DUMMY DATA
//        var arr = arrayListOf<FirstChartDataTransitor>(
//            FirstChartDataTransitor(arrayListOf("1","1","1","1","1","1","1","1"),
//                arrayListOf(1,2,3,4,6,10,2,9)),
//        FirstChartDataTransitor(arrayListOf("1","1","1","1","1","1","1","1"),
//            arrayListOf(1,2,3,4,6,10,2,9)),
//        FirstChartDataTransitor(arrayListOf("1","1","1","1","1","1","1","1"),
//            arrayListOf(1,2,3,4,6,10,2,9)))
//        allDataForCharts.postValue(arr)
        val TIME_OF_WORK_LOOPER = 100*60*60*1000L
        var SUM = 1200.0
        var looperTime = object : CountDownTimer(TIME_OF_WORK_LOOPER,1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeNow.value      = "${getTimeHuman(PreferenceMaestro.chosenTimeZone)}"
                financeM1Now.value = "${roundTo7decimals(SUM.toFloat())}"

                SUM *= 1.012

            }
            override fun onFinish() {}
        }.start()

        checkConnection()

        if (ensureNeedUpdateOrNot_PeriodThreeHr()){
            startRequestFor5days()
        }

        Timber.i(">>> ${ensureNeedUpdateOrNot_PeriodThreeHr()}")
        fiveDaysRequestRes.postValue(Resource.Init()) //
    }

    private fun ensureNeedUpdateOrNot_PeriodThreeHr() : Boolean {
        Timber.i("~~~~~~~~ init VW  current:${(getCurrentTimestampSec()-3600*3)}  last:${PreferenceMaestro.timeOfLastDataUpdateLong}")
        Timber.i("ensure to upd ${getCurrentTimestampSec()-3600*3} ? ${PreferenceMaestro.timeOfLastDataUpdateLong}  ~~  ${(getCurrentTimestampSec()-3600*3)>PreferenceMaestro.timeOfLastDataUpdateLong}")
        if (PreferenceMaestro.timeOfLastDataUpdateLong == 1234L){
            return true
        }
        if ((getCurrentTimestampSec()-3600*3)>PreferenceMaestro.timeOfLastDataUpdateLong) {
            return true
        }
        return false
    }


    fun manualRequest(){
        //cm = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // check Internet connection
        //activeNetwork= cm.activeNetworkInfo
        //isConnected = activeNetwork?.isConnectedOrConnecting == true
        checkConnection()
        startRequestFor5days()
    }

    private fun startRequestFor5days() = viewModelScope.launch {
        safe5daysRequest()
    }


    private suspend fun safe5daysRequest() {
        fiveDaysRequestRes.postValue(Resource.Loading())

        try {
            Timber.i(" xxx safe5daysRequest() isConnected:${isConnected}")
            if (isConnected){
                //checkInternetConnection.postValue()


                var response =  repoSpx.get5daysRequest(input_API_KEY = API_KEY)
                when (response.code()){
                    401 -> {

                        fiveDaysRequestRes.postValue(Resource.Error("Error 401. Try Later", null,401))

                        response =  keySwitcherResponser()

                    }
                }


                fiveDaysRequestRes.postValue( handle5daysResponse(response)  )
                Timber.d("start API request, code:${response.code()}")

                // if invalid key- {"cod":401, "message": "Invalid API key. Please see http://openweathermap.org/faq#error401 for more info."}

                PreferenceMaestro.timeOfLastDataUpdate =  generateTimestampLastUpdate() // for UI sign
                PreferenceMaestro.timeOfLastDataUpdateLong = getCurrentTimestampSec()   // for frequency of update
                //be.postValue(handleBetaResponse(response))

            }else{
                fiveDaysRequestRes.postValue(Resource.Error("NO INTERNET", null,0))
                Timber.e("NO INTERNET")
                //checkInternetConnection.postValue("NO INTERNET") // i will delete this
            }
        }catch (t: Throwable){
            when(t) {
                is IOException -> fiveDaysRequestRes.postValue(Resource.Error("Network Failure",null,0))
                else -> fiveDaysRequestRes.postValue(Resource.Error("Conversion Error",null,0))
            }
            Timber.e("safeBetaRequest() error: " + t)
        }
    }

    private suspend fun keySwitcherResponser(): Response<FiveDaysForecastModelParser> {
        var response = repoSpx.get5daysRequest(input_API_KEY = API_KEY_RESERVE1)
        Timber.w("used API_KEY_RESERVE1")
        if (response.code() == 401) {
            response = repoSpx.get5daysRequest(input_API_KEY = API_KEY_RESERVE2)
            delay(100)
            Timber.w("used API_KEY_RESERVE2")
            if (response.code() == 401) {
                response = repoSpx.get5daysRequest(input_API_KEY = API_KEY_RESERVE3)
                delay(100)
                Timber.w("used API_KEY_RESERVE3")
                if (response.code() == 401) {
                    response = repoSpx.get5daysRequest(input_API_KEY = API_KEY_RESERVE4)
                    delay(100)
                    Timber.w("used API_KEY_RESERVE4")

                }
            }
        }

        return response
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

                    //


                    PreferenceMaestro.sunriseL = sunrise
                    PreferenceMaestro.sunsetL = sunset
                    //PreferenceMaestro.timezoneL = unxtoHrAndMinutesByDecimial(timeZone,false)

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
                    /**
                     * Save sunrise and sunset define by timezone
                     */
                    PreferenceMaestro.sunriseHour = unxtoHrAndMinutesByDecimial(sunrise,true)
                    PreferenceMaestro.sunsetHour =   unxtoHrAndMinutesByDecimial(sunset,true)

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
        response.code()
        //fiveDaysRequestRes.postValue(Resource.Error("Error 401. Try Later", null))
        when(response.code()) {
            401 -> return Resource.Error("Error 401. Try Later",null,401)
            else -> return Resource.Error(response.message(),null,0)
        }

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
                forecastArray.add(data.value!!.get(i).forecast.toFloat() * (PreferenceMaestro.calibrationCoeff))
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