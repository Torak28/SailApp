package com.pwr.sailapp.data

import com.google.gson.annotations.SerializedName

const val CURRENCY = "zł"
const val TIME_UNIT = "h"

class Equipment(
    @SerializedName("gear_id")
    val equipmentID: Int,
    val name: String,
    @SerializedName("price_hour")
    val priceHour: Double
){
    override fun toString(): String {
        return "$name - $priceHour $CURRENCY/$TIME_UNIT"
    }
}