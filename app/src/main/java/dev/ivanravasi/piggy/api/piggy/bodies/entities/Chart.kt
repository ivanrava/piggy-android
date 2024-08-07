package dev.ivanravasi.piggy.api.piggy.bodies.entities

import com.google.gson.annotations.SerializedName

data class Chart(
    @SerializedName("id")
    val id: Long,
    @SerializedName("kind")
    val kind: String,
    @SerializedName("interval")
    val interval: String,
    @SerializedName("stat")
    val stat: String,
    @SerializedName("filter")
    val filter: String,
    @SerializedName("filter_id")
    val filterId: Long?,
    @SerializedName("filter_group")
    val filterGroup: String?,
    @SerializedName("favorite")
    val favorite: Boolean,
) {
    fun getChartKind(): ChartKind {
        return when (kind) {
            "list" -> ChartKind.LIST
            "pie" -> ChartKind.PIE
            else -> ChartKind.LINE_BAR
        }
    }
}

enum class ChartKind {
    LINE_BAR, LIST, PIE
}
