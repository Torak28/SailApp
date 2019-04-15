package com.pwr.sailapp.data

// TODO add number of hours
data class Rental (val centre: Centre, val rentDate: String, val rentStartTime:String){
    override fun toString(): String = "${centre.name} , $rentDate , $rentStartTime"
}