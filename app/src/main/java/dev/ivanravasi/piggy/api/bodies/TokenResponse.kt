package dev.ivanravasi.piggy.api.bodies

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("token")
    val token: String
)
