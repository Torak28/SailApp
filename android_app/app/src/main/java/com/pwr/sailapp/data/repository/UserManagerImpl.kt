package com.pwr.sailapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pwr.sailapp.data.User
import com.pwr.sailapp.data.network.sail.SailNetworkDataSource
import com.pwr.sailapp.data.AuthenticationState
import com.pwr.sailapp.data.RegistrationState
import com.pwr.sailapp.data.UserCredentials
import com.pwr.sailapp.data.network.sail.SailAppApiService
import com.pwr.sailapp.internal.NoConnectivityException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

const val REGISTER_OK_MESSAGE = "ok"
const val LOGIN_OK_MESSAGE = "ok"
const val LOGOUT_OK_MESSAGE = "ok"
const val NO_TOKEN = ""

class UserManagerImpl(
    private val sailAppApiService: SailAppApiService
) : UserManager {

    override val authStatus: LiveData<AuthenticationState>
        get() = _authStatus

    override val registerStatus: LiveData<RegistrationState>
        get() = _registerStatus

    override val currentUser: LiveData<User>
        get() = _currentUser

    private val _authStatus = MutableLiveData<AuthenticationState>()
    private val _registerStatus = MutableLiveData<RegistrationState>()
    private val _currentUser = MutableLiveData<User>()
    private val _token = MutableLiveData<String>()


    override suspend fun loginUser(email: String, password: String) {
        // val userWithToken = sailAppApiService.loginUser()
        withContext(Dispatchers.IO) {
            try {
                val fetchedLoginResponse = sailAppApiService.loginUserAsync(UserCredentials(email, password)).await()
                when (fetchedLoginResponse.message) {
                    LOGIN_OK_MESSAGE -> {
                        _authStatus.postValue(AuthenticationState.AUTHENTICATED)
                        _token.postValue(fetchedLoginResponse.token)
                    }
                    else -> {
                        _authStatus.postValue(AuthenticationState.UNAUTHENTICATED)
                        _token.postValue(NO_TOKEN)
                    }
                }
            } catch (e: NoConnectivityException) {
                Log.e("Connectivity", "No internet connection")
            }
        }
    }

    override suspend fun registerUser(user: User) {
        withContext(Dispatchers.IO) {
            try {
                val fetchedRegistrationResponse = sailAppApiService.registerUserAsync(user).await()
                when (fetchedRegistrationResponse.message) {
                    REGISTER_OK_MESSAGE -> _registerStatus.postValue(RegistrationState.OK)
                    else -> _registerStatus.postValue(RegistrationState.FAILED)
                }
            } catch (e: NoConnectivityException) {
                Log.e("Connectivity", "No internet connection")
            }
        }
    }

    override suspend fun logoutUser() {
        withContext(Dispatchers.IO){
            try {
                val fetchedLogoutUserResponse = sailAppApiService.logoutUserAsync().await()
                when (fetchedLogoutUserResponse.message) {
                    LOGOUT_OK_MESSAGE -> {
                        _authStatus.postValue(AuthenticationState.UNAUTHENTICATED)
                        _token.postValue(NO_TOKEN)
                    }
                    else -> _authStatus.postValue(AuthenticationState.AUTHENTICATED)
                }
            } catch (e: NoConnectivityException) {
                Log.e("Connectivity", "No internet connection")
            }
        }
    }

}