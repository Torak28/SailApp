package com.pwr.sailapp.viewModel

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.*
import com.pwr.sailapp.data.RentalSummary
import com.pwr.sailapp.data.mocks.MockUsers
import com.pwr.sailapp.data.network.sail.ConnectivityInterceptorImpl
import com.pwr.sailapp.data.network.sail.SailAppApiService
import com.pwr.sailapp.data.network.sail.SailNetworkDataSourceImpl
import com.pwr.sailapp.data.network.weather.DarkSkyApiService
import com.pwr.sailapp.data.repository.MainRepositoryImpl
import com.pwr.sailapp.data.repository.UserManagerImpl
import com.pwr.sailapp.data.sail.Centre
import com.pwr.sailapp.data.sail.Equipment
import com.pwr.sailapp.data.sail.Rental
import com.pwr.sailapp.data.sail.User
import com.pwr.sailapp.utils.FiltersAndLocationUtil.calculateDistances
import com.pwr.sailapp.utils.FiltersAndLocationUtil.filterAndSortCentres
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log

/*
https://developer.android.com/guide/navigation/navigation-conditional#kotlin
https://developer.android.com/reference/android/arch/lifecycle/AndroidViewModel - AndroidViewModel provides application (and its context!)
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val appContext = application.applicationContext

    // TODO move repo to constructor, use dependency injection
    private val mainRepositoryImpl =
        MainRepositoryImpl(
            SailNetworkDataSourceImpl(SailAppApiService(ConnectivityInterceptorImpl(appContext)))
            , DarkSkyApiService(ConnectivityInterceptorImpl(appContext))
        )
    private val userManagerImp = UserManagerImpl(SailAppApiService(ConnectivityInterceptorImpl(appContext)))

    companion object {
        const val INITIAL_MIN_RATING = 0.0
        const val INITIAL_MAX_DISTANCE = 1000000.00
    }

    // Authentication
    val authenticationState = userManagerImp.authStatus

    var currentUser: User
    lateinit var currentRental: Rental

    /*
    Transformations.switchMap lets you create a new LiveData that reacts to changes of other LiveData instances. It also allows carrying over the observer Lifecycle information across the chain:
    Mediator - combines multiple live data sources into single live data
     */
    lateinit var centres: MediatorLiveData<ArrayList<Centre>>
    private lateinit var allCentres: LiveData<ArrayList<Centre>>


    val selectedCentre = MutableLiveData<Centre>() // observe which centre was selected
    var location: Location? = null

    // Dialogs
    var minRating = INITIAL_MIN_RATING
    var maxDistance = INITIAL_MAX_DISTANCE
    var actualDistance = INITIAL_MAX_DISTANCE
    var isByRating = false

    // Rent details fragment
    lateinit var centreEquipment: LiveData<ArrayList<Equipment>>
    val selectedEquipment = MutableLiveData<Equipment>()

    val startTime = MutableLiveData<Date>()
    val endTime = MutableLiveData<Date>()

    // Profile fragment
    // val rentals = MutableLiveData<ArrayList<Rental>>()
    lateinit var rentals: LiveData<ArrayList<Rental>>
    lateinit var rentalSummaries: MediatorLiveData<ArrayList<RentalSummary>>
    lateinit var rentalStats: MediatorLiveData<ArrayList<Rental>>

    init {
        currentUser = fetchUserData()
        //   rentals.value = ArrayList<Rental>()
    }

    suspend fun logOut() {
        userManagerImp.logoutUser()
    }

    suspend fun fetchCentres() {
        val fetchedCentres = mainRepositoryImpl.getCentres()
        allCentres = fetchedCentres
        if (allCentres.value == null) {
            Log.e("MainViewModel", "fetchCentres: allCentres.value = null")
        }
        // Transformation of live data
        // centres = Transformations.map(allCentres) { inputCentres -> filterAndSortCentres(inputCentres, INITIAL_MIN_RATING)}
        centres = MediatorLiveData()
        centres.addSource(allCentres) { inputCentres ->
            val centresDist = calculateDistances(inputCentres, location)
            centres.value = filterAndSortCentres(centresDist, minRating, isByRating, actualDistance)
        }
    }

    suspend fun fetchEquipment(centreID: Int) {
        val fetchedEquipment = mainRepositoryImpl.getAllCentreGear(centreID)
        centreEquipment = fetchedEquipment
        if (centreEquipment.value == null) {
            Log.e("MainViewModel", "fetchEquipment: centreEquipment.value = null")
        }
    }

    suspend fun fetchRentals(userID: Int) {
        val fetchedRentals = mainRepositoryImpl.getAllUserRentals(userID)
        rentals = fetchedRentals
        if (rentals.value == null) {
            Log.e("MainViewModel", "fetchRentals: rentals.value = null)")
        }
        rentalSummaries = MediatorLiveData()
        val today = Calendar.getInstance().time
        rentalSummaries.addSource(rentals) {
            viewModelScope.launch {
                val rentalSums = ArrayList<RentalSummary>()
                for (rental in it) {
                    if (rental.rentStartDate == null) Log.e("fetchRentals", "rental.rentStartDay = null")
                    else if (rental.rentStartDate!! == today || rental.rentStartDate!!.after(today)) {
                        val summary = withContext(Dispatchers.IO) { summariseRental(rental) }
                        rentalSums.add(summary)
                    }
                }
                rentalSummaries.value = rentalSums
            }
        }
    }

    private suspend fun summariseRental(rental: Rental): RentalSummary = mainRepositoryImpl.getRentalSummary(rental)

    suspend fun fetchStats(userID: Int) {
        val fetchedRentals = mainRepositoryImpl.getAllUserRentals(userID)
        rentals = fetchedRentals
        if (rentals.value == null) { Log.e("MainViewModel", "fetchStats: rentals.value = null") }
        rentalStats = MediatorLiveData()
        val today = Calendar.getInstance().time
        rentalStats.addSource(rentals) {
            val previousRentals = it.filter { rental ->
                rental.rentStartDate != null && rental.rentStartDate!!.before(today)
            }
            rentalStats.value = ArrayList(previousRentals)
        }
    }

    fun selectCentre(centre: Centre) {
        selectedCentre.value = centre
    }

    fun confirmRental(): Boolean {
        if (centreEquipment.value == null) {
            Log.e("MainViewModel", "confirmRental: equipment.options = null"); return false
        }
        return (selectedCentre.value != null
                && startTime.value != null
                && endTime.value != null
                && selectedEquipment.value != null
                && selectedEquipment.value != null)
    }

    // Search (filter) centres by name
    fun search(query: String?) {
        if (query == null) {
            Log.e("MainViewModel", "search: query = null"); return
        }
        if (centres.value == null) {
            Log.e("MainViewModel", "search: centres.value = null"); return
        }
        val queryLowerCase = query.toLowerCase(Locale.getDefault())
        if (queryLowerCase.isNotEmpty()) {
            val filteredCentres = centres.value!!.filter { centre: Centre ->
                centre.name.toLowerCase(Locale.getDefault()).contains(queryLowerCase) // && centre.rating>minRating
            }
            centres.value = ArrayList(filteredCentres)
        } else centres.value = filterAndSortCentres(allCentres.value, minRating, isByRating, actualDistance)
    }


    fun applyFilter() {
        if (allCentres.value == null) {
            Log.e("MainViewModel", "applyFilter: allCentres.value = null"); return
        }
        centres.value = filterAndSortCentres(allCentres.value, minRating, isByRating, actualDistance)
    }

    fun applySort() {
        if (centres.value == null) {
            Log.e("MainViewModel", "applySort: allCentres.value = null"); return
        }
        centres.value = filterAndSortCentres(allCentres.value, minRating, isByRating, actualDistance)
    }

    fun applyLocation() {
        if (location == null) {
            Log.e("MainViewModel", "calculateDistances: location = null"); return
        }
        centres.value = calculateDistances(centres.value, location)
    }

    fun cancelRental(rental: Rental) {}


    private fun fetchUserData(): User = MockUsers.usersList[0]

}