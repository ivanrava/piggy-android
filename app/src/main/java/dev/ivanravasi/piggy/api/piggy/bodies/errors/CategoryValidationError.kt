package dev.ivanravasi.piggy.api.piggy.bodies.errors

import com.google.gson.annotations.SerializedName
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Budget

data class CategoryValidationError(
    @SerializedName("message")
    var message: String,
    @SerializedName("errors")
    var errors: Errors
) {
    data class Errors(
        @SerializedName("name")
        var name: List<String?> = listOf(null),
        @SerializedName("type")
        var type: List<String?> = listOf(null),
        @SerializedName("icon")
        var icon: List<String?> = listOf(null),
        @SerializedName("parent_category_id")
        var parentCategoryId: List<String?> = listOf(null),
        @SerializedName("virtual")
        var virtual: List<String?> = listOf(null),
        @SerializedName("budget_overall")
        var budgetOverall: List<String?> = listOf(null),
        @SerializedName("budget")
        var budget: BudgetErrors = BudgetErrors(
            listOf(null),
            listOf(null),
            listOf(null),
            listOf(null),
            listOf(null),
            listOf(null),
            listOf(null),
            listOf(null),
            listOf(null),
            listOf(null),
            listOf(null),
            listOf(null),
        )
    ) {
        data class BudgetErrors(
            @SerializedName("jan")
            var jan: List<String?> = listOf(null),
            @SerializedName("feb")
            var feb: List<String?> = listOf(null),
            @SerializedName("mar")
            var mar: List<String?> = listOf(null),
            @SerializedName("apr")
            var apr: List<String?> = listOf(null),
            @SerializedName("may")
            var may: List<String?> = listOf(null),
            @SerializedName("jun")
            var jun: List<String?> = listOf(null),
            @SerializedName("jul")
            var jul: List<String?> = listOf(null),
            @SerializedName("aug")
            var aug: List<String?> = listOf(null),
            @SerializedName("sep")
            var sep: List<String?> = listOf(null),
            @SerializedName("oct")
            var oct: List<String?> = listOf(null),
            @SerializedName("nov")
            var nov: List<String?> = listOf(null),
            @SerializedName("dec")
            var dec: List<String?> = listOf(null),
        )
    }
}