package dev.ivanravasi.piggy.ui.properties

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Property
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.ui.common.IndexApiViewModel
import kotlinx.coroutines.launch

class PropertiesViewModel(
    tokenRepository: TokenRepository,
    navController: NavController
) : IndexApiViewModel<Property>(tokenRepository, navController) {
    init {
        viewModelScope.launch {
            hydrateApiClient()
            refreshContents()
        }
    }

    private suspend fun getProperties() {
        tryApiRequest("properties") {
            val response = piggyApi.properties("Bearer ${tokenRepository.getToken()}")
            _objList.value = response.body()!!.data
        }
    }

    override suspend fun refreshContents() {
        _isLoading.value = true
        getProperties()
        _isLoading.value = false
    }

    fun refreshAll() {
        viewModelScope.launch {
            _isLoading.value = true
            getProperties()
            _isLoading.value = false
        }
    }
}