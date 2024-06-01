package dev.ivanravasi.piggy.api.piggy.bodies.entities

import com.google.gson.annotations.SerializedName

data class Transfer(
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
    @SerializedName("to")
    val to: Account?,
    @SerializedName("from")
    val from: Account?,
) : Operation {
    override fun getOperationId(): Long {
        return id
    }

    override fun type(): OperationType {
        return OperationType.TRANSFER
    }

    override fun rawDate(): String {
        return date
    }
}
