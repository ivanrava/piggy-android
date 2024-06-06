package dev.ivanravasi.piggy.api

import dev.ivanravasi.piggy.api.iconify.IconifyApi
import dev.ivanravasi.piggy.api.piggy.PiggyApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {
    private fun buildRetrofit(domain: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(domain)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getPiggyInstance(domain: String): PiggyApi {
        return buildRetrofit(domain).create(PiggyApi::class.java)
    }

    fun getIconifyInstance(): IconifyApi {
        return buildRetrofit("https://api.iconify.design").create(IconifyApi::class.java)
    }
}