package dev.ivanravasi.piggy.ui.beneficiaries

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BeneficiariesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is beneficiaries Fragment"
    }
    val text: LiveData<String> = _text
}