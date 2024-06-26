package dev.ivanravasi.piggy.ui.operations

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Account
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Operation
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.ui.common.IndexApiViewModel
import kotlinx.coroutines.launch

class OperationsViewModel(
    tokenRepository: TokenRepository,
    private val accountId: Long
) : IndexApiViewModel<Operation>(tokenRepository) {
    private val _account = MutableLiveData<Account>().apply {
        value = null
    }
    val account: LiveData<Account> = _account

    init {
        viewModelScope.launch {
            hydrateApiClient()
            getOperations()
        }
    }

    private fun getOperations() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = piggyApi.account("Bearer ${tokenRepository.getToken()}", accountId)
                _objList.value =
                    (response.body()!!.data.transactions + response.body()!!.data.inTransfers + response.body()!!.data.outTransfers)
                        .sortedByDescending { it.rawDate() }
                _account.value = response.body()!!.data
            } catch (e: Exception) {
                e.localizedMessage?.let { Log.i("message", it) }
            }
            _isLoading.value = false
        }
    }
}