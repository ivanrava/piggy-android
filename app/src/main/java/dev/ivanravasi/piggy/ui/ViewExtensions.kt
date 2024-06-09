package dev.ivanravasi.piggy.ui

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.iconify.loadIconify
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Account
import dev.ivanravasi.piggy.databinding.CardAccountBinding
import java.text.DateFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Currency

fun TextView.setCurrency(value: Double, colorize: Boolean = false) {
    val currencyFormatter: NumberFormat = NumberFormat.getCurrencyInstance()
    currencyFormatter.maximumFractionDigits = 2
    currencyFormatter.minimumFractionDigits = 2
    currencyFormatter.currency = Currency.getInstance("EUR")
    text = currencyFormatter.format(value)
    if (colorize) {
        val colorId = if (value < 0) R.color.out_value else R.color.in_value
        setTextColor(ContextCompat.getColor(this.context, colorId))
    }
}

fun TextView.setDate(dateString: String) {
    val date = SimpleDateFormat("yyyy-MM-dd").parse(dateString)
    val dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM)
    text = dateFormat.format(date!!)
}

private fun isColorDark(hex: String): Boolean {
    val brightness = Math.round(
        ((Integer.parseInt(hex.substring(0,2), 16) * 299) +
                (Integer.parseInt(hex.substring(2,4), 16) * 587) +
                (Integer.parseInt(hex.substring(4,6), 16) * 114)) / 1000.0)

    return brightness > 148
}

fun accountTextColor(context: Context, hex: String): Int {
    return ContextCompat.getColor(context, if (isColorDark(hex)) {
        R.color.account_text_dark
    } else {
        R.color.account_text_light
    })
}

fun CardAccountBinding.setAccount(account: Account, navController: NavController) {
    this.cardAccount.setCardBackgroundColor(Color.parseColor(account.color))

    val color = accountTextColor(this.root.context, account.color.substring(1))
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

fun Fragment.backWithSnackbar(viewRef: View, message: String) {
    Snackbar.make(
        viewRef,
        message,
        Snackbar.LENGTH_SHORT
    ).setAnchorView(R.id.bottom_bar)
        .show()
    findNavController().popBackStack()
}

fun TextView.afterTextChangedDebounced(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        var timer: CountDownTimer? = null

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(editable: Editable?) {
            timer?.cancel()
            timer = object : CountDownTimer(1000, 1500) {
                override fun onTick(millisUntilFinished: Long) {}
                override fun onFinish() {
                    afterTextChanged.invoke(editable.toString())
                }
            }.start()
        }
    })
}