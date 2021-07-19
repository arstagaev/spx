package com.revolve44.solarpanelx.datasource.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.revolve44.solarpanelx.datasource.model.db.ForecastCell
import com.revolve44.solarpanelx.datasource.model.db.SolarStation


@Dao
interface SolarDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addStation(solarStation: SolarStation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveForecastCell(forecastCell: ForecastCell)

    @Query("DELETE FROM forecastcell")
    suspend fun deleteAllinForecastCell()

    @Query("SELECT * FROM forecastcell")
    fun getAllForecastCells(): LiveData<List<ForecastCell>>

    @Query("SELECT COUNT(*) from forecastcell")
    fun getSizeOfForecast() : Int

//    @Delete
//    suspend fun deleteStation(vararg solarStation: SolarStation)

//    @Query("SELECT * FROM solarstation")
//    suspend fun getAllStations(): LiveData<List<SolarStation>>
}