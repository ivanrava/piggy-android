package dev.ivanravasi.piggy.ui.properties

import androidx.lifecycle.viewModelScope
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Property
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.ui.common.IndexApiViewModel
import kotlinx.coroutines.launch

class PropertiesViewModel(
    tokenRepository: TokenRepository
) : IndexApiViewModel<Property>(tokenRepository) {
    init {
        viewModelScope.launch {
            hydrateApiClient()
            getProperties()
        }
    }

    private fun getProperties() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = piggyApi.properties("Bearer ${tokenRepository.getToken()}")
                _objList.value = response.body()!!.data
            } catch (e: Exception) {
//                Toast.makeText(context, e.localizedMessage, Toast.LENGTH_LONG).show()
            }
            _isLoading.value = false
        }
    }
}