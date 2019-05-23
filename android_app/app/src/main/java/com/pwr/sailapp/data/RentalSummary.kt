package com.pwr.sailapp.data

import com.pwr.sailapp.data.sail.Rental
import com.pwr.sailapp.data.weather.Currently

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

    //  val endTime : String
    //    get()  = rental. //...

    val temperature: String?
        get() = currently?.temperature.toString()

    val wind: String?
        get() = currently?.windSpeed.toString()

    val iconName: String?
        get() = currently?.icon

    val gearName : String
        get() = rental.equipmentName

    val photoURL : String
        get() = rental.centre.photoURL

    override fun toString(): String {
        return "$centreName $startDate $startTime $temperature $wind $iconName\n"
    }
}
