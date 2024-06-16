package dev.ivanravasi.piggy.api.piggy.bodies.errors

import com.google.gson.annotations.SerializedName
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Beneficiary
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Budget
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Category

data class TransactionValidationError(
    @SerializedName("message")
    var message: String,
    @SerializedName("errors")
    var errors: Errors
) {
    data class Errors(
        @SerializedName("account_id")
        val accountId: List<String?> = listOf(null),
        @SerializedName("beneficiary")
        val beneficiary: BeneficiaryValidationError.Errors = BeneficiaryValidationError.Errors(),
        @SerializedName("category")
        val category: CategoryValidationError.Errors = CategoryValidationError.Errors(),
        @SerializedName("date")
        val date: List<String?> = listOf(null),
        @SerializedName("checked")
        val checked: List<String?> = listOf(null),
        @SerializedName("amount")
        val amount: List<String?> = listOf(null),
        @SerializedName("notes")
        val notes: List<String?> = listOf(null),
    )
}
