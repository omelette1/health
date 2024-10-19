package com.quintonpyx.healthapp.networking

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig { // Use object to create a singleton

    private const val BASE_URL = "https://api.spoonacular.com/"
    private const val API_KEY = "dd4d8e8932d643318c590e3e29d45bf5" // Change this to your method of getting the API key

    fun getApiService(): SpoonacularApiService {
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .header("Authorization", "Bearer $API_KEY") // Add authorization header if needed
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(SpoonacularApiService::class.java)
    }
}
