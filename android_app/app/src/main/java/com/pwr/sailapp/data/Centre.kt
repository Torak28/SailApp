package com.pwr.sailapp.data

import com.google.gson.annotations.SerializedName


data class Centre(@SerializedName("centre_id")
                  val ID:Int,
                  val name:String,
                  var rating: Double,
                  val location: String,
                  @SerializedName("coordinate_x")
                  val coordinateX:Double,
                  @SerializedName("coordinate_y")
                  val coordinateY:Double,
                  @SerializedName("photo_url")
                  var photoURL:String,
                  var phone:Int,
                  var distance:Double = Double.POSITIVE_INFINITY){

    override fun toString(): String {
        return "$ID $name $rating $coordinateX $coordinateY"
    }
}
