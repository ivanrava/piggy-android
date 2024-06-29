package dev.ivanravasi.piggy.api.piggy.bodies.errors

import com.google.gson.annotations.SerializedName
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Beneficiary
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Budget
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Category

data class PropertyVariationValidationError(
    @SerializedName("message")
    var message: String,
    @SerializedName("errors")
    var errors: Errors
) {
    data class Errors(
        @SerializedName("property_id")
        val propertyId: List<String?> = listOf(null),
        @SerializedName("date")
        val date: List<String?> = listOf(null),
        @SerializedName("type")
        val type: List<String?> = listOf(null),
        @SerializedName("amount")
        val amount: List<String?> = listOf(null),
        @SerializedName("notes")
        val notes: List<String?> = listOf(null),
    )
}
