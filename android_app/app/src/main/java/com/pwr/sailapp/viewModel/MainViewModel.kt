package com.pwr.sailapp.viewModel

import android.app.Application
import android.location.Location
import android.media.Rating
import android.telephony.cdma.CdmaCellLocation
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.pwr.sailapp.data.Centre
import com.pwr.sailapp.utils.CredentialsUtil
import com.pwr.sailapp.data.MockCentres
import com.pwr.sailapp.data.Rental
import java.util.*
import kotlin.collections.ArrayList

/*
https://developer.android.com/guide/navigation/navigation-conditional#kotlin
https://developer.android.com/reference/android/arch/lifecycle/AndroidViewModel - AndroidViewModel provides application (and its context!)
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val appContext = application.applicationContext

    companion object {
        const val INITIAL_MIN_RATING = 0.0
        const val INITIAL_COORDINATE_X = 0.00
        const val INITIAL_COORDINATE_Y = 0.00
        const val INITIAL_MAX_DISTANCE = 1000000.00
    }

    enum class AuthenticationState{
        AUTHENTICATED,
        UNAUTHENTICATED,
        INVALID_AUTHENTICATION
    }

    val authenticationState = MutableLiveData<AuthenticationState>() // observe it to know if user is logged in
    val centres = MutableLiveData<ArrayList<Centre>>() // observe it to know which centres are available
    val allCentres = ArrayList<Centre>()
    // TODO consider mutable live data
    var minRating = INITIAL_MIN_RATING
    var maxDistance = INITIAL_MAX_DISTANCE
    var coordinates = Pair(INITIAL_COORDINATE_X, INITIAL_COORDINATE_Y)
    var actualDistance = INITIAL_MAX_DISTANCE

    val selectedCentre = MutableLiveData<Centre>() // observe which centre was selected
    val rentals = MutableLiveData<ArrayList<Rental>>()

    init{
        // TODO use repository and LiveData here
        if (CredentialsUtil.isLogged(application.applicationContext))  authenticationState.value = AuthenticationState.AUTHENTICATED
        else authenticationState.value = AuthenticationState.UNAUTHENTICATED
        centres.value = MockCentres.centres
        allCentres.addAll(MockCentres.centres)
        rentals.value = ArrayList<Rental>()
    }

    // TODO consider using live data for observing whether the user has remover their credentials from shared preferences (logged out)

    fun selectCentre(centre: Centre){ // TODO consider nicer Kotlin syntax
        selectedCentre.value = centre
    }

    fun confirmRental(centre: Centre, startDate: String, startTime: String){
    //    val rentalsPrev = rentals.value
    //    rentalsPrev?.add(Rental(centre, startDate, startTime))
        rentals.value?.add(Rental(centre, startDate, startTime))
    }

    fun logOut(){
        CredentialsUtil.resetUserCredentials(appContext)
        authenticationState.value = MainViewModel.AuthenticationState.UNAUTHENTICATED
    }

    // TODO rating, location filtering, real location
    fun search(query: String?){
        val queryLowerCase = query!!.toLowerCase(Locale.getDefault())
        // Filter by centre name
        if(!queryLowerCase.isEmpty()) {
            val filteredCentres = centres.value!!.filter { centre ->
                centre.name.toLowerCase(Locale.getDefault()).contains(queryLowerCase) // && centre.rating>minRating
            }
            centres.value = ArrayList(filteredCentres)
        }
        else centres.value = allCentres
    }

    fun filter(){ // rating, distance, sport, centreName ...
        val filteredCentres = allCentres.filter { centre ->
            centre.rating >= minRating  && centre.distance<actualDistance
        }
        centres.value = ArrayList(filteredCentres)
    }

    fun sort(){ // byDistance :Boolean = true, byRating: Boolean = false
        val sortedCentres = centres.value!!.sortedBy {it.distance} // {if(byRating) it.rating else it.distance} - distance !!!
        centres.value = ArrayList(sortedCentres)
    }

    fun calculateDistances(myLocation: Location){
        for(centre in centres.value!!){
            val distance = calculateDistance(myLocation, Pair(centre.coordinateX, centre.coordinateY))
            centre.distance = distance.toDouble()
            Log.d("Calculated distance", "$distance") // ...
        }
    }

    private fun calculateDistance(myLocation: Location, theirCoordinates: Pair<Double, Double>):Float{
        val theirLocation = Location("")
        theirLocation.latitude = theirCoordinates.first
        theirLocation.longitude = theirCoordinates.second
        return myLocation.distanceTo(theirLocation)
    }




}