package dev.ivanravasi.piggy.ui.operations

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Operation
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Transaction
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Transfer
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.ui.ApiViewModel
import kotlinx.coroutines.launch

class OperationsViewModel(
    private val tokenRepository: TokenRepository,
    private val accountId: Long
) : ApiViewModel(tokenRepository) {
    private val _transactions = MutableLiveData<List<Transaction>>().apply {
        value = emptyList()
    }
    private val _transfers = MutableLiveData<List<Transfer>>().apply {
        value = emptyList()
    }
    private val _operations = MutableLiveData<List<Operation>>().apply {
        value = emptyList()
    }
    private val _accountName = MutableLiveData<String>().apply {
        value = ""
    }
    val transactions: LiveData<List<Transaction>> = _transactions
    val transfers: LiveData<List<Transfer>> = _transfers
    val operations: LiveData<List<Operation>> = _operations
    val accountName: LiveData<String> = _accountName
    private val _isLoading = MutableLiveData<Boolean>().apply { value = true }
    val isLoading: LiveData<Boolean> = _isLoading

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
                _transactions.value = response.body()!!.data.transactions.sortedByDescending { it.date }
                _transfers.value = response.body()!!.data.inTransfers + response.body()!!.data.outTransfers
                _operations.value =
                    (response.body()!!.data.transactions + response.body()!!.data.inTransfers + response.body()!!.data.outTransfers)
                        .sortedByDescending { it.rawDate() }
                _accountName.value = response.body()!!.data.name
            } catch (e: Exception) {
                e.localizedMessage?.let { Log.i("message", it) }
            }
            _isLoading.value = false
        }
    }
}