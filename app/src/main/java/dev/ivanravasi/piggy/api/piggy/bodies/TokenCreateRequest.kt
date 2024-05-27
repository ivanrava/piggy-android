package dev.ivanravasi.piggy.api.piggy.bodies

import com.google.gson.annotations.SerializedName

data class TokenCreateRequest(
    @SerializedName("email")
    var email: String,
    @SerializedName("password")
    var password: String,
    @SerializedName("device_name")
    var deviceName: String,
)
