package com.pwr.sailapp.data.sail

import com.google.gson.annotations.SerializedName



class Equipment(
    @SerializedName("gear_id")
    val equipmentID: Int,
    val name: String,
    @SerializedName("price_hour")
    val priceHour: Double
)