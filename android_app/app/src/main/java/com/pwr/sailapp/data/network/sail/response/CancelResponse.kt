package com.pwr.sailapp.data.network.sail.response

data class CancelResponse (
    val msg: String
){
    companion object{
        const val CANCEL_OK_MSG = "Cancelation was successful."
    }
}