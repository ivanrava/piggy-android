package dev.ivanravasi.piggy.ui.accounts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Account
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.ui.ApiViewModel
import kotlinx.coroutines.launch

class AccountsViewModel(
    private val tokenRepository: TokenRepository
) : ApiViewModel(tokenRepository) {
    private val _accounts = MutableLiveData<List<Account>>().apply {
        value = emptyList()
    }
    val accounts: LiveData<List<Account>> = _accounts
    private val _isLoading = MutableLiveData<Boolean>().apply { value = true }
    val isLoading: LiveData<Boolean> = _isLoading

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
                _accounts.value = response.body()!!.data.sortedBy { it.name }
            } catch (e: Exception) {
//                Toast.makeText(context, e.localizedMessage, Toast.LENGTH_LONG).show()
            }
            _isLoading.value = false
        }
    }
}