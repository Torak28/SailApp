package com.pwr.sailapp.data

import android.provider.ContactsContract

object MockUserAuthentication {
    val CORRECT_EMAIL = "jan@gmail.com"
    val CORRECT_PASSWORD = "abcd1234"

    fun authenticateUser(email: String, password: String):Boolean{
        return (email == CORRECT_EMAIL && password== CORRECT_PASSWORD)
    }
}