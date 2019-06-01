package com.pwr.sailapp.data.sail

import com.google.gson.annotations.SerializedName

data class Gear(
    @SerializedName("gear_id")
    val ID: Int,
    @SerializedName("gear_name")
    val name: String,
    @SerializedName("gear_price")
    val price: Int,
    @SerializedName("gear_quantity")
    val quantity: Int
)