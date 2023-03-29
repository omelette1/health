package com.quintonpyx.healthapp.networking

import com.quintonpyx.healthapp.model.ResponseObject
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call
import retrofit2.http.Headers

interface ApiService {

    @GET("v2/parser")
    fun getFood(
        @Query("ingr") key: String,
        @Query("app_id") appId:String = "422f54c0",
        @Query("app_key")appKey:String = "2e1b03b824db5d6af9e8ced4e184079c",

        ): Call<ResponseObject>
}