package com.pwr.sailapp.data

import android.content.Context
import com.pwr.sailapp.data.network.sail.*
import com.pwr.sailapp.data.repository.MainRepository
import com.pwr.sailapp.data.repository.MainRepositoryImpl
import com.pwr.sailapp.data.repository.UserManager
import com.pwr.sailapp.data.repository.UserManagerImpl

// https://github.com/googlesamples/android-architecture-components/blob/master/BasicRxJavaSampleKotlin/app/src/main/java/com/example/android/observability/persistence/UsersDatabase.kt

class DataProvider private constructor(context: Context) {

    private val connectivityInterceptor : ConnectivityInterceptor = ConnectivityInterceptorImpl(context)
    private val sailAppApiService : SailAppApiService = SailAppApiService.invoke(connectivityInterceptor)
    private val sailNetworkDataSource : SailNetworkDataSource = SailNetworkDataSourceImpl(sailAppApiService)

    val userManager: UserManager = UserManagerImpl(sailAppApiService)
    val repository: MainRepository = MainRepositoryImpl(sailNetworkDataSource)

    companion object{
        @Volatile private var INSTANCE: DataProvider? = null

        fun getInstance(context: Context): DataProvider =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: initializeDataProvider(context).also { INSTANCE = it }
            }

        private fun initializeDataProvider(context: Context) = DataProvider(context)
    }
}