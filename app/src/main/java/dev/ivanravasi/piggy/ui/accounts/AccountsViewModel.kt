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
            getProperties()
        }
    }

    private fun getProperties() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = piggyApi.accounts("Bearer ${tokenRepository.getToken()}")
                _objList.value = response.body()!!.data.sortedBy { it.name }
            } catch (e: Exception) {
//                Toast.makeText(context, e.localizedMessage, Toast.LENGTH_LONG).show()
            }
            _isLoading.value = false
        }
    }
}