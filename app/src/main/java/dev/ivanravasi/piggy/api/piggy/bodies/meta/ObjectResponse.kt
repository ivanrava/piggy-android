package dev.ivanravasi.piggy.api.piggy.bodies.meta

import com.google.gson.annotations.SerializedName

data class ObjectResponse<T>(
    @SerializedName("data")
    val data: T
)
