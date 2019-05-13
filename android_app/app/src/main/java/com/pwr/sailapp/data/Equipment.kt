package com.pwr.sailapp.data

import com.google.gson.annotations.SerializedName

class Equipment(
    @SerializedName("gear_id")
    val equipmentID: Int,
    val name: String,
    @SerializedName("price_hour")
    val priceHour: Double
){
    override fun toString(): String {
        return "$equipmentID $name $priceHour"
    }
}