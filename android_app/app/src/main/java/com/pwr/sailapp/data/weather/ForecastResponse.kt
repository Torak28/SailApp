package com.pwr.sailapp.data.weather

data class ForecastResponse(
    val current: Current,
    val forecast: Forecast,
    val location: Location
)