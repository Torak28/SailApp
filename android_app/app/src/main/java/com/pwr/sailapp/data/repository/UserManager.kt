package com.pwr.sailapp.data.repository

import androidx.lifecycle.LiveData
import com.pwr.sailapp.data.sail.AuthenticationState
import com.pwr.sailapp.data.sail.RegistrationState
import com.pwr.sailapp.data.sail.User

interface UserManager {
    val authStatus : LiveData<AuthenticationState>
    val registerStatus : LiveData<RegistrationState>
    val currentUser: LiveData<User>
    val authToken: LiveData<String>
    val refreshToken: LiveData<String>

    suspend fun loginUser(email: String, password: String)//:LiveData<User> // return: user with auth token field but without password
    suspend fun refreshToken(refreshToken: String)
    suspend fun registerUser(user: User)//:LiveData<RegistrationState> // return: message if user is correctly registered
    fun logoutUser()//:LiveData<AuthenticationState>  // return: message if user is correctly logged out
}