package dev.ivanravasi.piggy.ui

import android.widget.TextView
import java.text.NumberFormat
import java.util.Currency

fun TextView.setCurrency(value: Double) {
    val currencyFormatter: NumberFormat = NumberFormat.getCurrencyInstance()
    currencyFormatter.maximumFractionDigits = 2
    currencyFormatter.minimumFractionDigits = 2
    currencyFormatter.currency = Currency.getInstance("EUR")
    text = currencyFormatter.format(value)
}