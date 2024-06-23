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
