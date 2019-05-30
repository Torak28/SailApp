package com.pwr.sailapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pwr.sailapp.data.sail.User
import com.pwr.sailapp.data.sail.AuthenticationState
import com.pwr.sailapp.data.sail.RegistrationState
import com.pwr.sailapp.data.sail.UserCredentials
import com.pwr.sailapp.data.network.sail.SailAppApiService
import com.pwr.sailapp.data.network.sail.response.REGISTER_OK_MESSAGE
import com.pwr.sailapp.internal.ErrorCodeException
import com.pwr.sailapp.internal.NoConnectivityException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

const val NO_TOKEN = ""

class UserManagerImpl(
    private val sailAppApiService: SailAppApiService
) : UserManager {

    override val authStatus: LiveData<AuthenticationState>
        get() = _authStatus

    override val registerStatus: LiveData<RegistrationState>
        get() = _registerStatus

    override val authToken: LiveData<String>
        get() = _accessToken

    override val refreshToken: LiveData<String>
        get() = _refreshToken

    override val currentUser: LiveData<User>
        get() = _currentUser

    private val _authStatus = MutableLiveData<AuthenticationState>()
    private val _registerStatus = MutableLiveData<RegistrationState>()
    private val _currentUser = MutableLiveData<User>()
    private val _accessToken = MutableLiveData<String>()
    private val _refreshToken = MutableLiveData<String>()


    override suspend fun loginUser(email: String, password: String) {
        // val userWithToken = sailAppApiService.loginUser()
        withContext(Dispatchers.IO) {
            try {
                val fetchedLoginResponse = sailAppApiService.loginUserAsync(
                    email = email,
                    password = password
                ).await()

                if(fetchedLoginResponse.access_token != null && fetchedLoginResponse.refresh_token != null){
                    _authStatus.postValue(AuthenticationState.AUTHENTICATED)
                    _accessToken.postValue(fetchedLoginResponse.access_token)
                    _refreshToken.postValue(fetchedLoginResponse.refresh_token)
                }
                else{
                    _authStatus.postValue(AuthenticationState.UNAUTHENTICATED)
                    _accessToken.postValue(NO_TOKEN)
                    _refreshToken.postValue(NO_TOKEN)
                }

            }
            catch (e: ErrorCodeException){
                Log.e("Response ", "Returned error code")
            }

            catch (e: NoConnectivityException) {
                Log.e("Connectivity", "No internet connection")
            }

        }
    }

    override suspend fun refreshToken(refreshToken: String) {
        withContext(Dispatchers.IO){
            try {
                val fetchedRefreshTokenResponse = sailAppApiService.refreshTokenAsync(refreshToken).await()
                if(fetchedRefreshTokenResponse.access_token != null){
                    _authStatus.postValue(AuthenticationState.AUTHENTICATED)
                    _accessToken.postValue(fetchedRefreshTokenResponse.access_token)
                }
                else{
                    _authStatus.postValue(AuthenticationState.UNAUTHENTICATED)
                    _accessToken.postValue(fetchedRefreshTokenResponse.access_token)
                }
            } catch (e: NoConnectivityException) {
                Log.e("Connectivity", "No internet connection")
            }
        }
    }

    override fun logoutUser() {
        _authStatus.value = AuthenticationState.UNAUTHENTICATED
        _accessToken.value = NO_TOKEN
        _refreshToken.value = NO_TOKEN
    }

    override suspend fun registerUser(user: User) {
        withContext(Dispatchers.IO) {
            try {
                val fetchedRegistrationResponse = sailAppApiService.registerUserAsync(
                    firstName = user.firstName,
                    lastName = user.lastName,
                    email = user.email,
                    phoneNumber = user.phoneNumber,
                    password = user.password,
                    role = "user"
                ).await()

                when (fetchedRegistrationResponse.msg) {
                    REGISTER_OK_MESSAGE -> _registerStatus.postValue(RegistrationState.OK)
                    else -> _registerStatus.postValue(RegistrationState.FAILED)
                }
            } catch (e: NoConnectivityException) {
                Log.e("Connectivity", "No internet connection")
            }
        }
    }



}