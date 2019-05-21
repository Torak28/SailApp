package com.pwr.sailapp.data.mocks

import com.pwr.sailapp.data.sail.User

object MockUsers {
    val usersList = ArrayList<User>()
    init {
        usersList.add(
            User(
                1,
                "Jan",
                "Kowalski",
                "jan@gmail.com",
                "abcd1234",
                "511754113"
            )
        )
        usersList.add(
            User(
                2,
                "Krzysztof",
                "Nowak",
                "chris@gmail.com",
                "abcd1234",
                "511754113"
            )
        )
    }
}