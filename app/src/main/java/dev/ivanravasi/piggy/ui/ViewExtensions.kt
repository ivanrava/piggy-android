package dev.ivanravasi.piggy.ui

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.iconify.loadIconify
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Account
import dev.ivanravasi.piggy.databinding.CardAccountBinding
import java.text.NumberFormat
import java.util.Currency

fun TextView.setCurrency(value: Double) {
    val currencyFormatter: NumberFormat = NumberFormat.getCurrencyInstance()
    currencyFormatter.maximumFractionDigits = 2
    currencyFormatter.minimumFractionDigits = 2
    currencyFormatter.currency = Currency.getInstance("EUR")
    text = currencyFormatter.format(value)
}

fun CardAccountBinding.setAccount(account: Account, navController: NavController) {
    fun isColorDark(hex: String): Boolean {
        val brightness = Math.round(
            ((Integer.parseInt(hex.substring(0,2), 16) * 299) +
                    (Integer.parseInt(hex.substring(2,4), 16) * 587) +
                    (Integer.parseInt(hex.substring(4,6), 16) * 114)) / 1000.0)

        return brightness > 148
    }

    this.cardAccount.setCardBackgroundColor(Color.parseColor(account.color))

    val color = ContextCompat.getColor(this.root.context, if (isColorDark(account.color.substring(1))) {
        R.color.account_text_dark
    } else {
        R.color.account_text_light
    })
    this.accountIcon.loadIconify(account.icon, color)
    this.accountName.text = account.name
    this.accountType.text = account.type

    this.accountName.setTextColor(color)
    this.accountType.setTextColor(color)
    this.cardAccount.setOnClickListener {
        val bundle = Bundle()
        bundle.putLong("id", account.id)
        navController.navigate(R.id.navigation_operations, bundle)
    }
}