package dev.ivanravasi.piggy.ui.properties

import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.ui.ApiViewModel

class AddPropertyViewModel(
    private val tokenRepository: TokenRepository
) : ApiViewModel(tokenRepository) {
}