package dev.ivanravasi.piggy.ui.beneficiaries

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Beneficiary
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.ui.common.viewmodels.IndexApiViewModel
import kotlinx.coroutines.launch

class BeneficiariesViewModel(
    tokenRepository: TokenRepository,
    navController: NavController
) : IndexApiViewModel<Beneficiary>(tokenRepository, navController) {

    init {
        viewModelScope.launch {
            hydrateApiClient()
            refreshContents()
        }
    }

    private suspend fun getBeneficiaries() {
        tryApiRequest("beneficiaries") {
            val response = piggyApi.beneficiaries("Bearer ${tokenRepository.getToken()}")
            _objList.value = response.body()!!.data
        }
    }

    override suspend fun refreshContents() {
        _isLoading.value = true
        getBeneficiaries()
        _isLoading.value = false
    }
}