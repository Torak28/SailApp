package com.pwr.sailapp.data.sail

import android.util.Log
import com.google.gson.annotations.SerializedName
import com.pwr.sailapp.utils.DateUtil
import java.text.DateFormat
import java.util.*

// TODO add number of hours
data class Rental (
    @SerializedName("rent_id")
    val id: Int,

    val centre: Centre = Centre(20,
        "Default centre",
        3.0,
        "Default location",
        54.692867,
        18.691693,
        "https://imageog.flaticon.com/icons/png/512/36/36601.png?size=1200x630f&pad=10,10,10,10&ext=png&bg=FFFFFFFF",
        123456789
        ),

    @SerializedName("rent_start")
    val rentStartDateStr: String = "Thu, 30 May 2019 08:35:31 GMT",

    @SerializedName("rent_end")
    val rentEndDateStr: String = "Thu, 30 May 2019 08:25:31 GMT",

    val equipmentOptionID: Int = 0,

    @SerializedName("gear_name")
    val equipmentName: String,

    @SerializedName("centre_id")
    val centreID : Int? = null,

    @SerializedName("centre_name")
    val centreName : String? = null,

    @SerializedName("rent_quantity")
    val rentQuantity : Int? = null,

    @SerializedName("gear_name")
    val gearName: String? = null


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

    val rentEndTime: String
        get() {
            val dateFormatTime = DateFormat.getTimeInstance()
            val date = DateUtil.stringToDate(rentEndDateStr)
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