package dev.ivanravasi.piggy.ui.auth.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.ivanravasi.piggy.api.RetrofitClient
import dev.ivanravasi.piggy.api.piggy.bodies.requests.TokenCreateRequest
import dev.ivanravasi.piggy.data.DataStoreRepository
import dev.ivanravasi.piggy.ui.auth.ModelUtils.deviceName
import kotlinx.coroutines.launch

open class LoginViewModel(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {
    fun requestToken(
        domain: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (code: Int) -> Unit
    ) {
        val piggyApi = RetrofitClient.getPiggyInstance(domain)
        viewModelScope.launch {
            try {
                val response = piggyApi.token(TokenCreateRequest(email, password, deviceName()))
                if (response.isSuccessful) {
                    val token = response.body()!!.token
                    dataStoreRepository.saveAuthData(token, domain)
                    onSuccess()
                } else {
                    onError(response.code())
                    Log.e("tokenRequest", response.errorBody()!!.string())
                }
            } catch (e: Exception) {
                onError(0)
                Log.e("tokenRequest", e.toString())
            }
        }
    }
}