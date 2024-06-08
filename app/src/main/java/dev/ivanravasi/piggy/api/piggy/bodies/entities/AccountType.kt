package dev.ivanravasi.piggy.api.piggy.bodies.entities

import com.google.gson.annotations.SerializedName

data class AccountType(
    @SerializedName("id")
    val id: Long,
    @SerializedName("type")
    val type: String,
)
