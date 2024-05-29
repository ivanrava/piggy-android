package dev.ivanravasi.piggy.ui.properties

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Property
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.ui.ApiViewModel
import kotlinx.coroutines.launch

class PropertiesViewModel(
    private val tokenRepository: TokenRepository
) : ApiViewModel(tokenRepository) {
    private val _properties = MutableLiveData<List<Property>>().apply {
        value = emptyList()
    }
    val properties: LiveData<List<Property>> = _properties
    private val _isLoading = MutableLiveData<Boolean>().apply { value = true }
    val isLoading: LiveData<Boolean> = _isLoading

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
                _properties.value = response.body()!!.data
            } catch (e: Exception) {
//                Toast.makeText(context, e.localizedMessage, Toast.LENGTH_LONG).show()
            }
            _isLoading.value = false
        }
    }
}