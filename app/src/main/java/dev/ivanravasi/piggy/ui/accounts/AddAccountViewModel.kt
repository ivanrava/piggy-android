package dev.ivanravasi.piggy.ui.accounts

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.skydoves.colorpickerview.ColorEnvelope
import dev.ivanravasi.piggy.api.piggy.bodies.entities.AccountType
import dev.ivanravasi.piggy.api.piggy.bodies.errors.AccountValidationError
import dev.ivanravasi.piggy.api.piggy.bodies.requests.AccountRequest
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.ui.common.ApiViewModel
import kotlinx.coroutines.launch

class AddAccountViewModel(
    private val tokenRepository: TokenRepository,
    private val startingColor: Int
) : ApiViewModel(tokenRepository) {
    private val _isLoading = MutableLiveData<Boolean>().apply { value = true }
    val isLoading: LiveData<Boolean> = _isLoading
    val icon = MutableLiveData<String?>().apply { value = null }
    val color = MutableLiveData<ColorEnvelope>().apply { value = ColorEnvelope(startingColor) }
    private val _errors = MutableLiveData<AccountValidationError.Errors>().apply { value = AccountValidationError.Errors() }
    val errors: LiveData<AccountValidationError.Errors> = _errors
    private val _accountTypes = MutableLiveData<List<AccountType>>().apply { value = emptyList() }
    val accountTypes: LiveData<List<AccountType>> = _accountTypes

    init {
        viewModelScope.launch {
            hydrateApiClient()
            accountTypes()
        }
    }

    private suspend fun accountTypes() {
        try {
            val res = piggyApi.accountTypes("Bearer ${tokenRepository.getToken()}")
            if (res.isSuccessful)
                _accountTypes.value = res.body()!!
        } catch (e: Exception) {
        }
    }

    fun submit(
        name: String,
        initialBalance: String,
        opening: String,
        closing: String?,
        description: String,
        accountType: String,
        onSuccess: () -> Unit
    ) {
        if (icon.value == null) {
            _errors.value!!.icon = mutableListOf("The icon is required")
            _errors.postValue(_errors.value)
            return
        }
        if (accountType.isEmpty()) {
            _errors.value!!.accountTypeId = mutableListOf("You need to specify an account type")
            _errors.postValue(_errors.value)
            return
        }
        val accountTypeId = accountTypes.value!!.find { it.type == accountType }!!.id
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val req = AccountRequest(
                    name,
                    icon.value!!,
                    "#${color.value!!.hexCode!!.substring(2)}",
                    opening,
                    closing,
                    initialBalance,
                    description,
                    accountTypeId
                )
                val response = piggyApi.accountAdd("Bearer ${tokenRepository.getToken()}", req)
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    val errorString = response.errorBody()!!.string()
                    _errors.value =
                        Gson().fromJson(errorString, AccountValidationError::class.java).errors
                }
            } catch (e: Exception) {
                Log.e("add_account", e.toString())
            }
            _isLoading.value = false
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
}