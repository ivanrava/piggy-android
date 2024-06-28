package dev.ivanravasi.piggy.ui.accounts

import androidx.lifecycle.viewModelScope
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Account
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.ui.common.IndexApiViewModel
import kotlinx.coroutines.launch

class AccountsViewModel(
    tokenRepository: TokenRepository
) : IndexApiViewModel<Account>(tokenRepository) {
    init {
        viewModelScope.launch {
            hydrateApiClient()
            _isLoading.value = true
            getAccounts()
            _isLoading.value = false
        }
    }

    private suspend fun getAccounts() {
        tryApiRequest("accounts") {
            val response = piggyApi.accounts("Bearer ${tokenRepository.getToken()}")
            _objList.value = response.body()!!.data.sortedBy { it.name }
        }
    }
}