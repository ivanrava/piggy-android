package dev.ivanravasi.piggy.api.piggy.bodies.requests

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("name")
    var name: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("password")
    var password: String,
    @SerializedName("password_confirmation")
    var passwordConfirmation: String,
    @SerializedName("device_name")
    var deviceName: String,
)
