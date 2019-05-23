package com.pwr.sailapp.data.sail

import android.util.Log
import com.google.gson.annotations.SerializedName
import com.pwr.sailapp.utils.DateUtil
import java.text.DateFormat
import java.util.*

// TODO add number of hours
data class Rental (
    @SerializedName("rental_id")
    val id: Int,
    // @Embedded(prefix = "centre_")
    val centre: Centre,
    @SerializedName("rent_start")
    val rentStartDateStr: String = "12-02-2020T13:15:00",
    @SerializedName("rent_end")
    val rentEndDateStr: String = "12-02-2020T13:16:00",
    val equipmentOptionID: Int = 0,
    @SerializedName("gear_name")
    val equipmentName: String
){

    override fun toString() = "$id, ${centre.name} , $rentStartDateStr, $rentEndDateStr, $equipmentName"


  //  private val dateFormatDate = DateFormat.getDateInstance()
  //  private val dateFormatTime = DateFormat.getTimeInstance()
  //  private val rentStartDate:Date? = DateUtil.stringToDate(rentStartDateStr)

    val rentStartDate : Date?
    get() = DateUtil.stringToDate(rentStartDateStr)

    val rentDate: String
        get() {
            val dateFormatDate = DateFormat.getDateInstance()
            val date = DateUtil.stringToDate(rentStartDateStr)
            return dateFormatDate.format(date)
        }

    val rentStartTime: String
        get() {
            val dateFormatTime = DateFormat.getTimeInstance()
            val date = DateUtil.stringToDate(rentStartDateStr)
            return dateFormatTime.format(date)
        }

    val latitude: String
        get() = centre.coordinateX.toString()

    val longitude: String
        get() = centre.coordinateY.toString()

    val timestampSecs: String?
        get() {
            val timeMillis = rentStartDate?.time
            if(timeMillis == null){Log.e("timestamp.get()", "rentStartDate?.time = null")}
            val timeSecs = timeMillis!!/1000
            return timeSecs.toString()
        }


    // Required for correctly removing rentals from array list
    override fun equals(other: Any?): Boolean {
        return  if(other == null || other !is Rental) false
            else other.id == this.id
    }

    override fun hashCode() = id
}