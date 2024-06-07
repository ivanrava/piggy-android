package dev.ivanravasi.piggy.ui.properties

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.ui.common.ApiViewModel
import java.text.DateFormat
import java.util.Date

class VariationViewModel(
    private val tokenRepository: TokenRepository
): ApiViewModel(tokenRepository) {
    private val _date = MutableLiveData<String>().apply {
        value = today()
    }
    val date: LiveData<String> = _date
    private val dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM)

    private fun today(): String {
        val dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM)
        return dateFormat.format(Date())
    }

    fun updateDate(dateNumeric: Long) {
        _date.value = dateFormat.format(Date(dateNumeric))
    }
}