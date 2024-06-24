package dev.ivanravasi.piggy.api.piggy.bodies.entities

import com.google.gson.annotations.SerializedName

data class Stat(
    @SerializedName("time")
    val time: String,
    @SerializedName("avg")
    val avg: String,
    @SerializedName("count")
    val count: Int,
    @SerializedName("sum")
    val sum: String,
    @SerializedName("min")
    val min: String,
    @SerializedName("max")
    val max: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("name")
    val name: String?,
    @SerializedName("color")
    val color: String?,
    @SerializedName("icon")
    val icon: String?,
    @SerializedName("id")
    val id: Long?,
    @SerializedName("img")
    val img: String?,
) {
    fun getStat(stat: String): Float {
        return when (stat) {
            "avg" -> avg.toFloat()
            "count" -> count.toFloat()
            "sum" -> sum.toFloat()
            "min" -> min.toFloat()
            "max" -> max.toFloat()
            else -> sum.toFloat()
        }
    }
}
