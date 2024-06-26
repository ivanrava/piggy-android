package dev.ivanravasi.piggy.api.piggy.bodies.entities

import com.google.gson.annotations.SerializedName

data class Beneficiary(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("img")
    val img: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("transactions")
    val transactions: List<String>
) {
    fun type(): String {
        return if (img.startsWith("https://logo.clearbit.com")) {
            "Company"
        } else if (img == "bottts") {
            "People"
        } else {
            "Generic"
        }
    }
}
