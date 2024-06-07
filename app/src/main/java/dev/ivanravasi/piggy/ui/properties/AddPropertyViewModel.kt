package dev.ivanravasi.piggy.ui.properties

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dev.ivanravasi.piggy.api.piggy.bodies.PropertyRequest
import dev.ivanravasi.piggy.api.piggy.bodies.PropertyValidationError
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.ui.ApiViewModel
import kotlinx.coroutines.launch

class AddPropertyViewModel(
    private val tokenRepository: TokenRepository
) : ApiViewModel(tokenRepository) {
    private val _isLoading = MutableLiveData<Boolean>().apply { value = true }
    val isLoading: LiveData<Boolean> = _isLoading
    val icon = MutableLiveData<String?>().apply { value = null }
    private val _errors = MutableLiveData<PropertyValidationError.Errors>().apply { value = PropertyValidationError.Errors() }
    val errors: LiveData<PropertyValidationError.Errors> = _errors

    init {
        viewModelScope.launch {
            hydrateApiClient()
        }
    }

    fun submit(name: String, initialValue: String, description: String, onSuccess: () -> Unit) {
        if (icon.value == null) {
            _errors.value!!.icon = mutableListOf("The icon is required")
            _errors.postValue(_errors.value)
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val req = PropertyRequest(
                    name,
                    icon.value!!,
                    initialValue,
                    description
                )
                val response = piggyApi.propertyAdd("Bearer ${tokenRepository.getToken()}", req)
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    val errorString = response.errorBody()!!.string()
                    _errors.value =
                        Gson().fromJson(errorString, PropertyValidationError::class.java).errors
                }
            } catch (e: Exception) {
//                Toast.makeText(context, e.localizedMessage, Toast.LENGTH_LONG).show()
            }
            _isLoading.value = false
        }
    }
}