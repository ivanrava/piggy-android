package dev.ivanravasi.piggy.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Account
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Chart
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.ui.common.ApiViewModel
import kotlinx.coroutines.launch

class HomeViewModel(
    val tokenRepository: TokenRepository
) : ApiViewModel(tokenRepository) {
    private val _isLoading = MutableLiveData<Boolean>().apply { value = true }
    val isLoading: LiveData<Boolean> = _isLoading
    private val _accounts = MutableLiveData<List<Account>>().apply { value = emptyList() }
    val accounts: LiveData<List<Account>> = _accounts
    private val _favoriteCharts = MutableLiveData<List<Chart>>().apply { value = emptyList() }
    val favoriteCharts: LiveData<List<Chart>> = _favoriteCharts

    init {
        viewModelScope.launch {
            hydrateApiClient()
            _isLoading.value = true
            getAccounts()
            getFavoriteCharts()
            _isLoading.value = false
        }
    }

    private suspend fun getAccounts() {
        try {
            val response = piggyApi.accounts("Bearer ${tokenRepository.getToken()}")
            _accounts.value = response.body()!!.data.sortedBy { it.lastUpdate }
        } catch (e: Exception) {
            Log.e("accounts", e.toString())
        }
    }

    private suspend fun getFavoriteCharts() {
        try {
            val response = piggyApi.chartsFavorites("Bearer ${tokenRepository.getToken()}")
            _favoriteCharts.value = response.body()!!.data.filter { it.kind != "pie" }
        } catch (e: Exception) {
            Log.e("accounts", e.toString())
        }
    }
}