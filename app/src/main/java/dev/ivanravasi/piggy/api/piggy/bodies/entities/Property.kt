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
    val variations: List<PropertyVariation>
) {
    data class PropertyVariation(
        @SerializedName("id")
        val id: Long,
        @SerializedName("property_id")
        val propertyId: Long,
        @SerializedName("date")
        val date: String,
        @SerializedName("amount")
        val amount: String,
        @SerializedName("notes")
        val notes: String?,
        @SerializedName("type")
        private val type: String,
        @SerializedName("value")
        val value: String,
        @SerializedName("created_at")
        val createdAt: String,
    ) {
        fun type(): CategoryType {
            return when(type) {
                "in" -> CategoryType.IN
                else -> CategoryType.OUT
            }
        }
    }
}
