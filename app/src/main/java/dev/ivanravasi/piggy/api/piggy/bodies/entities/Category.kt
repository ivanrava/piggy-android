package dev.ivanravasi.piggy.api.piggy.bodies.entities

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("virtual")
    val virtual: Boolean,
    @SerializedName("parent")
    val parent: Category?,
    @SerializedName("parent_category_id")
    val parentCategoryId: Long,
    @SerializedName("children")
    val children: List<Category>,
    @SerializedName("expenditures")
    val expenditures: List<String>,
    @SerializedName("transactions")
    val transactions: List<String>,
    @SerializedName("budget")
    val budget: String
)
