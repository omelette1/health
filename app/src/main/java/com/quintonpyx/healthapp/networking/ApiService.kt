package com.quintonpyx.healthapp.networking

import com.quintonpyx.healthapp.model.ResponseObject
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call
import retrofit2.http.Headers

interface ApiService {
    @Headers(
        "X-RapidAPI-Key:c4e567f282msh35734da17d5f182p1a9b11jsne27d65ec649f",
        "X-RapidAPI-Host:edamam-food-and-grocery-database.p.rapidapi.com"
    )
    @GET("parser")
    fun getFood(
        @Query("ingr") key: String,

        ): Call<ResponseObject>
}