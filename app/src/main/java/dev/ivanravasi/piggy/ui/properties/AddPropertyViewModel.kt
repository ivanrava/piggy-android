package dev.ivanravasi.piggy.ui.properties

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Property
import dev.ivanravasi.piggy.api.piggy.bodies.requests.PropertyRequest
import dev.ivanravasi.piggy.api.piggy.bodies.errors.PropertyValidationError
import dev.ivanravasi.piggy.api.piggy.bodies.meta.ObjectResponse
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.ui.common.StoreApiViewModel
import kotlinx.coroutines.launch
import retrofit2.Response

class AddPropertyViewModel(
    private val tokenRepository: TokenRepository
) : StoreApiViewModel<Property, PropertyRequest, PropertyValidationError.Errors>(
    tokenRepository
) {
    override fun emptyErrorsProvider(): PropertyValidationError.Errors {
        return PropertyValidationError.Errors()
    }

    val icon = MutableLiveData<String?>().apply { value = null }

    init {
        viewModelScope.launch {
            hydrateApiClient()
        }
    }

    override fun validate(request: PropertyRequest): Boolean {
        if (icon.value == null) {
            _errors.value!!.icon = mutableListOf("The icon is required")
            _errors.postValue(_errors.value)
            return false
        }
        return true
    }

    override fun loadErrors(errors: String): PropertyValidationError.Errors {
        return Gson().fromJson(errors, PropertyValidationError::class.java).errors
    }

    override suspend fun updateRequest(request: PropertyRequest, resourceId: Long): Response<ObjectResponse<Property>> {
        return piggyApi.propertyUpdate("Bearer ${tokenRepository.getToken()}", request, resourceId)
    }

    override suspend fun storeRequest(request: PropertyRequest): Response<ObjectResponse<Property>> {
        return piggyApi.propertyAdd("Bearer ${tokenRepository.getToken()}", request)
    }
}