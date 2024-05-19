package dev.ivanravasi.piggy.api

import dev.ivanravasi.piggy.api.bodies.TokenRequest
import dev.ivanravasi.piggy.api.bodies.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface PiggyApi {
    @Headers("Accept: application/json")
    @POST("/api/token")
    suspend fun token(@Body loginBody: TokenRequest): Response<TokenResponse>
}