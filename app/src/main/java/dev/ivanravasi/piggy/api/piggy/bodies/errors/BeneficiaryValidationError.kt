package dev.ivanravasi.piggy.api.piggy.bodies.errors

import com.google.gson.annotations.SerializedName

data class BeneficiaryValidationError(
    @SerializedName("message")
    var message: String,
    @SerializedName("errors")
    var errors: Errors
) {
    data class Errors(
        @SerializedName("name")
        var name: List<String?> = listOf(null),
        @SerializedName("img")
        var img: List<String?> = listOf(null),
    )
}
