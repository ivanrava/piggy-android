package dev.ivanravasi.piggy.ui.auth.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.ivanravasi.piggy.api.RetrofitClient
import dev.ivanravasi.piggy.api.piggy.bodies.requests.RegisterRequest
import dev.ivanravasi.piggy.data.DataStoreRepository
import dev.ivanravasi.piggy.ui.auth.ModelUtils.deviceName
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {
    fun register(
        domain: String,
        name: String,
        email: String,
        password: String,
        passwordConfirmation: String,
        onSuccess: () -> Unit,
        onError: (code: Int) -> Unit
    ) {
        val piggyApi = RetrofitClient.getPiggyInstance(domain)
        viewModelScope.launch {
            try {
                val response = piggyApi.register(RegisterRequest(name, email, password, passwordConfirmation, deviceName()))
                if (response.isSuccessful) {
                    val token = response.body()!!.token
                    dataStoreRepository.saveAuthData(token, domain)
                    onSuccess()
                } else {
                    onError(response.code())
                    Log.e("register", response.errorBody()!!.string())
                }
            } catch (e: Exception) {
                onError(0)
                Log.e("register", e.toString())
            }
        }
    }
}
