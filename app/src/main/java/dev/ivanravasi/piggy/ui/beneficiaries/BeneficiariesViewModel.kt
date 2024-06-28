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
            _isLoading.value = true
            getBeneficiaries()
            _isLoading.value = false
        }
    }

    private suspend fun getBeneficiaries() {
        tryApiRequest("beneficiaries") {
            val response = piggyApi.beneficiaries("Bearer ${tokenRepository.getToken()}")
            _objList.value = response.body()!!.data
        }
    }
}