package com.pwr.sailapp.data


data class Centre(val ID:Int, val name:String, var rating: Double, val location: String, val coordinateX:Double, val coordinateY:Double, var photoURL:String, var phone:Int, var distance:Double = Double.POSITIVE_INFINITY)
