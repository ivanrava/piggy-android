package dev.ivanravasi.piggy.api.piggy.bodies.requests

import com.google.gson.annotations.SerializedName
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Budget

data class CategoryRequest(
    @SerializedName("name")
    var name: String,
    @SerializedName("icon")
    var icon: String?,
    @SerializedName("type")
    var type: String,
    @SerializedName("parent_category_id")
    var parentCategoryId: Long?,
    @SerializedName("virtual")
    var virtual: Boolean?,
    @SerializedName("budget_overall")
    var budgetOverall: String?,
    @SerializedName("budget")
    var budget: Budget?,
)