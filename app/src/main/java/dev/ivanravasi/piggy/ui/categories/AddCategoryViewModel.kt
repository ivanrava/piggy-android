package dev.ivanravasi.piggy.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Category
import dev.ivanravasi.piggy.api.piggy.bodies.errors.CategoryValidationError
import dev.ivanravasi.piggy.api.piggy.bodies.meta.ObjectResponse
import dev.ivanravasi.piggy.api.piggy.bodies.requests.CategoryRequest
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.ui.common.StoreApiViewModel
import kotlinx.coroutines.launch
import retrofit2.Response

class AddCategoryViewModel(
    private val tokenRepository: TokenRepository
) : StoreApiViewModel<Category, CategoryRequest, CategoryValidationError.Errors>(
    tokenRepository,
    CategoryValidationError.Errors()
) {
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
        try {
            val res = piggyApi.categoryTrees("Bearer ${tokenRepository.getToken()}")
            if (res.isSuccessful)
                _rootCategories.value = res.body()!!.data
        } catch (e: Exception) {
        }
    }

    class Factory(
        private val tokenRepository: TokenRepository
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AddCategoryViewModel(tokenRepository) as T
        }
    }

    override fun validate(request: CategoryRequest): Boolean {
        if (icon.value == null) {
            _errors.value!!.icon = mutableListOf("The icon is required")
            _errors.postValue(_errors.value)
            return false
        }
        return true
    }

    override fun loadErrors(errors: String): CategoryValidationError.Errors {
        return Gson().fromJson(errors, CategoryValidationError::class.java).errors
    }

    override suspend fun request(request: CategoryRequest): Response<ObjectResponse<Category>> {
        return piggyApi.categoryAdd("Bearer ${tokenRepository.getToken()}", request)
    }
}