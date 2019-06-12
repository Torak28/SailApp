package com.pwr.sailapp.data.weather

data class Currently(
    val apparentTemperature: Double,
    val cloudCover: Double,
    val dewPoint: Double,
    val humidity: Double,
    val icon: String,
    val ozone: Double,
    val precipIntensity: Double,
    val precipProbability: Double,
    val precipType: String,
    val pressure: Double,
    val summary: String,
    val temperature: Double,
    val time: Int,
    val uvIndex: Int,
    val visibility: Double,
    val windBearing: Int,
    val windGust: Double,
    val windSpeed: Double
){
    val temperatureFormatted : String
    get() = "${temperature.toInt()} $TEMPERATURE_UNIT"

    val windSpeedFormatted : String
    get() = "${windSpeed.toInt()} $WIND_UNIT"

    companion object{
        val CLEAR_DAY = "clear-day"
        val CLEAR_NIGHT = "clear-night"
        val RAIN = "rain"
        val SNOW = "snow"
        val SLEET ="sleet"
        val WIND = "wind"
        val FOG = "fog"
        val CLOUDY = "cloudy"
        val PARTLY_CLOUDY_DAY = "partly-cloudy-day"
        val PARTLY_CLOUDY_NIGHT = "partly-cloudy-night"

        val TEMPERATURE_UNIT = "°C"
        val WIND_UNIT = "km/h"
    }
}