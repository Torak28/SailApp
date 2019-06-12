package com.pwr.sailapp.utils

fun formatDistance(distance: Double):String{
    return if(distance < Double.POSITIVE_INFINITY && distance > 1000) "%d km".format((distance/1000).toInt()) // distance in km
    else if (distance < 1000) "%d m".format(distance.toInt()) // distance in meters
    else "" // other distances not displayed
}

fun formatCoordinate(coordinate:Double, decimals:Int)= "%.${decimals}f".format(coordinate)
