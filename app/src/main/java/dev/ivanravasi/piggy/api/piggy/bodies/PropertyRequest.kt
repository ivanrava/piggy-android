package dev.ivanravasi.piggy.api.piggy.bodies

import com.google.gson.annotations.SerializedName

data class PropertyRequest(
    @SerializedName("name")
    var name: String,
    @SerializedName("icon")
    var icon: String,
    @SerializedName("initial_value")
    var initialValue: String,
    @SerializedName("description")
    var description: String?,
)
