package com.pwr.sailapp.data

import java.text.DateFormat
import java.util.*

// TODO add number of hours
data class Rental (val id: Int, val centre: Centre, val rentStartDate: Date,  val timeOptionID: Int, val equipmentOptionID: Int){
    override fun toString() = "$id, ${centre.name} , $rentDate , $rentStartTime, $timeOptionID, $equipmentOptionID"

    private val dateFormatDate = DateFormat.getDateInstance()

    private val dateFormatTime = DateFormat.getTimeInstance()

    val rentDate: String
        get() = dateFormatDate.format(rentStartDate)

    val rentStartTime: String
        get() = dateFormatTime.format(rentStartDate)

    // Required for correctly removing rentals from array list
    override fun equals(other: Any?): Boolean {
        return  if(other == null || other !is Rental) false
            else other.id == this.id
    }

    override fun hashCode() = id
}