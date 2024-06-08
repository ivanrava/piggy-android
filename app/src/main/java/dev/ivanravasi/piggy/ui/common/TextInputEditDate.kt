package dev.ivanravasi.piggy.ui.common

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

class TextInputEditDate(
    context: Context,
    attrs: AttributeSet
): TextInputEditText(context, attrs) {
    private val listeners: MutableList<(date: Long) -> Unit> = mutableListOf()
    private val dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM)
    private var submittableDate: String = ""
    private val submittableDateFormat = SimpleDateFormat("yyyy-MM-dd")

    init {
        inputType = AUTOFILL_TYPE_DATE
        setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select variation date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()
            getFragmentManager()?.let { fragmentManager ->
                datePicker.show(fragmentManager, "DatePicker")
            }
            datePicker.addOnPositiveButtonClickListener { dateLong ->
                setText(dateFormat.format(Date(dateLong)))
                submittableDate = submittableDateFormat.format(Date(dateLong))
                listeners.forEach { it(dateLong) }
            }
        }
    }

    private fun getFragmentManager(ctx: Context = context): FragmentManager? {
        return when (ctx) {
            is FragmentActivity -> ctx.supportFragmentManager
            is ContextThemeWrapper -> getFragmentManager(ctx.baseContext)
            is android.view.ContextThemeWrapper -> getFragmentManager(ctx.baseContext)
            else -> null
        }
    }

    fun setOnSelectedDateListener(listener: (date: Long) -> Unit) {
        listeners.add(listener)
    }

    fun setToday() {
        submittableDate = submittableDateFormat.format(Date())
        setText(dateFormat.format(Date()))
    }

    fun date(): String {
        return submittableDate
    }
}