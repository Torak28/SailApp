package com.pwr.sailapp.utils

import android.location.Location
import android.util.Log
import com.pwr.sailapp.data.sail.Centre

object FiltersAndLocationUtil {

    fun filterAndSortCentres(inputCentres : Collection<Centre>?, isAscending:Boolean, actualDistance:Double):ArrayList<Centre>{
        if(inputCentres == null) {
            Log.e("MainViewModel", "filterAndSortCentres: inputCentres = null"); return ArrayList() }
        val filteredCentres = inputCentres.filter { centre ->
            centre.distance < actualDistance
        }
        val sortedFilteredCentres = if (isAscending) filteredCentres.sortedBy { it.rating } else filteredCentres.sortedByDescending { it.distance }
        return ArrayList(sortedFilteredCentres)
    }

    fun calculateDistances(inputCentres: Collection<Centre>?, myLocation: Location?):ArrayList<Centre> {
        if(inputCentres == null) { Log.e("MainViewModel", "calculateDistances: inputCentres = null"); return ArrayList() }
        if(myLocation == null){ Log.d("MainViewModel", "calculateDistances: myLocation = null"); return ArrayList(inputCentres)}
        val outputCentres = inputCentres.map { centre ->
            val distance = calculateDistance(myLocation, Pair(centre.coordinateX, centre.coordinateY))
            centre.distance = distance.toDouble()
            centre
        }
        return ArrayList(outputCentres)
    }

    private fun calculateDistance(myLocation: Location, theirCoordinates: Pair<Double, Double>): Float {
        val theirLocation = Location("").apply {
            latitude = theirCoordinates.first
            longitude = theirCoordinates.second
        }
        return myLocation.distanceTo(theirLocation)
    }
}