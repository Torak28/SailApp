package com.pwr.sailapp.data

import com.google.gson.annotations.SerializedName


data class User(val id:Int,
                @SerializedName("first_name")
                val firstName:String,
                @SerializedName("second_name")
                val lastName:String,
                @SerializedName("email")
                var email:String,
                @SerializedName("password")
                var password:String = "",
                @SerializedName("phone_number")
                var phoneNumber:String = "",
                var authToken:String = "")