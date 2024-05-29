package dev.ivanravasi.piggy.api.piggy.bodies.entities

import com.google.gson.annotations.SerializedName

data class Account(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("initial_balance")
    val initialBalance: String,
    @SerializedName("balance")
    val balance: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("account_type_id")
    val accountTypeId: Long,
    @SerializedName("color")
    val color: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("last_update")
    val lastUpdate: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("opening")
    val opening: String,
    @SerializedName("closing")
    val closing: String?,
)
