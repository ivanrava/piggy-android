package dev.ivanravasi.piggy.ui.operations

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Beneficiary
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Category
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Transaction
import dev.ivanravasi.piggy.api.piggy.bodies.errors.TransactionValidationError
import dev.ivanravasi.piggy.api.piggy.bodies.meta.ObjectResponse
import dev.ivanravasi.piggy.api.piggy.bodies.requests.TransactionRequest
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.ui.common.StoreApiViewModel
import kotlinx.coroutines.launch
import retrofit2.Response

class AddTransactionViewModel(
    private val tokenRepository: TokenRepository
) : StoreApiViewModel<Transaction, TransactionRequest, TransactionValidationError.Errors>(
    tokenRepository,
    TransactionValidationError.Errors()
) {
    private val _beneficiaries = MutableLiveData<List<Beneficiary>>().apply { value = emptyList() }
    val beneficiaries: LiveData<List<Beneficiary>> = _beneficiaries
    private val _categories = MutableLiveData<List<Category>>().apply { value = emptyList() }
    val categories: LiveData<List<Category>> = _categories

    init {
        viewModelScope.launch {
            hydrateApiClient()
            getBeneficiaries()
            getCategories()
        }
    }

    private suspend fun getBeneficiaries() {
        try {
            val res = piggyApi.beneficiaries("Bearer ${tokenRepository.getToken()}")
            if (res.isSuccessful)
                _beneficiaries.value = res.body()!!.data
        } catch (e: Exception) {
            Log.e("transactions.beneficiaries", e.toString())
        }
    }

    private suspend fun getCategories() {
        try {
            val res = piggyApi.categoryLeaves("Bearer ${tokenRepository.getToken()}")
            if (res.isSuccessful)
                _categories.value = res.body()!!.data
        } catch (e: Exception) {
            Log.e("transactions.categories", e.toString())
        }
    }

    class Factory(
        private val tokenRepository: TokenRepository
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AddTransactionViewModel(tokenRepository) as T
        }
    }

    override fun validate(request: TransactionRequest): Boolean {
        return true
    }


    override fun loadErrors(errors: String): TransactionValidationError.Errors {
        return Gson().fromJson(errors, TransactionValidationError::class.java).errors
    }

    override suspend fun request(request: TransactionRequest): Response<ObjectResponse<Transaction>> {
        return piggyApi.transactionAdd("Bearer ${tokenRepository.getToken()}", request)
    }
}