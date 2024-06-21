package dev.ivanravasi.piggy.api.piggy.bodies.entities

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type

sealed class CategoryBudget<T>(val value: T) {
    class Yearly(value: String) : CategoryBudget<String>(value)
    class Monthly(value: Budget) : CategoryBudget<Budget>(value)
}

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
    var parent: Category?,
    @SerializedName("parent_category_id")
    val parentCategoryId: Long?,
    @SerializedName("children")
    var children: List<Category>,
    @SerializedName("expenditures")
    var expenditures: Budget,
    @SerializedName("budget")
    var budget: CategoryBudget<*>
)

class BudgetDeserializer: JsonDeserializer<CategoryBudget<*>> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): CategoryBudget<*> {
        val str = json!!.toString()
        var budgetRepr: CategoryBudget<*>
        try {
            val budget = Gson().fromJson(str, Budget::class.java)
            budgetRepr = CategoryBudget.Monthly(budget)
        } catch (exc: JsonSyntaxException) {
            budgetRepr = CategoryBudget.Yearly(str.substring(1, str.length-2))
        }
        return budgetRepr
    }
}