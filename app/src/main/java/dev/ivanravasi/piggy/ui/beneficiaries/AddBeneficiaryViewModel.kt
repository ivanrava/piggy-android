package dev.ivanravasi.piggy.ui.beneficiaries

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.gson.Gson
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Beneficiary
import dev.ivanravasi.piggy.api.piggy.bodies.errors.BeneficiaryValidationError
import dev.ivanravasi.piggy.api.piggy.bodies.meta.ObjectResponse
import dev.ivanravasi.piggy.api.piggy.bodies.requests.BeneficiaryRequest
import dev.ivanravasi.piggy.data.DataStoreRepository
import dev.ivanravasi.piggy.ui.common.viewmodels.StoreApiViewModel
import kotlinx.coroutines.launch
import retrofit2.Response

class AddBeneficiaryViewModel(
    private val dataStoreRepository: DataStoreRepository,
    navController: NavController
) : StoreApiViewModel<Beneficiary, BeneficiaryRequest, BeneficiaryValidationError.Errors>(
    dataStoreRepository, navController
) {
    override fun emptyErrorsProvider(): BeneficiaryValidationError.Errors {
        return BeneficiaryValidationError.Errors()
    }

    init {
        viewModelScope.launch {
            hydrateApiClient()
        }
    }

    class Factory(
        private val dataStoreRepository: DataStoreRepository,
        private val navController: NavController
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AddBeneficiaryViewModel(dataStoreRepository, navController) as T
        }
    }

    override fun validate(request: BeneficiaryRequest): Boolean = true

    override fun loadErrors(errors: String): BeneficiaryValidationError.Errors {
        return Gson().fromJson(errors, BeneficiaryValidationError::class.java).errors
    }

    override suspend fun updateRequest(
        request: BeneficiaryRequest,
        resourceId: Long
    ): Response<ObjectResponse<Beneficiary>> {
        return piggyApi.beneficiaryUpdate("Bearer ${dataStoreRepository.getToken()}", request, resourceId)
    }

    override suspend fun storeRequest(request: BeneficiaryRequest): Response<ObjectResponse<Beneficiary>> {
        return piggyApi.beneficiaryAdd("Bearer ${dataStoreRepository.getToken()}", request)
    }
}