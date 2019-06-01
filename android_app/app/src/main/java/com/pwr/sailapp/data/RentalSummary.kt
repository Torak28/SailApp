package com.pwr.sailapp.data

import com.pwr.sailapp.data.sail.Rental
import com.pwr.sailapp.data.weather.Currently

const val CLEAR_DAY = "clear-day"
const val CLEAR_NIGHT = "clear-night"
const val RAIN = "rain"
const val SNOW = "snow"
const val SLEET= "sleet"
const val WIND = "wind"
const val FOG = "fog"
const val CLOUDY = "cloudy"
const val PARTLY_CLOUDY_DAY = "partly-cloudy-day"
const val PARTLY_CLOUDY_NIGHT = "partly-cloudy-night"

const val CELSIOUS = "°C"
const val KPH = "kph"

data class RentalSummary(
    val rental: Rental,
    private val currently: Currently?
) {

    val centreName: String
        get() = rental.centre.name

    val startDate: String
        get() = rental.rentDate

    val startTime: String
        get() = rental.rentStartTime

    val endTime: String
        get() = rental.rentEndTime

    //  val endTime : String
    //    get()  = rental. //...

    val temperature: String?
        get() {
            val tempInt = currently?.temperature?.toInt()
            return "$tempInt$CELSIOUS"
        }

    val wind: String?
        get() {
            val tempInt = currently?.windSpeed?.toInt()
            return "$tempInt $KPH"
        }

    val iconName: String?
        get() = currently?.icon

    val gearName : String
        get() = rental.equipmentName

    val photoURL : String
        get() = rental.centre.photoURL ?: ""

    override fun toString(): String {
        return "$centreName $startDate $startTime $temperature $wind $iconName\n"
    }
}
