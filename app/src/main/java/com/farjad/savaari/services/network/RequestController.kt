package com.farjad.savaari.services.network

import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object RequestController {

    private const val BASE_URL = "https://9d49e8524a75.ngrok.io/"
    private val webService = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
            .create(WebService::class.java)

    fun getWebService() : WebService {
        return webService
    }
}