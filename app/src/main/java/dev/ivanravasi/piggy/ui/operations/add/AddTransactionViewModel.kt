package dev.ivanravasi.piggy.ui.operations.add

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.gson.Gson
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Account
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Beneficiary
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Category
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Transaction
import dev.ivanravasi.piggy.api.piggy.bodies.errors.TransactionValidationError
import dev.ivanravasi.piggy.api.piggy.bodies.meta.ObjectResponse
import dev.ivanravasi.piggy.api.piggy.bodies.requests.TransactionRequest
import dev.ivanravasi.piggy.data.DataStoreRepository
import dev.ivanravasi.piggy.ui.common.viewmodels.StoreApiViewModel
import kotlinx.coroutines.launch
import retrofit2.Response

class AddTransactionViewModel(
    private val dataStoreRepository: DataStoreRepository,
    private val accountCached: Account,
    navController: NavController
) : StoreApiViewModel<Transaction, TransactionRequest, TransactionValidationError.Errors>(
    dataStoreRepository, navController
) {
    override fun emptyErrorsProvider(): TransactionValidationError.Errors {
        return TransactionValidationError.Errors()
    }

    private val _beneficiaries = MutableLiveData<List<Beneficiary>>().apply { value = emptyList() }
    val beneficiaries: LiveData<List<Beneficiary>> = _beneficiaries
    private val _categories = MutableLiveData<List<Category>>().apply { value = emptyList() }
    val categories: LiveData<List<Category>> = _categories
    private val _account = MutableLiveData<Account?>().apply { value = null }
    val account: LiveData<Account?> = _account

    val beneficiary = MutableLiveData<Beneficiary?>().apply { value = null }
    val category = MutableLiveData<Category?>().apply { value = null }


    init {
        viewModelScope.launch {
            hydrateApiClient()
            _isLoading.value = true
            getAccount()
            getBeneficiaries()
            getCategories()
            _isLoading.value = false
        }
    }

    private suspend fun getBeneficiaries() {
        tryApiRequest("transactions.beneficiaries") {
            val res = piggyApi.beneficiaries("Bearer ${dataStoreRepository.getToken()}")
            if (res.isSuccessful) {
                _beneficiaries.value = res.body()!!.data
            }
        }
    }

    private suspend fun getCategories() {
        tryApiRequest("transactions.categories") {
            val res = piggyApi.categoryLeaves("Bearer ${dataStoreRepository.getToken()}")
            if (res.isSuccessful) {
                _categories.value = res.body()!!.data
            }
        }
    }

    private suspend fun getAccount() {
        tryApiRequest("transactions.account") {
            val response = piggyApi.account("Bearer ${dataStoreRepository.getToken()}", accountCached.id)
            if (response.isSuccessful) {
                _account.value = response.body()!!.data
                val mostFrequentTransaction = _account.value!!.transactions
                    .groupBy { it.beneficiary.name + it.category.name }
                    .values.maxBy { it.count() }
                    .firstOrNull()
                beneficiary.value = beneficiary.value ?: mostFrequentTransaction?.beneficiary
                category.value = category.value ?: mostFrequentTransaction?.category
            }
        }
    }

    class Factory(
        private val dataStoreRepository: DataStoreRepository,
        private val account: Account,
        private val navController: NavController
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AddTransactionViewModel(dataStoreRepository, account, navController) as T
        }
    }

    override fun validate(request: TransactionRequest): Boolean {
        if (beneficiary.value == null) {
            _errors.value!!.beneficiary.name = mutableListOf("The beneficiary is required")
            _errors.postValue(_errors.value)
            return false
        }
        if (category.value == null) {
            _errors.value!!.category.name = mutableListOf("The category is required")
            _errors.postValue(_errors.value)
            return false
        }
        return true
    }

    override fun loadErrors(errors: String): TransactionValidationError.Errors {
        return Gson().fromJson(errors, TransactionValidationError::class.java).errors
    }

    override suspend fun updateRequest(
        request: TransactionRequest,
        resourceId: Long
    ): Response<ObjectResponse<Transaction>> {
        return piggyApi.transactionUpdate("Bearer ${dataStoreRepository.getToken()}", request, resourceId)
    }

    override suspend fun storeRequest(request: TransactionRequest): Response<ObjectResponse<Transaction>> {
        return piggyApi.transactionAdd("Bearer ${dataStoreRepository.getToken()}", request)
    }
}