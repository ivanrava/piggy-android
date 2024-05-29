package dev.ivanravasi.piggy.ui.beneficiaries

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Beneficiary
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.ui.ApiViewModel
import kotlinx.coroutines.launch

class BeneficiariesViewModel(
    private val tokenRepository: TokenRepository
) : ApiViewModel(tokenRepository) {
    private val _beneficiaries = MutableLiveData<List<Beneficiary>>().apply {
        value = emptyList()
    }
    val beneficiaries: LiveData<List<Beneficiary>> = _beneficiaries
    private val _isLoading = MutableLiveData<Boolean>().apply { value = true }
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            hydrateApiClient()
            getBeneficiaries()
        }
    }

    private fun getBeneficiaries() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = piggyApi.beneficiaries("Bearer ${tokenRepository.getToken()}")
                _beneficiaries.value = response.body()!!.data
            } catch (e: Exception) {
//                Toast.makeText(context, e.localizedMessage, Toast.LENGTH_LONG).show()
            }
            _isLoading.value = false
        }
    }
}