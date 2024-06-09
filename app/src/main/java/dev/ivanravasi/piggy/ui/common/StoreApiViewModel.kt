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
    tokenRepository: TokenRepository,
    private val emptyErrors: Errors,
): ApiViewModel(tokenRepository) {
    protected val _isLoading = MutableLiveData<Boolean>().apply { value = true }
    val isLoading: LiveData<Boolean> = _isLoading
    protected val _errors = MutableLiveData<Errors>().apply { value = emptyErrors }
    val errors: LiveData<Errors> = _errors

    abstract fun validate(request: Request): Boolean

    abstract fun loadErrors(errors: String): Errors

    abstract suspend fun request(request: Request): Response<ObjectResponse<Entity>>

    fun submit(request: Request, onSuccess: () -> Unit) {
        if (!validate(request))
            return
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = request(request)
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    val errorString = response.errorBody()!!.string()
                    _errors.value = loadErrors(errorString)
                }
            } catch (e: Exception) {
                Log.e("store", e.toString())
            }
            _isLoading.value = false
        }
    }
}