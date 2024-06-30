package dev.ivanravasi.piggy.ui.home

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.RetrofitClient
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Account
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Chart
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.ui.common.ApiViewModel
import kotlinx.coroutines.launch

class HomeViewModel(
    val tokenRepository: TokenRepository,
    navController: NavController
) : ApiViewModel(tokenRepository, navController) {
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
        tryApiRequest("home_accounts") {
            val response = piggyApi.accounts("Bearer ${tokenRepository.getToken()}")
            _accounts.value = response.body()!!.data.sortedBy { it.lastUpdate }
        }
    }

    private suspend fun getFavoriteCharts() {
        tryApiRequest("home_favorite_charts") {
            val response = piggyApi.chartsFavorites("Bearer ${tokenRepository.getToken()}")
            _favoriteCharts.value = response.body()!!.data.filter { it.kind != "pie" }
        }
    }

    fun revokeToken(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val domain = tokenRepository.getDomain()!!
            val token = tokenRepository.getToken()!!
            val piggyApi = RetrofitClient.getPiggyInstance(domain)
            tryApiRequest("logout") {
                val response = piggyApi.revoke("Bearer $token")
                if (response.isSuccessful) {
                    tokenRepository.deleteToken()
                    onSuccess()
                } else {
                    _error.value = "Error ${response.code()}. Please contact the app developer."
                    Log.e("logout", response.code().toString())
                }
            }
        }
    }
}