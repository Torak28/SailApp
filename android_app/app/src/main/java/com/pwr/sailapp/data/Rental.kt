package com.pwr.sailapp.data

import java.text.DateFormat
import java.util.*

// TODO add number of hours
data class Rental (val centre: Centre, val rentStartDate: Date,  val timeOptionID: Int, val equipmentOptionID: Int){
    override fun toString() = "${centre.name} , $rentDate , $rentStartTime, $timeOptionID, $equipmentOptionID"

    private val dateFormatDate = DateFormat.getDateInstance()

    private val dateFormatTime = DateFormat.getTimeInstance()

    val rentDate: String
        get() = dateFormatDate.format(rentStartDate)

    val rentStartTime: String
        get() = dateFormatTime.format(rentStartDate)
}