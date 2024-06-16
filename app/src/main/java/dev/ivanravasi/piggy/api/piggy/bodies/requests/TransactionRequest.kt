package dev.ivanravasi.piggy.api.piggy.bodies.requests

import com.google.gson.annotations.SerializedName
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Beneficiary
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Category

data class TransactionRequest(
    @SerializedName("account_id")
    val accountId: Long,
    @SerializedName("beneficiary")
    val beneficiary: Beneficiary,
    @SerializedName("category")
    val category: Category,
    @SerializedName("date")
    val date: String,
    @SerializedName("checked")
    val checked: Boolean,
    @SerializedName("amount")
    val amount: String,
    @SerializedName("notes")
    val notes: String?,
)
