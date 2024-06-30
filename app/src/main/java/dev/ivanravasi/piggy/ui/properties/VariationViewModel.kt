package dev.ivanravasi.piggy.ui.properties

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.gson.Gson
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Property
import dev.ivanravasi.piggy.api.piggy.bodies.errors.PropertyVariationValidationError
import dev.ivanravasi.piggy.api.piggy.bodies.meta.ObjectResponse
import dev.ivanravasi.piggy.api.piggy.bodies.requests.PropertyVariationRequest
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.ui.common.StoreApiViewModel
import kotlinx.coroutines.launch
import retrofit2.Response

class VariationViewModel(
    private val tokenRepository: TokenRepository,
    navController: NavController
): StoreApiViewModel<Property.PropertyVariation, PropertyVariationRequest, PropertyVariationValidationError.Errors>(
    tokenRepository,
    navController
) {
    init {
        viewModelScope.launch {
            hydrateApiClient()
        }
    }

    override fun emptyErrorsProvider(): PropertyVariationValidationError.Errors {
        return PropertyVariationValidationError.Errors()
    }

    override fun loadErrors(errors: String): PropertyVariationValidationError.Errors {
        return Gson().fromJson(errors, PropertyVariationValidationError::class.java).errors
    }

    override suspend fun updateRequest(
        request: PropertyVariationRequest,
        resourceId: Long
    ): Response<ObjectResponse<Property.PropertyVariation>> {
        // TODO: implement
        return piggyApi.variationAdd("Bearer ${tokenRepository.getToken()}", request.propertyId, request)
    }

    override suspend fun storeRequest(request: PropertyVariationRequest): Response<ObjectResponse<Property.PropertyVariation>> {
        return piggyApi.variationAdd("Bearer ${tokenRepository.getToken()}", request.propertyId, request)
    }

    override fun validate(request: PropertyVariationRequest): Boolean {
        return true
    }
}