package com.pwr.sailapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pwr.sailapp.data.Centre
import com.pwr.sailapp.data.Equipment
import com.pwr.sailapp.data.Rental
import com.pwr.sailapp.data.network.sail.SailNetworkDataSource

// https://developer.android.com/jetpack/docs/guide

class MainRepositoryImpl(
    private val sailNetworkDataSource: SailNetworkDataSource
) : MainRepository {

    init {
        // observe forever (repos don't have lifecycle) changes in live data
        sailNetworkDataSource.apply {
            downloadedCentres.observeForever {
                // TODO ...
            }
            downloadedAllCentreGear.observeForever {
                // TODO ...
            }
            downloadedAllUserRentals.observeForever {
                // TODO ...
            }
        }
    }

    override suspend fun getAllUserRentals(userID: Int): LiveData<ArrayList<Rental>> {
        sailNetworkDataSource.fetchAllUserRentals(userID)
        //val result = sailNetworkDataSource.downloadedAllUserRentals
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getAllCentreGear(centreID: Int): LiveData<ArrayList<Equipment>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getCentres(): LiveData<ArrayList<Centre>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private suspend fun fetchCentres(){ sailNetworkDataSource.fetchCentres() }
}