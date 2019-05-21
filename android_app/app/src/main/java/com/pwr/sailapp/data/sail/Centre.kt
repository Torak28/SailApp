package com.pwr.sailapp.data.sail

import com.google.gson.annotations.SerializedName


data class Centre(@SerializedName("id")
                  val ID:Int,
                  val name:String,
                  var rating: Double,
                  val location: String,
                  @SerializedName("latitude")
                  val coordinateX:Double,
                  @SerializedName("longitude")
                  val coordinateY:Double,
                  @SerializedName("photo_url")
                  var photoURL:String,
                  var phone:Int,
                  var distance:Double = Double.POSITIVE_INFINITY){

    override fun toString(): String {
        return "$ID $name $rating $coordinateX $coordinateY"
    }
}
