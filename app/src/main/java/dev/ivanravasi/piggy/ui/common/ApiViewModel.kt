package dev.ivanravasi.piggy.ui.common

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.ivanravasi.piggy.api.piggy.PiggyApi
import dev.ivanravasi.piggy.api.RetrofitClient
import dev.ivanravasi.piggy.data.TokenRepository
import java.net.SocketTimeoutException

open class ApiViewModel(
    private val tokenRepository: TokenRepository
): ViewModel() {
    protected lateinit var piggyApi: PiggyApi

    protected suspend fun hydrateApiClient() {
        piggyApi = RetrofitClient.getPiggyInstance(tokenRepository.getDomain()!!)
    }

    private val ERROR_NETWORK_TIMEOUT_MESSAGE = "There was a timeout in your request. Please try again."
    private val ERROR_GENERIC_MESSAGE = "Unexpected error. Please contact the app developer."

    private val _error = MutableLiveData<String>().apply { value = "" }
    val error: LiveData<String> = _error

    protected suspend fun tryApiRequest(logTag: String, block: suspend () -> Unit) {
        try {
            block()
        } catch (e: SocketTimeoutException) {
            _error.value = ERROR_NETWORK_TIMEOUT_MESSAGE
        } catch (e: Exception) {
            _error.value = ERROR_GENERIC_MESSAGE
            Log.e(logTag, e.toString())
        }
    }
}