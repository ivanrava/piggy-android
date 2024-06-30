package dev.ivanravasi.piggy.api.piggy.bodies.errors

import com.google.gson.annotations.SerializedName
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Budget

data class ChartValidationError(
    @SerializedName("message")
    var message: String,
    @SerializedName("errors")
    var errors: Errors
) {
    data class Errors(
        @SerializedName("kind")
        var kind: List<String?> = listOf(null),
        @SerializedName("interval")
        var interval: List<String?> = listOf(null),
        @SerializedName("stat")
        var stat: List<String?> = listOf(null),
        @SerializedName("filter")
        var filter: List<String?> = listOf(null),
        @SerializedName("filter_id")
        var filterId: List<String?> = listOf(null),
        @SerializedName("filter_type")
        var filterType: List<String?> = listOf(null),
    )
}