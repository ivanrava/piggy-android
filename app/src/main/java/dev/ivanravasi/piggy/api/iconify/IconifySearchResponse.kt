package dev.ivanravasi.piggy.api.iconify

import com.google.gson.annotations.SerializedName

data class IconifySearchResponse(
    @SerializedName("icons")
    val icons: List<String>,
    @SerializedName("total")
    val total: Int,
    @SerializedName("limit")
    val limit: Int,
    @SerializedName("start")
    val start: Int,
)
