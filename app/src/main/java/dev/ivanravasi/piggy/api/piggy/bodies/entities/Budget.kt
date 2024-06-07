package dev.ivanravasi.piggy.api.piggy.bodies.entities

import com.google.gson.annotations.SerializedName

data class Budget(
    @SerializedName("jan")
    var jan: String,
    @SerializedName("feb")
    var feb: String,
    @SerializedName("mar")
    var mar: String,
    @SerializedName("apr")
    var apr: String,
    @SerializedName("may")
    var may: String,
    @SerializedName("jun")
    var jun: String,
    @SerializedName("jul")
    var jul: String,
    @SerializedName("aug")
    var aug: String,
    @SerializedName("sep")
    var sep: String,
    @SerializedName("oct")
    var oct: String,
    @SerializedName("nov")
    var nov: String,
    @SerializedName("dec")
    var dec: String,
)
