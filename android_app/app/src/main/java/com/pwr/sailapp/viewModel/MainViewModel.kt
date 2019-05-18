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
import com.pwr.sailapp.utils.FiltersAndLocationUtil
import com.pwr.sailapp.utils.FiltersAndLocationUtil.calculateDistances
import com.pwr.sailapp.utils.FiltersAndLocationUtil.filterAndSortCentres

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

    /*
    Transformations.switchMap lets you create a new LiveData that reacts to changes of other LiveData instances. It also allows carrying over the observer Lifecycle information across the chain:
    Mediator - combines multiple live data sources into single live data
     */
    lateinit var centres : MediatorLiveData<ArrayList<Centre>>
    lateinit var allCentres: LiveData<ArrayList<Centre>>


    val selectedCentre = MutableLiveData<Centre>() // observe which centre was selected
    var location: Location? = null

    // Dialogs
    var minRating = INITIAL_MIN_RATING
    var maxDistance = INITIAL_MAX_DISTANCE
    var actualDistance = INITIAL_MAX_DISTANCE
    var isByRating = false

    // Rent details fragment
    lateinit var centreEquipment : LiveData<ArrayList<Equipment>>
    val selectedEquipment = MutableLiveData<Equipment>()

    val startTime = MutableLiveData<Date>()
    val endTime = MutableLiveData<Date>()



    // Profile fragment
    val rentals = MutableLiveData<ArrayList<Rental>>()


    init {
        // TODO use repository and LiveData here
        if (CredentialsUtil.isLogged(application.applicationContext)) authenticationState.value =
            AuthenticationState.AUTHENTICATED
        else authenticationState.value = AuthenticationState.UNAUTHENTICATED
        currentUser = fetchUserData()
        rentals.value = ArrayList<Rental>()
    }

    // TODO consider using live data for observing whether the user has remover their credentials from shared preferences (logged out)


    suspend fun fetchCentres(){
        val fetchedCentres = mainRepositoryImpl.getCentres()
        allCentres = fetchedCentres
        if(allCentres.value == null) {Log.e("MainViewModel", "fetchCentres: allCentres.value = null")}
        // Transformation of live data
        // centres = Transformations.map(allCentres) { inputCentres -> filterAndSortCentres(inputCentres, INITIAL_MIN_RATING)}
         centres = MediatorLiveData()
         centres.addSource(allCentres){ inputCentres ->
             val centresDist = calculateDistances(inputCentres, location)
             centres.value = filterAndSortCentres(centresDist, minRating, isByRating, actualDistance)}
    }

    suspend fun fetchEquipment(centreID: Int){
        val fetchedEquipment = mainRepositoryImpl.getAllCentreGear(centreID)
        centreEquipment = fetchedEquipment
        if(centreEquipment.value == null) {Log.e("MainViewModel", "fetchEquipment: centreEquipment.value = null")}
    }

    fun selectCentre(centre: Centre) { selectedCentre.value = centre }

    fun confirmRental(): Boolean {
        if(centreEquipment.value == null) {Log.e("MainViewModel", "confirmRental: equipment.options = null"); return false}
        return if (selectedCentre.value != null
            && startTime.value != null
            && endTime.value != null
            && selectedEquipment.value != null
            && selectedEquipment.value != null
        ) {
            MockRentals.counter++ // mock ID
            rentals.value?.add(
                Rental(
                    MockRentals.counter,
                    selectedCentre.value!!,
                    dateToString(startTime.value!!)!!,
                    dateToString(endTime.value!!)!!,
                    selectedEquipment.value!!.equipmentID,
                    selectedEquipment.value!!.toString()
                )
            ); true
        } else false
    }

    fun logOut() {
        CredentialsUtil.resetUserCredentials(appContext)
        authenticationState.value = AuthenticationState.UNAUTHENTICATED
    }


    // Search (filter) centres by name
    fun search(query: String?) {
        if(query == null) {Log.e("MainViewModel", "search: query = null"); return }
        if(centres.value == null) {Log.e("MainViewModel", "search: centres.value = null"); return }
        val queryLowerCase = query.toLowerCase(Locale.getDefault())
        if (queryLowerCase.isNotEmpty()) {
            val filteredCentres = centres.value!!.filter { centre : Centre ->
                centre.name.toLowerCase(Locale.getDefault()).contains(queryLowerCase) // && centre.rating>minRating
            }
            centres.value = ArrayList(filteredCentres)
        } else centres.value = filterAndSortCentres(allCentres.value, minRating, isByRating, actualDistance)
    }


    fun applyFilter(){
        if(allCentres.value == null) {Log.e("MainViewModel", "applyFilter: allCentres.value = null"); return }
        centres.value = filterAndSortCentres(allCentres.value, minRating, isByRating, actualDistance)
    }

    fun applySort(){
        if(centres.value == null) {Log.e("MainViewModel", "applySort: allCentres.value = null"); return}
        centres.value = filterAndSortCentres(allCentres.value, minRating, isByRating, actualDistance)
    }

    fun applyLocation(){
        if(location == null){ Log.e("MainViewModel", "calculateDistances: location = null"); return }
        centres.value = calculateDistances(centres.value, location)
    }


    fun cancelRental(rental: Rental) {
        rentals.value?.remove(rental)
        /*
        Necessary to notify that live data changed.
        Since live data is an object (arrayList), adding nor removing will not cause the data change (still the same reference)
         */
        rentals.value = rentals.value
    }

    private fun fetchUserData(): User = MockUsers.usersList[0]

}