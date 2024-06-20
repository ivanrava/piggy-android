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
//        @SerializedName("budget")
//        var budget: Budget
    )
}
