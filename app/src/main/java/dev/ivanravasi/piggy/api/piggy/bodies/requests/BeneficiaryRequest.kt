package dev.ivanravasi.piggy.api.piggy.bodies.requests

import com.google.gson.annotations.SerializedName

data class BeneficiaryRequest(
    @SerializedName("name")
    var name: String,
    @SerializedName("img")
    var img: String?,
)
