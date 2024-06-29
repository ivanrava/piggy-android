package dev.ivanravasi.piggy.ui.accounts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.skydoves.colorpickerview.ColorEnvelope
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Account
import dev.ivanravasi.piggy.api.piggy.bodies.entities.AccountType
import dev.ivanravasi.piggy.api.piggy.bodies.errors.AccountValidationError
import dev.ivanravasi.piggy.api.piggy.bodies.meta.ObjectResponse
import dev.ivanravasi.piggy.api.piggy.bodies.requests.AccountRequest
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.ui.common.StoreApiViewModel
import kotlinx.coroutines.launch
import retrofit2.Response

class AddAccountViewModel(
    private val tokenRepository: TokenRepository,
    private val startingColor: Int
) : StoreApiViewModel<Account, AccountRequest, AccountValidationError.Errors>(
    tokenRepository
) {
    override fun emptyErrorsProvider(): AccountValidationError.Errors {
        return AccountValidationError.Errors()
    }


    val icon = MutableLiveData<String?>().apply { value = null }
    val color = MutableLiveData<ColorEnvelope>().apply { value = ColorEnvelope(startingColor) }
    private val _accountTypes = MutableLiveData<List<AccountType>>().apply { value = emptyList() }
    val accountTypes: LiveData<List<AccountType>> = _accountTypes

    init {
        viewModelScope.launch {
            hydrateApiClient()
            _isLoading.value = true
            getAccountTypes()
            _isLoading.value = false
        }
    }

    private suspend fun getAccountTypes() {
        tryApiRequest("account_types") {
            val res = piggyApi.accountTypes("Bearer ${tokenRepository.getToken()}")
            if (res.isSuccessful)
                _accountTypes.value = res.body()!!
        }
    }

    class Factory(
        private val tokenRepository: TokenRepository,
        private val startingColor: Int
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AddAccountViewModel(tokenRepository, startingColor) as T
        }
    }

    override fun validate(request: AccountRequest): Boolean {
        if (icon.value == null) {
            _errors.value!!.icon = mutableListOf("The icon is required")
            _errors.postValue(_errors.value)
            return false
        }
        return true
    }

    override fun loadErrors(errors: String): AccountValidationError.Errors {
        return Gson().fromJson(errors, AccountValidationError::class.java).errors
    }

    override suspend fun updateRequest(
        request: AccountRequest,
        resourceId: Long
    ): Response<ObjectResponse<Account>> {
        return piggyApi.accountUpdate("Bearer ${tokenRepository.getToken()}", request, resourceId)
    }

    override suspend fun storeRequest(request: AccountRequest): Response<ObjectResponse<Account>> {
        return piggyApi.accountAdd("Bearer ${tokenRepository.getToken()}", request)
    }
}