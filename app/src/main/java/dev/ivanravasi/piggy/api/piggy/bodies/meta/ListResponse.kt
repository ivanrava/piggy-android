package dev.ivanravasi.piggy.api.piggy.bodies.meta

import com.google.gson.annotations.SerializedName

data class ListResponse<T>(
    @SerializedName("data")
    val data: List<T>
)
