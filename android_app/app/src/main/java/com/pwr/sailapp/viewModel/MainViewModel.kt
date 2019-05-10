package com.pwr.sailapp.viewModel

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.pwr.sailapp.data.*
import com.pwr.sailapp.utils.CredentialsUtil
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

    enum class AuthenticationState {
        AUTHENTICATED,
        UNAUTHENTICATED,
        INVALID_AUTHENTICATION
    }

    // Authentication
    val authenticationState = MutableLiveData<AuthenticationState>() // observe it to know if user is logged in

    var currentUser: User
    lateinit var currentRental: Rental

    // Rent fragments
    // Rent master fragment
    val centres = MutableLiveData<ArrayList<Centre>>() // observe it to know which centres are available
    val allCentres = ArrayList<Centre>()
    val selectedCentre = MutableLiveData<Centre>() // observe which centre was selected

    // Dialogs
    // TODO consider mutable live data
    var minRating = INITIAL_MIN_RATING
    var maxDistance = INITIAL_MAX_DISTANCE
    var coordinates = Pair(INITIAL_COORDINATE_X, INITIAL_COORDINATE_Y)
    var actualDistance = INITIAL_MAX_DISTANCE
    var isByRating = false

    // Rent details fragment
    val startTime = MutableLiveData<Date>()
    val timeOptions = ArrayList<String>()
    val equipmentOptions = ArrayList<String>()
    val selectedTimeIndex = MutableLiveData<Int>() // observe which element of time options array list was selected
    val selectedEquipmentIndex =
        MutableLiveData<Int>() // observe which element of equipment options array list was selected
    // val totalCost = MutableLiveData<Double>()

    // Profile fragment
    val rentals = MutableLiveData<ArrayList<Rental>>()


    init {
        // TODO use repository and LiveData here
        if (CredentialsUtil.isLogged(application.applicationContext)) authenticationState.value =
            AuthenticationState.AUTHENTICATED
        else authenticationState.value = AuthenticationState.UNAUTHENTICATED
        currentUser = fetchUserData()
        centres.value = MockCentres.centres
        allCentres.addAll(MockCentres.centres)
        rentals.value = ArrayList<Rental>()
    }

    // TODO consider using live data for observing whether the user has remover their credentials from shared preferences (logged out)

    fun selectCentre(centre: Centre) { // TODO consider nicer Kotlin syntax
        selectedCentre.value = centre
    }

    fun confirmRental(): Boolean {
        //    val rentalsPrev = rentals.value
        //    rentalsPrev?.add(Rental(centre, startDate, startTime))
        if (selectedCentre.value != null && startTime.value != null && selectedTimeIndex.value != null && selectedEquipmentIndex.value != null) {
            MockRentals.counter ++ // mock ID
            rentals.value?.add(
                Rental(
                    MockRentals.counter,
                    selectedCentre.value!!,
                    startTime.value!!,
                    selectedTimeIndex.value!!,
                    selectedEquipmentIndex.value!!
                )
            ); return true
        } else return false
    }

    fun logOut() {
        CredentialsUtil.resetUserCredentials(appContext)
        authenticationState.value = MainViewModel.AuthenticationState.UNAUTHENTICATED
    }

    fun search(query: String?) {
        val queryLowerCase = query!!.toLowerCase(Locale.getDefault())
        // Filter by centre name
        if (!queryLowerCase.isEmpty()) {
            val filteredCentres = centres.value!!.filter { centre ->
                centre.name.toLowerCase(Locale.getDefault()).contains(queryLowerCase) // && centre.rating>minRating
            }
            centres.value = ArrayList(filteredCentres)
        } else centres.value = allCentres
    }

    fun filter() { // rating, distance, sport, centreName ...
        val filteredCentres = allCentres.filter { centre ->
            centre.rating >= minRating && centre.distance < actualDistance
        }
        centres.value = ArrayList(filteredCentres)
    }

    fun sort() {
        val sortedCentres =
            if (isByRating) centres.value!!.sortedBy { it.rating } else centres.value!!.sortedBy { it.distance }
        centres.value = ArrayList(sortedCentres)
    }

    fun calculateDistances(myLocation: Location) {
        for (centre in centres.value!!) {
            val distance = calculateDistance(myLocation, Pair(centre.coordinateX, centre.coordinateY))
            centre.distance = distance.toDouble()
            Log.d("Calculated distance", "$distance") // ...
        }
    }

    fun fetchTimeOptions() {
        timeOptions.clear() // remove previously fetched data
        timeOptions.addAll(MockRentalOptions.timeOptions) // add new data
    }

    fun fetchEquipmentOptions() {
        equipmentOptions.clear() // remove previously fetched data
        equipmentOptions.addAll(MockRentalOptions.equipmentOptions) // add new data
    }

    fun cancelRental(rental: Rental) {
        rentals.value?.remove(rental)
        /*
        Necessary to notify that live data changed.
        Since live data is an object (arrayList), adding nor removing will not cause the data change (still the same reference)
         */
        rentals.value = rentals.value
    }


    private fun calculateDistance(myLocation: Location, theirCoordinates: Pair<Double, Double>): Float {
        val theirLocation = Location("")
        theirLocation.latitude = theirCoordinates.first
        theirLocation.longitude = theirCoordinates.second
        return myLocation.distanceTo(theirLocation)
    }

    private fun fetchUserData(): User = MockUsers.usersList[0]

}