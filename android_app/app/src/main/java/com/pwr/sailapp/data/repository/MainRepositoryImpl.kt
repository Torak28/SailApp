package com.pwr.sailapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pwr.sailapp.data.Centre
import com.pwr.sailapp.data.Equipment
import com.pwr.sailapp.data.Rental
import com.pwr.sailapp.data.network.sail.SailAppApiService
import com.pwr.sailapp.data.network.sail.SailNetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// https://developer.android.com/jetpack/docs/guide

class MainRepositoryImpl(
    private val sailNetworkDataSource: SailNetworkDataSource
) : MainRepository {

    private val centres = MutableLiveData<ArrayList<Centre>>()
    private val allCentreGear = MutableLiveData<ArrayList<Equipment>>()
    private val allUserRentals = MutableLiveData<ArrayList<Rental>>()

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

    private suspend fun fetchCentres(){ sailNetworkDataSource.fetchCentres() }
    private suspend fun fetchAllCentreGear(centreID: Int){ sailNetworkDataSource.fetchAllCentreGear(centreID) }
    private suspend fun fetchAllUserRentals(rentalID: Int){ sailNetworkDataSource.fetchAllUserRentals(rentalID) }
}