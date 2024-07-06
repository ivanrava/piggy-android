package dev.ivanravasi.piggy.ui.charts

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
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Chart
import dev.ivanravasi.piggy.api.piggy.bodies.errors.ChartValidationError
import dev.ivanravasi.piggy.api.piggy.bodies.meta.ObjectResponse
import dev.ivanravasi.piggy.api.piggy.bodies.requests.ChartRequest
import dev.ivanravasi.piggy.data.DataStoreRepository
import dev.ivanravasi.piggy.ui.common.viewmodels.StoreApiViewModel
import kotlinx.coroutines.launch
import retrofit2.Response

class AddChartViewModel(
    private val dataStoreRepository: DataStoreRepository,
    navController: NavController
) : StoreApiViewModel<Chart, ChartRequest, ChartValidationError.Errors>(
    dataStoreRepository, navController
) {
    override fun emptyErrorsProvider(): ChartValidationError.Errors {
        return ChartValidationError.Errors()
    }

    private val _beneficiaries = MutableLiveData<List<Beneficiary>>().apply { value = emptyList() }
    val beneficiaries: LiveData<List<Beneficiary>> = _beneficiaries
    private val _categories = MutableLiveData<List<Category>>().apply { value = emptyList() }
    val categories: LiveData<List<Category>> = _categories
    private val _accounts = MutableLiveData<List<Account>>().apply { value = emptyList() }
    val accounts: LiveData<List<Account>> = _accounts

    val beneficiary = MutableLiveData<Beneficiary?>().apply { value = null }
    val category = MutableLiveData<Category?>().apply { value = null }
    val account = MutableLiveData<Account?>().apply { value = null }


    init {
        viewModelScope.launch {
            hydrateApiClient()
            _isLoading.value = true
            getAccounts()
            getBeneficiaries()
            getCategories()
            _isLoading.value = false
        }
    }

    private suspend fun getBeneficiaries() {
        tryApiRequest("charts.beneficiaries") {
            val res = piggyApi.beneficiaries("Bearer ${dataStoreRepository.getToken()}")
            if (res.isSuccessful) {
                _beneficiaries.value = res.body()!!.data
                beneficiary.value = _beneficiaries.value!!.firstOrNull()
            }
        }
    }

    private suspend fun getCategories() {
        tryApiRequest("charts.categories") {
            val res = piggyApi.categoryLeaves("Bearer ${dataStoreRepository.getToken()}")
            if (res.isSuccessful) {
                _categories.value = res.body()!!.data
                category.value = _categories.value!!.firstOrNull()
            }
        }
    }

    private suspend fun getAccounts() {
        tryApiRequest("charts.accounts") {
            val response = piggyApi.accounts("Bearer ${dataStoreRepository.getToken()}")
            if (response.isSuccessful) {
                _accounts.value = response.body()!!.data
                account.value = _accounts.value!!.firstOrNull()
            }
        }
    }

    class Factory(
        private val dataStoreRepository: DataStoreRepository,
        private val navController: NavController
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AddChartViewModel(dataStoreRepository, navController) as T
        }
    }

    override fun validate(request: ChartRequest): Boolean {
        return true
    }

    override fun loadErrors(errors: String): ChartValidationError.Errors {
        return Gson().fromJson(errors, ChartValidationError::class.java).errors
    }

    override suspend fun updateRequest(
        request: ChartRequest,
        resourceId: Long
    ): Response<ObjectResponse<Chart>> {
        return piggyApi.chartAdd("Bearer ${dataStoreRepository.getToken()}", request)
    }

    override suspend fun storeRequest(request: ChartRequest): Response<ObjectResponse<Chart>> {
        return piggyApi.chartAdd("Bearer ${dataStoreRepository.getToken()}", request)
    }

    fun favorite(chartId: Long) {
        viewModelScope.launch {
            tryApiRequest("favorite") {
                piggyApi.chartUpdate("Bearer ${dataStoreRepository.getToken()}", chartId)
            }
        }
    }
}