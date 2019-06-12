package com.pwr.sailapp.data.sail

import android.util.Log
import com.google.gson.annotations.SerializedName
import com.pwr.sailapp.data.weather.Currently
import com.pwr.sailapp.utils.DateUtil
import java.text.DateFormat
import java.util.*

data class Rental(
    @SerializedName("rent_id")
    val ID: Int,

    @SerializedName("centre_id")
    val centreID: Int? = null,

    @SerializedName("centre_name")
    val centreName: String? = null,

    @SerializedName("rent_start")
    val rentStartDateStr: String = "Thu, 30 May 2019 08:35:31 GMT",

    @SerializedName("rent_end")
    val rentEndDateStr: String = "Thu, 30 May 2019 08:25:31 GMT",

    @SerializedName("gear_name")
    val equipmentName: String,

    @SerializedName("rent_quantity")
    val rentQuantity: Int? = null,

    @SerializedName("centre_latitude")
    val centreLatitude: String,

    @SerializedName("centre_longitude")
    val centreLongitude: String,

    @SerializedName("centre_phone_number")
    val centrePhoneNumber: String,

    @SerializedName("rent_status")
    val rentStatus: String = STATUS_PENDING,

    var currently : Currently? = null

) {

    companion object{
        const val STATUS_ACCEPTED = "accepted"
        const val STATUS_DENIED= "denied"
        const val STATUS_PENDING = "pending"
    }

    override fun toString() = "$ID, $centreName , $rentStartDateStr, $rentEndDateStr, $equipmentName"


    //  private val dateFormatDate = DateFormat.getDateInstance()
    //  private val dateFormatTime = DateFormat.getTimeInstance()
    //  private val rentStartDate:Date? = DateUtil.stringToDate(rentStartDateStr)

    val rentStartDate: Date?
        get() = DateUtil.stringToDate(rentStartDateStr)

    val rentEndDate: Date?
        get() = DateUtil.stringToDate(rentEndDateStr)

    val rentStartDateFormatted: String
        get() {
            val dateFormatDate = DateFormat.getDateInstance()
            val date = DateUtil.stringToDate(rentStartDateStr)
            return dateFormatDate.format(date)
        }

    val rentStartTimeFormatted: String
        get() {
            val dateFormatTime = DateFormat.getTimeInstance()
            val date = DateUtil.stringToDate(rentStartDateStr)
            return dateFormatTime.format(date)
        }

    val rentEndDateFormatted: String
        get() {
            val dateFormatDate = DateFormat.getDateInstance()
            val date = DateUtil.stringToDate(rentEndDateStr)
            return dateFormatDate.format(date)
        }

    val rentEndTimeFormatted: String
        get() {
            val dateFormatTime = DateFormat.getTimeInstance()
            val date = DateUtil.stringToDate(rentEndDateStr)
            return dateFormatTime.format(date)
        }

    val latitude: String
        get() = centreLatitude

    val longitude: String
        get() = centreLongitude

    val timestampSecs: String?
        get() {
            val timeMillis = rentStartDate?.time
            if (timeMillis == null) {
                Log.e("timestamp.get()", "rentStartDate?.time = null")
            }
            val timeSecs = timeMillis!! / 1000
            return timeSecs.toString()
        }

}