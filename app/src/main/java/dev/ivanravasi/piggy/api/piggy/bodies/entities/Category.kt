package dev.ivanravasi.piggy.api.piggy.bodies.entities

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.JsonSyntaxException
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type


sealed class CategoryBudget<T>(val value: T) {
    class Yearly(value: String) : CategoryBudget<String>(value)
    class Monthly(value: Budget) : CategoryBudget<Budget>(value)
}

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Exclude

class AnnotationExclusionStrategy: ExclusionStrategy {
    override fun shouldSkipField(f: FieldAttributes): Boolean {
        return f.getAnnotation(Exclude::class.java) != null
    }

    override fun shouldSkipClass(clazz: Class<*>?): Boolean {
        return false
    }
}

data class Category(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("type")
    private val type: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("virtual")
    val virtual: Boolean,
    @SerializedName("parent")
    var parent: Category?,
    @SerializedName("parent_category_id")
    val parentCategoryId: Long?,
    @SerializedName("children")
    @Exclude var children: List<Category>,
    @SerializedName("expenditures")
    var expenditures: Budget?,
    @SerializedName("budget")
    var budget: CategoryBudget<*>?
) {
    fun type(): CategoryType {
        return when (type) {
            "in" -> CategoryType.IN
            else -> CategoryType.OUT
        }
    }

    fun budgetCurrentFill(): Double {
        return when (budget!!) {
            is CategoryBudget.Monthly -> expenditures!!.monthValue()
            is CategoryBudget.Yearly -> expenditures!!.sum()
        }
    }

    fun budgetMaximumFill(): Double {
        return when (budget!!) {
            is CategoryBudget.Monthly -> (budget as CategoryBudget.Monthly).value.monthValue()
            is CategoryBudget.Yearly -> (budget as CategoryBudget.Yearly).value.toDouble()
        }
    }

    fun fills(): List<Pair<Double, Double>> {
        val fills = mutableListOf<Pair<Double, Double>>()
        if (parentCategoryId == null)
            return fills

        // TODO: maybe replace with reflection
        when (budget) {
            is CategoryBudget.Yearly -> fills.add(Pair(expenditures!!.sum(), (budget as CategoryBudget.Yearly).value.toDouble()))
            is CategoryBudget.Monthly -> {
                for (month in listOf("jan","feb","mar","apr","may","jun","jul","aug","sep","oct","nov","dec")) {
                    fills.add(Pair(expenditures!!.monthValue(month), (budget as CategoryBudget.Monthly).value.monthValue(month)))
                }
            }
            null -> {} // Therefore list stays empty
        }
        return fills
    }

}

enum class CategoryType {
    IN, OUT
}

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

class BudgetSerializer: JsonSerializer<CategoryBudget<*>> {
    override fun serialize(
        src: CategoryBudget<*>?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        val obj = JsonObject()
        when (src) {
            is CategoryBudget.Yearly -> {
                return JsonPrimitive(src.value)
            }
            is CategoryBudget.Monthly -> {
                obj.addProperty("jan", src.value.jan)
                obj.addProperty("feb", src.value.feb)
                obj.addProperty("mar", src.value.mar)
                obj.addProperty("apr", src.value.apr)
                obj.addProperty("may", src.value.may)
                obj.addProperty("jun", src.value.jun)
                obj.addProperty("jul", src.value.jul)
                obj.addProperty("aug", src.value.aug)
                obj.addProperty("sep", src.value.sep)
                obj.addProperty("oct", src.value.oct)
                obj.addProperty("nov", src.value.nov)
                obj.addProperty("dec", src.value.dec)
            }
            null -> {}
        }
        return obj
    }

}