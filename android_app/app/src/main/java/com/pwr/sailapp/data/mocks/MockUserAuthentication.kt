package com.pwr.sailapp.data.mocks

object MockUserAuthentication {
    val CORRECT_EMAIL = "jan@gmail.com"
    val CORRECT_PASSWORD = "abcd1234"

    fun authenticateUser(email: String, password: String):Boolean{
        return (email == CORRECT_EMAIL && password== CORRECT_PASSWORD)
    }
}