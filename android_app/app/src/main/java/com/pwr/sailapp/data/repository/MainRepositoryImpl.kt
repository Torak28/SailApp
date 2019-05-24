package com.pwr.sailapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.pwr.sailapp.data.RentalSummary
import com.pwr.sailapp.data.sail.Centre
import com.pwr.sailapp.data.sail.Equipment
import com.pwr.sailapp.data.sail.Rental
import com.pwr.sailapp.data.network.sail.SailNetworkDataSource
import com.pwr.sailapp.data.network.weather.DarkSkyApiService
import com.pwr.sailapp.data.network.weather.WeatherNetworkDataSource
import com.pwr.sailapp.utils.DateUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// https://developer.android.com/jetpack/docs/guide
// https://medium.com/androiddevelopers/coroutines-on-android-part-i-getting-the-background-3e0e54d20bb

class MainRepositoryImpl(
    private val sailNetworkDataSource: SailNetworkDataSource,
    // private val weatherNetworkDataSource: WeatherNetworkDataSource? = null
    private val darkSkyApiService: DarkSkyApiService? = null
) : MainRepository {

    private val centres = MutableLiveData<ArrayList<Centre>>()
    private val allCentreGear = MutableLiveData<ArrayList<Equipment>>()
    private val allUserRentals = MutableLiveData<ArrayList<Rental>>()
    private val rentalSummaries = MutableLiveData<ArrayList<RentalSummary>>()

    init {
        // observe forever (repos don't have lifecycle) changes in live data (responses)
        sailNetworkDataSource.apply {
            downloadedCentres.observeForever {
                centres.postValue(it.centres)
            }
            downloadedAllCentreGear.observeForever {
                allCentreGear.postValue(it.gear)
            }
            downloadedAllUserRentals.observeForever {
                allUserRentals.postValue(it.rentals)
            }
        }

    }

    override suspend fun getAllUserRentals(userID: Int): LiveData<ArrayList<Rental>> {
        // create a block that will run on the IO dispatcher
        return withContext(Dispatchers.IO) {
            fetchAllUserRentals(userID)
            allUserRentals
        }
    }

    override suspend fun getAllCentreGear(centreID: Int): LiveData<ArrayList<Equipment>> {
        return withContext(Dispatchers.IO) {
            fetchAllCentreGear(centreID)
            allCentreGear
        }
    }

    override suspend fun getCentres(): LiveData<ArrayList<Centre>> {
        return withContext(Dispatchers.IO) {
            fetchCentres()
            centres
        }
    }

    suspend fun getRentalSummary(rental: Rental) : RentalSummary{
        if(darkSkyApiService == null){
            Log.e("getRentalSummary", "darkSkyApiService = null")
            return RentalSummary(rental, null)
        }
        if(rental.timestampSecs == null){
            Log.e("getRentalSummary", "rental.timestamp = null")
            return RentalSummary(rental, null)
        }
        if(rental.rentStartDate == null){
            Log.e("getRentalSummary", "rental.rentStartDate= null")
            return RentalSummary(rental, null)
        }
        return if(DateUtil.isForecastAvailable(rental.rentStartDate)){
            withContext(Dispatchers.IO){
                val forecast = darkSkyApiService.getForecast(rental.latitude, rental.longitude, rental.timestampSecs!!).await()
                RentalSummary(rental, forecast.currently)
            }
        } else RentalSummary(rental, null)

    }

    private suspend fun fetchCentres() {
        sailNetworkDataSource.fetchCentres()
    }

    private suspend fun fetchAllCentreGear(centreID: Int) {
        sailNetworkDataSource.fetchAllCentreGear(centreID)
    }

    private suspend fun fetchAllUserRentals(rentalID: Int) {
        sailNetworkDataSource.fetchAllUserRentals(rentalID)
    }
}