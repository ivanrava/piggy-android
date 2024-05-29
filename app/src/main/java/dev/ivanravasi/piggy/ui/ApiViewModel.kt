package dev.ivanravasi.piggy.ui

import androidx.lifecycle.ViewModel
import dev.ivanravasi.piggy.api.piggy.PiggyApi
import dev.ivanravasi.piggy.api.piggy.RetrofitClient
import dev.ivanravasi.piggy.data.TokenRepository

open class ApiViewModel(
    private val tokenRepository: TokenRepository
): ViewModel() {
    protected lateinit var piggyApi: PiggyApi

    protected suspend fun hydrateApiClient() {
        piggyApi = RetrofitClient.getInstance(tokenRepository.getDomain()!!)
    }
}