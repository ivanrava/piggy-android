package dev.ivanravasi.piggy.ui.common

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.ivanravasi.piggy.api.piggy.bodies.meta.ObjectResponse
import dev.ivanravasi.piggy.data.TokenRepository
import kotlinx.coroutines.launch
import retrofit2.Response

abstract class StoreApiViewModel<Entity, Request, Errors>(
    tokenRepository: TokenRepository
): ApiViewModel(tokenRepository) {
    protected val _isLoading = MutableLiveData<Boolean>().apply { value = true }
    val isLoading: LiveData<Boolean> = _isLoading
    protected val _errors = MutableLiveData<Errors>().apply { value = emptyErrorsProvider() }
    val errors: LiveData<Errors> = _errors

    private fun validateWrapper(request: Request): Boolean {
        _errors.value = emptyErrorsProvider()
        return validate(request)
    }

    abstract fun emptyErrorsProvider(): Errors

    abstract fun validate(request: Request): Boolean

    abstract fun loadErrors(errors: String): Errors

    abstract suspend fun request(request: Request): Response<ObjectResponse<Entity>>

    fun submit(request: Request, onSuccess: () -> Unit) {
        if (!validateWrapper(request))
            return
        viewModelScope.launch {
            _isLoading.value = true
            tryApiRequest("store") {
                val response = request(request)
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    val errorString = response.errorBody()!!.string()
                    Log.e("store_errors", errorString)
                    _errors.value = loadErrors(errorString)
                }
            }
            _isLoading.value = false
        }
    }
}