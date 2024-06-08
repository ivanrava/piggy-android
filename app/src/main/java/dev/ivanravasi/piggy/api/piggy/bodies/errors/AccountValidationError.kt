package dev.ivanravasi.piggy.api.piggy.bodies.errors

import com.google.gson.annotations.SerializedName

data class AccountValidationError(
    @SerializedName("message")
    var message: String,
    @SerializedName("errors")
    var errors: Errors
) {
    data class Errors(
        @SerializedName("account_type_id")
        var accountTypeId: List<String?> = listOf(null),
        @SerializedName("name")
        var name: List<String?> = listOf(null),
        @SerializedName("icon")
        var icon: List<String?> = listOf(null),
        @SerializedName("color")
        var color: List<String?> = listOf(null),
        @SerializedName("opening")
        var opening: List<String?> = listOf(null),
        @SerializedName("closing")
        var closing: List<String?> = listOf(null),
        @SerializedName("description")
        var description: List<String?> = listOf(null),
        @SerializedName("initial_balance")
        var initialBalance: List<String?> = listOf(null),
    )
}
