package com.revolve44.solarpanelx.datasource.remote

import com.revolve44.solarpanelx.datasource.local.PreferenceMaestro
import com.revolve44.solarpanelx.datasource.models.api.FiveDaysForecastModelParser
import com.revolve44.solarpanelx.global_utils.ConstantsCalculations.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SolarApi {

    // for 5 days
    @GET("data/2.5/forecast")
    suspend fun get5daysRequest(
        @Query("lat")
        lat: Double = PreferenceMaestro.lat.toDouble(),
        @Query("lon")
        lon: Double = PreferenceMaestro.lon.toDouble(),
        @Query("cnt")
        cnt: Int = 40,
        @Query("appid")
        apiKey: String //= API_KEY
    ) : Response<FiveDaysForecastModelParser>

}