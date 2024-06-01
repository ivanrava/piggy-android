package dev.ivanravasi.piggy.api.piggy.bodies.entities

import com.google.gson.annotations.SerializedName

data class Transaction(
    @SerializedName("id")
    val id: Long,
    @SerializedName("date")
    val date: String,
    @SerializedName("amount")
    val amount: String,
    @SerializedName("notes")
    val notes: String?,
    @SerializedName("checked")
    val checked: Boolean,
    @SerializedName("category")
    val category: Category,
    @SerializedName("beneficiary")
    val beneficiary: Beneficiary,
    @SerializedName("account")
    val account: Account,
) : Operation {
    override fun getOperationId(): Long {
        return id
    }

    override fun type(): OperationType {
        return OperationType.TRANSACTION
    }

    override fun rawDate(): String {
        return date
    }
}
