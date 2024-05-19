package dev.ivanravasi.piggy.api.bodies

import com.google.gson.annotations.SerializedName

data class TokenRequest(
    @SerializedName("email")
    var email: String,
    @SerializedName("password")
    var password: String,
    @SerializedName("device_name")
    var deviceName: String,
)
