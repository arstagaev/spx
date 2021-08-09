package com.revolve44.solarpanelx.datasource.model.db





data class FirstChartDataTransitor (
    val dates : ArrayList<String>,
    val forecasts : ArrayList<Int> // need in Int coz we all measure in watts dont worry about convert to float
)