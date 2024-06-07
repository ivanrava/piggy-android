package dev.ivanravasi.piggy.api.piggy.bodies.entities

import com.google.gson.annotations.SerializedName

data class Property(
    @SerializedName("id")
    val id: Long,
    @SerializedName("user_id")
    val userId: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("initial_value")
    val initialValue: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("value")
    val value: Double,
    @SerializedName("variations")
    val variations: List<String>
)
