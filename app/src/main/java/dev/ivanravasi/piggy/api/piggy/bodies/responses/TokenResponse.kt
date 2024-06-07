package dev.ivanravasi.piggy.api.piggy.bodies.responses

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("token")
    val token: String
)
