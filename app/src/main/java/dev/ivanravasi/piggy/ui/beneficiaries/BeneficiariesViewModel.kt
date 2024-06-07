package dev.ivanravasi.piggy.ui.beneficiaries

import androidx.lifecycle.viewModelScope
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Beneficiary
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.ui.common.IndexApiViewModel
import kotlinx.coroutines.launch

class BeneficiariesViewModel(
    tokenRepository: TokenRepository
) : IndexApiViewModel<Beneficiary>(tokenRepository) {

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
                _objList.value = response.body()!!.data
            } catch (e: Exception) {
//                Toast.makeText(context, e.localizedMessage, Toast.LENGTH_LONG).show()
            }
            _isLoading.value = false
        }
    }
}