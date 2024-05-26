package dev.ivanravasi.piggy.api

import dev.ivanravasi.piggy.api.bodies.entities.Property
import dev.ivanravasi.piggy.api.bodies.TokenCreateRequest
import dev.ivanravasi.piggy.api.bodies.TokenResponse
import dev.ivanravasi.piggy.api.bodies.meta.ListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface PiggyApi {
    @Headers("Accept: application/json")
    @POST("/api/token/create")
    suspend fun token(@Body loginBody: TokenCreateRequest): Response<TokenResponse>

    @Headers("Accept: application/json")
    @POST("/api/token/revoke")
    suspend fun revoke(@Header("Authorization") authHeader: String): Response<TokenResponse>

    @Headers("Accept: application/json")
    @GET("/api/properties")
    suspend fun properties(@Header("Authorization") authHeader: String): Response<ListResponse<Property>>
}