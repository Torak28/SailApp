package com.pwr.sailapp.viewModel

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.*
import com.pwr.sailapp.data.*
import com.pwr.sailapp.data.mocks.MockCentres
import com.pwr.sailapp.data.mocks.MockRentalOptions
import com.pwr.sailapp.data.mocks.MockRentals
import com.pwr.sailapp.data.mocks.MockUsers
import com.pwr.sailapp.data.network.sail.ConnectivityInterceptorImpl
import com.pwr.sailapp.data.network.sail.SailAppApiService
import com.pwr.sailapp.data.network.sail.SailNetworkDataSource
import com.pwr.sailapp.data.network.sail.SailNetworkDataSourceImpl
import com.pwr.sailapp.data.repository.MainRepositoryImpl
import com.pwr.sailapp.utils.CredentialsUtil
import java.util.*
import kotlin.collections.ArrayList
import com.pwr.sailapp.utils.DateUtil
import com.pwr.sailapp.utils.DateUtil.dateToString
import kotlinx.coroutines.*

/*
https://developer.android.com/guide/navigation/navigation-conditional#kotlin
https://developer.android.com/reference/android/arch/lifecycle/AndroidViewModel - AndroidViewModel provides application (and its context!)
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val appContext = application.applicationContext

    // TODO move repo to constructor, use dependency injection
    private val mainRepositoryImpl =
        MainRepositoryImpl(SailNetworkDataSourceImpl(SailAppApiService(ConnectivityInterceptorImpl(appContext))))

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

    // val centres = MutableLiveData<ArrayList<Centre>>() // TODO use transformations here
    /*
    Transformations.switchMap lets you create a new LiveData that reacts to changes of other LiveData instances. It also allows carrying over the observer Lifecycle information across the chain:
     */
    lateinit var centres : LiveData<ArrayList<Centre>>
    lateinit var allCentres: LiveData<ArrayList<Centre>>

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
    // val timeOptions = ArrayList<String>()
    val endTime = MutableLiveData<Date>()
    val equipmentOptions = ArrayList<String>()
    // val selectedTimeIndex = MutableLiveData<Int>() // observe which element of time options array list was selected
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
        //     centres.value = MockCentres.centres
        //     allCentres.addAll(MockCentres.centres)
        rentals.value = ArrayList<Rental>()
        // fetchCentres()
    }

    // TODO consider using live data for observing whether the user has remover their credentials from shared preferences (logged out)


    suspend fun fetchCentres(){
        val fetchedCentres = mainRepositoryImpl.getCentres()
        allCentres = fetchedCentres
        if(allCentres.value == null) {Log.e("MainViewModel", "fetchCentres: allCentres.value = null")}
        // Transformation of live data
        centres = Transformations.map(allCentres) { inputCentres -> filterAndSortCentres(inputCentres)}
    }

    fun selectCentre(centre: Centre) { selectedCentre.value = centre }

    fun confirmRental(): Boolean {
        //    val rentalsPrev = rentals.value
        //    rentalsPrev?.add(Rental(centre, startDate, startTime))
        if (selectedCentre.value != null && startTime.value != null && endTime.value != null && selectedEquipmentIndex.value != null) {
            MockRentals.counter++ // mock ID
            rentals.value?.add(
                Rental(
                    MockRentals.counter,
                    selectedCentre.value!!,
                    dateToString(startTime.value!!)!!,
                    dateToString(endTime.value!!)!!,
                    selectedEquipmentIndex.value!!,
                    equipmentOptions[selectedEquipmentIndex.value!!]
                )
            ); return true
        } else return false
    }

    fun logOut() {
        CredentialsUtil.resetUserCredentials(appContext)
        authenticationState.value = AuthenticationState.UNAUTHENTICATED
    }

/*
    fun search(query: String?) {
        val queryLowerCase = query!!.toLowerCase(Locale.getDefault())
        // Filter by centre name
        if (!queryLowerCase.isEmpty()) {
            val filteredCentres = centres.value!!.filter { centre : Centre ->
                centre.name.toLowerCase(Locale.getDefault()).contains(queryLowerCase) // && centre.rating>minRating
            }
            centres.value = ArrayList(filteredCentres)
        } else centres.value = allCentres.value
    }
*/
    private fun filterAndSortCentres(inputCentres : ArrayList<Centre>):ArrayList<Centre>{
        val filteredCentres = inputCentres.filter { centre ->
            centre.rating >= minRating && centre.distance < actualDistance
        }
        return ArrayList(filteredCentres)
    }
/*
    fun filter() { // rating, distance, sport, centreName ...
        if(allCentres.value == null) {Log.e("MainViewModel", "filter: allCentres.value = null"); return}
        val filteredCentres = allCentres.value!!.filter { centre ->
            centre.rating >= minRating && centre.distance < actualDistance
        }
        centres.value = ArrayList(filteredCentres)
    }

    fun sort() {
        if(centres.value == null) {Log.e("MainViewModel", "sort: centres.value = null"); return}
        val sortedCentres =
            if (isByRating) centres.value!!.sortedBy { it.rating } else centres.value!!.sortedBy { it.distance }
        centres.value = ArrayList(sortedCentres)
    }
*/
    fun calculateDistances(myLocation: Location) {
        if(centres.value == null) {Log.e("MainViewModel", "calculateDistances: centres.value = null"); return}
        for (centre in centres.value!!) {
            val distance = calculateDistance(myLocation, Pair(centre.coordinateX, centre.coordinateY))
            centre.distance = distance.toDouble()
        //    Log.d("Calculated distance", "$distance") // ...
        }
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