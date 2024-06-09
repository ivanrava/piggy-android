package dev.ivanravasi.piggy.api.piggy.bodies.requests

import com.google.gson.annotations.SerializedName

data class AccountRequest(
    @SerializedName("name")
    var name: String,
    @SerializedName("icon")
    var icon: String?,
    @SerializedName("color")
    var color: String,
    @SerializedName("opening")
    var opening: String,
    @SerializedName("closing")
    var closing: String?,
    @SerializedName("initial_balance")
    var initialBalance: String,
    @SerializedName("description")
    var description: String?,
    @SerializedName("account_type_id")
    var accountTypeId: Long
)
