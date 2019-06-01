package com.pwr.sailapp.data.sail

import com.google.gson.annotations.SerializedName

const val CURRENCY = "zł"
const val TIME_UNIT = "h"

data class Gear(
    @SerializedName("gear_id")
    val ID: Int,
    @SerializedName("gear_name")
    val name: String,
    @SerializedName("gear_price")
    val price: Int,
    @SerializedName("gear_quantity")
    val quantity: Int
){
    override fun toString(): String {
        return "$name - $price $CURRENCY/$TIME_UNIT"
    }
}