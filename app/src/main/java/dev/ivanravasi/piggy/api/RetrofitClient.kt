package dev.ivanravasi.piggy.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    fun getInstance(domain: String): PiggyApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(domain)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(PiggyApi::class.java)
    }

}