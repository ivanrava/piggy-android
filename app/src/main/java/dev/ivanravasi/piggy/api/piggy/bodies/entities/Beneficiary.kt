package dev.ivanravasi.piggy.api.piggy.bodies.entities

import com.google.gson.annotations.SerializedName

data class Beneficiary(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("img")
    val img: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("transactions")
    val transactions: List<String>
)
