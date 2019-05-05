package com.pwr.sailapp.utils

fun formatDistance(distance: Double):String{
    if(distance < Double.POSITIVE_INFINITY && distance > 1000) return "%d km".format((distance/1000).toInt()) // distance in km
    else if (distance < 1000) return "%d m".format(distance.toInt()) // distance in meters
    else return "" // other distances not displayed
}