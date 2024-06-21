package dev.ivanravasi.piggy.ui.properties

import android.util.Log
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
            _isLoading.value = true
            getProperties()
            _isLoading.value = false
        }
    }

    private suspend fun getProperties() {
        try {
            val response = piggyApi.properties("Bearer ${tokenRepository.getToken()}")
            _objList.value = response.body()!!.data
        } catch (e: Exception) {
            Log.e("properties", e.toString())
        }
    }
}