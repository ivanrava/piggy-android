package dev.ivanravasi.piggy.api.piggy.bodies.requests

import com.google.gson.annotations.SerializedName
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Budget

data class ChartRequest(
    @SerializedName("kind")
    var kind: String,
    @SerializedName("interval")
    var interval: String,
    @SerializedName("stat")
    var stat: String,
    @SerializedName("filter")
    var filter: String,
    @SerializedName("filter_id")
    var filterId: Long?,
    @SerializedName("filter_type")
    var filterType: String? = null,
)