package com.revolve44.solarpanelx.datasource

import android.app.Application
import com.revolve44.solarpanelx.datasource.local.SolarDatabase
import com.revolve44.solarpanelx.datasource.models.db.ForecastCell
import com.revolve44.solarpanelx.datasource.models.db.SolarStation
import com.revolve44.solarpanelx.datasource.remote.RetrofitInstance

class SpxRepository( app : Application) {

    val db : SolarDatabase = SolarDatabase.getInstance(app)
    val dao = db.solarDao

    ///////////////////////////////////////Remote/////////////////////////////////////
    //suspend fun getAlpha() =
    //    RetrofitInstance.apiAlpha.getAlfaRequest()

    suspend fun get5daysRequest(input_API_KEY : String) =
        RetrofitInstance.api5daysRequest.get5daysRequest(apiKey = input_API_KEY)

    ///////////////////////////////////Database///////////////////////////////////
    // put
    suspend fun addSolarStation(solarStation: SolarStation) = db.solarDao.addStation(solarStation)
    // put
    suspend fun saveForecastCell(forecastCell: ForecastCell) = db.solarDao.saveForecastCell(forecastCell)
    // del
    suspend fun deleteALL_table_ForecastCell() = db.solarDao.deleteAllinForecastCell()
    // get
    fun getAllForecastCells() = db.solarDao.getAllForecastCells()
    // get size
    fun getSizeOfForecast() = db.solarDao.getSizeOfForecast()

}