package dev.ivanravasi.piggy.api.piggy.bodies

import com.google.gson.annotations.SerializedName

data class PropertyValidationError(
    @SerializedName("message")
    var message: String,
    @SerializedName("errors")
    var errors: Errors
) {
    data class Errors(
        @SerializedName("name")
        var name: List<String?> = listOf(null),
        @SerializedName("icon")
        var icon: List<String?> = listOf(null),
        @SerializedName("initial_value")
        var initialValue: List<String?> = listOf(null),
        @SerializedName("description")
        var description: List<String?> = listOf(null),
    )
}
