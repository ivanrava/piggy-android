package dev.ivanravasi.piggy.ui.operations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.gson.Gson
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Account
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Transfer
import dev.ivanravasi.piggy.api.piggy.bodies.errors.TransferValidationError
import dev.ivanravasi.piggy.api.piggy.bodies.meta.ObjectResponse
import dev.ivanravasi.piggy.api.piggy.bodies.requests.TransferRequest
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.ui.common.StoreApiViewModel
import kotlinx.coroutines.launch
import retrofit2.Response

class AddTransferViewModel(
    private val tokenRepository: TokenRepository,
    private val accountId: Long,
    navController: NavController
) : StoreApiViewModel<Transfer, TransferRequest, TransferValidationError.Errors>(
    tokenRepository,
    navController
) {
    override fun emptyErrorsProvider(): TransferValidationError.Errors {
        return TransferValidationError.Errors()
    }

    private val _accounts = MutableLiveData<List<Account>>().apply { value = emptyList() }
    val accounts: LiveData<List<Account>> = _accounts

    private val _fromAccount = MutableLiveData<Account>().apply { value = null }
    val fromAccount: LiveData<Account> = _fromAccount

    val toAccount = MutableLiveData<Account?>().apply { value = null }

    init {
        viewModelScope.launch {
            hydrateApiClient()
            _isLoading.value = true
            getAccounts()
            getAccount()
            _isLoading.value = false
        }
    }

    private suspend fun getAccounts() {
        tryApiRequest("transactions.accounts") {
            val res = piggyApi.accounts("Bearer ${tokenRepository.getToken()}")
            if (res.isSuccessful) {
                _accounts.value = res.body()!!.data.filter { it.id != accountId }.sortedBy { it.name }
                toAccount.value = accounts.value!!.first()
            }
        }
    }

    // TODO: try to avoid this call by grabbing the object from the previous fragment
    private suspend fun getAccount() {
        tryApiRequest("transactions.account") {
            val response = piggyApi.account("Bearer ${tokenRepository.getToken()}", accountId)
            if (response.isSuccessful)
                _fromAccount.value = response.body()!!.data
        }
    }

    class Factory(
        private val tokenRepository: TokenRepository,
        private val accountId: Long,
        private val navController: NavController
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AddTransferViewModel(tokenRepository, accountId, navController) as T
        }
    }

    override fun validate(request: TransferRequest): Boolean {
        return true
    }


    override fun loadErrors(errors: String): TransferValidationError.Errors {
        return Gson().fromJson(errors, TransferValidationError::class.java).errors
    }

    override suspend fun updateRequest(
        request: TransferRequest,
        resourceId: Long
    ): Response<ObjectResponse<Transfer>> {
        return piggyApi.transferUpdate("Bearer ${tokenRepository.getToken()}", request, resourceId)
    }

    override suspend fun storeRequest(request: TransferRequest): Response<ObjectResponse<Transfer>> {
        return piggyApi.transferAdd("Bearer ${tokenRepository.getToken()}", request)
    }
}