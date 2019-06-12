package com.pwr.sailapp.data.sail

import com.google.gson.annotations.SerializedName


data class User(// val id:Int? = null,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("password")
    var password: String = "",
    @SerializedName("phone_number")
    var phoneNumber: String = "",
    @SerializedName("role")
    val role: String = "user"
)