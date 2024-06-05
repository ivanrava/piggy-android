package dev.ivanravasi.piggy.api.iconify

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface IconifyApi {
    @Headers("Accept: application/json")
    @GET("/search")
    suspend fun search(@Query("query") query: String): Response<IconifySearchResponse>
}