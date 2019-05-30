package com.pwr.sailapp.data.mocks

import com.pwr.sailapp.data.sail.User

object MockUsers {
    val usersList = ArrayList<User>()
    init {
        usersList.add(
            User(
                "Jan",
                "Kowalski",
                "jan@gmail.com",
                "abcd1234",
                "511754113"
            )
        )
        usersList.add(
            User(
                "Krzysztof",
                "Nowak",
                "chris@gmail.com",
                "abcd1234",
                "511754113"
            )
        )
    }
}