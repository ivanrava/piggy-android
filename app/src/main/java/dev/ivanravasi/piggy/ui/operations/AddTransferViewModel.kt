package dev.ivanravasi.piggy.ui.operations

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
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
    private val tokenRepository: TokenRepository
) : StoreApiViewModel<Transfer, TransferRequest, TransferValidationError.Errors>(
    tokenRepository,
    TransferValidationError.Errors()
) {
    private val _accounts = MutableLiveData<List<Account>>().apply { value = emptyList() }
    val accounts: LiveData<List<Account>> = _accounts

    init {
        viewModelScope.launch {
            hydrateApiClient()
            getAccounts()
        }
    }

    private suspend fun getAccounts() {
        try {
            val res = piggyApi.accounts("Bearer ${tokenRepository.getToken()}")
            // FIXME: filter out current account
            if (res.isSuccessful)
                _accounts.value = res.body()!!.data
        } catch (e: Exception) {
            Log.e("transactions.accounts", e.toString())
        }
    }

    class Factory(
        private val tokenRepository: TokenRepository
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AddTransferViewModel(tokenRepository) as T
        }
    }

    override fun validate(request: TransferRequest): Boolean {
        return true
    }


    override fun loadErrors(errors: String): TransferValidationError.Errors {
        return Gson().fromJson(errors, TransferValidationError::class.java).errors
    }

    override suspend fun request(request: TransferRequest): Response<ObjectResponse<Transfer>> {
        return piggyApi.transferAdd("Bearer ${tokenRepository.getToken()}", request)
    }
}