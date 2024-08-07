package dev.ivanravasi.piggy.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.gson.Gson
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Category
import dev.ivanravasi.piggy.api.piggy.bodies.errors.CategoryValidationError
import dev.ivanravasi.piggy.api.piggy.bodies.meta.ObjectResponse
import dev.ivanravasi.piggy.api.piggy.bodies.requests.CategoryRequest
import dev.ivanravasi.piggy.data.DataStoreRepository
import dev.ivanravasi.piggy.ui.common.viewmodels.StoreApiViewModel
import kotlinx.coroutines.launch
import retrofit2.Response

class AddCategoryViewModel(
    private val dataStoreRepository: DataStoreRepository,
    navController: NavController
) : StoreApiViewModel<Category, CategoryRequest, CategoryValidationError.Errors>(
    dataStoreRepository, navController
) {
    override fun emptyErrorsProvider(): CategoryValidationError.Errors {
        return CategoryValidationError.Errors()
    }

    val icon = MutableLiveData<String?>().apply { value = null }
    private val _rootCategories = MutableLiveData<List<Category>>().apply { value = emptyList() }
    val rootCategories: LiveData<List<Category>> = _rootCategories

    val parent = MutableLiveData<Category?>().apply { value = null }

    init {
        viewModelScope.launch {
            hydrateApiClient()
            rootCategories()
        }
    }

    private suspend fun rootCategories() {
        tryApiRequest("categories_root") {
            val res = piggyApi.categoryTrees("Bearer ${dataStoreRepository.getToken()}")
            if (res.isSuccessful)
                _rootCategories.value = res.body()!!.data
        }
    }

    class Factory(
        private val dataStoreRepository: DataStoreRepository,
        private val navController: NavController
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AddCategoryViewModel(dataStoreRepository, navController) as T
        }
    }

    fun validateBudgetFixed(overallBudget: String?): Boolean {
        if (overallBudget == null) {
            _errors.value!!.budgetOverall = mutableListOf("A budget is required")
            _errors.postValue(_errors.value)
            return false
        }
        return true
    }

    override fun validate(request: CategoryRequest): Boolean {
        if (icon.value == null) {
            _errors.value!!.icon = mutableListOf("The icon is required")
            _errors.postValue(_errors.value)
            return false
        }
        if ((request.budgetOverall == null || request.budgetOverall!!.isEmpty()) && request.budget == null) {
            _errors.value!!.budgetOverall = mutableListOf("An overall budget is required")
            _errors.postValue(_errors.value)
            return false
        }
        if ((request.budgetOverall == null || request.budgetOverall!!.isEmpty()) && request.budget != null) {
            val requiredError = mutableListOf("The field is required")
            if (request.budget!!.jan.isEmpty()) _errors.value!!.budget.jan = requiredError
            if (request.budget!!.feb.isEmpty()) _errors.value!!.budget.feb = requiredError
            if (request.budget!!.mar.isEmpty()) _errors.value!!.budget.mar = requiredError
            if (request.budget!!.apr.isEmpty()) _errors.value!!.budget.apr = requiredError
            if (request.budget!!.may.isEmpty()) _errors.value!!.budget.may = requiredError
            if (request.budget!!.jun.isEmpty()) _errors.value!!.budget.jun = requiredError
            if (request.budget!!.jul.isEmpty()) _errors.value!!.budget.jul = requiredError
            if (request.budget!!.aug.isEmpty()) _errors.value!!.budget.aug = requiredError
            if (request.budget!!.sep.isEmpty()) _errors.value!!.budget.sep = requiredError
            if (request.budget!!.oct.isEmpty()) _errors.value!!.budget.oct = requiredError
            if (request.budget!!.nov.isEmpty()) _errors.value!!.budget.nov = requiredError
            if (request.budget!!.dec.isEmpty()) _errors.value!!.budget.dec = requiredError
            _errors.postValue(_errors.value)
            return false
        }
        return true
    }

    override fun loadErrors(errors: String): CategoryValidationError.Errors {
        return Gson().fromJson(errors, CategoryValidationError::class.java).errors
    }

    override suspend fun updateRequest(
        request: CategoryRequest,
        resourceId: Long
    ): Response<ObjectResponse<Category>> {
        return piggyApi.categoryUpdate("Bearer ${dataStoreRepository.getToken()}", request, resourceId)
    }

    override suspend fun storeRequest(request: CategoryRequest): Response<ObjectResponse<Category>> {
        return piggyApi.categoryAdd("Bearer ${dataStoreRepository.getToken()}", request)
    }
}