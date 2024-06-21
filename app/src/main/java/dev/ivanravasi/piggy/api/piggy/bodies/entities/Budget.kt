package dev.ivanravasi.piggy.api.piggy.bodies.entities

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

data class Budget(
    @SerializedName("jan")
    var jan: String,
    @SerializedName("feb")
    var feb: String,
    @SerializedName("mar")
    var mar: String,
    @SerializedName("apr")
    var apr: String,
    @SerializedName("may")
    var may: String,
    @SerializedName("jun")
    var jun: String,
    @SerializedName("jul")
    var jul: String,
    @SerializedName("aug")
    var aug: String,
    @SerializedName("sep")
    var sep: String,
    @SerializedName("oct")
    var oct: String,
    @SerializedName("nov")
    var nov: String,
    @SerializedName("dec")
    var dec: String,
) {
    fun sum(): Double {
        return jan.toDouble() + feb.toDouble() + mar.toDouble() + apr.toDouble() +
                may.toDouble() + jun.toDouble() + jul.toDouble() + aug.toDouble() +
                sep.toDouble() + oct.toDouble() + nov.toDouble() + dec.toDouble()
    }

    private fun currentMonthName(): String {
        return SimpleDateFormat("MMM", Locale.UK).format(Calendar.getInstance().time).lowercase()
    }

    fun currentMonthValue(): Double {
        val value = Budget::class.memberProperties
            .first { it.name == currentMonthName() }
            .also { it.isAccessible = true }
            .getter(this)
        return (value as String).toDouble()
    }
}
