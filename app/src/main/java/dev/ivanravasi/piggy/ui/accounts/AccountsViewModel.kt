package dev.ivanravasi.piggy.ui.accounts

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Account
import dev.ivanravasi.piggy.data.DataStoreRepository
import dev.ivanravasi.piggy.ui.common.viewmodels.IndexApiViewModel
import kotlinx.coroutines.launch

class AccountsViewModel(
    dataStoreRepository: DataStoreRepository,
    navController: NavController
) : IndexApiViewModel<Account>(dataStoreRepository, navController) {
    init {
        viewModelScope.launch {
            hydrateApiClient()
            refreshContents()
        }
    }

    private suspend fun getAccounts() {
        tryApiRequest("accounts") {
            val response = piggyApi.accounts("Bearer ${dataStoreRepository.getToken()}")
            _objList.value = response.body()!!.data.sortedBy { it.name }
        }
    }

    override suspend fun refreshContents() {
        _isLoading.value = true
        getAccounts()
        _isLoading.value = false
    }
}