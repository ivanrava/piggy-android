package dev.ivanravasi.piggy.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dev.ivanravasi.piggy.api.RetrofitClient
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Account
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Chart
import dev.ivanravasi.piggy.data.DataStoreRepository
import dev.ivanravasi.piggy.ui.common.viewmodels.ApiViewModel
import kotlinx.coroutines.launch

class HomeViewModel(
    val dataStoreRepository: DataStoreRepository,
    navController: NavController
) : ApiViewModel(dataStoreRepository, navController) {
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
            val response = piggyApi.accounts("Bearer ${dataStoreRepository.getToken()}")
            _accounts.value = response.body()!!.data.sortedBy { it.lastUpdate }
        }
    }

    private suspend fun getFavoriteCharts() {
        tryApiRequest("home_favorite_charts") {
            val response = piggyApi.chartsFavorites("Bearer ${dataStoreRepository.getToken()}")
            _favoriteCharts.value = response.body()!!.data.filter { it.kind != "pie" }
        }
    }

    fun revokeToken(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val domain = dataStoreRepository.getDomain()!!
            val token = dataStoreRepository.getToken()!!
            val piggyApi = RetrofitClient.getPiggyInstance(domain)
            tryApiRequest("logout") {
                val response = piggyApi.revoke("Bearer $token")
                if (response.isSuccessful) {
                    dataStoreRepository.deleteToken()
                    onSuccess()
                } else {
                    _error.value = "Error ${response.code()}. Please contact the app developer."
                    Log.e("logout", response.code().toString())
                }
            }
        }
    }
}