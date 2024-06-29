package dev.ivanravasi.piggy.api.piggy.bodies.requests

import com.google.gson.annotations.SerializedName
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Beneficiary
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Category

data class PropertyVariationRequest(
    @SerializedName("property_id")
    val propertyId: Long,
    @SerializedName("date")
    val date: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("amount")
    val amount: String,
    @SerializedName("notes")
    val notes: String?,
)
