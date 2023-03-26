package com.quintonpyx.healthapp.networking

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {

    companion object {
        fun getApiService(): ApiService {

            // API response interceptor
            val loggingInterceptor = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)

            // Client
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                 .build()

            // Retrofit
            val retrofit = Retrofit.Builder()
                .baseUrl("https://edamam-food-and-grocery-database.p.rapidapi.com/")
                .addConverterFactory(GsonConverterFactory.create())

                .client(client)
                .build()

            return retrofit.create(ApiService::class.java)
        }

//        const val API_KEY = "Your API Key"
    }
}