package com.pwr.sailapp.data


data class User(val id:Int, val firstName:String, val lastName:String, var email:String, var password:String, var phoneNumber:String = "", var authToken:String = "") {
}