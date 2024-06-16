package dev.ivanravasi.piggy.api.piggy.bodies.requests

import com.google.gson.annotations.SerializedName
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Beneficiary
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Category

data class TransferRequest(
    @SerializedName("from_account_id")
    val fromAccountId: Long,
    @SerializedName("to_account_id")
    val toAccountId: Long,
    @SerializedName("date")
    val date: String,
    @SerializedName("amount")
    val amount: String,
    @SerializedName("notes")
    val notes: String?,
    @SerializedName("checked")
    val checked: Boolean,
)
