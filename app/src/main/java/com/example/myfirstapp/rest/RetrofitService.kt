package com.example.myfirstapp.rest

import com.example.myfirstapp.models.ProductsResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface RetrofitService {
    // setup get all products request
    @GET("products")
    fun getAllProducts(): Call<ProductsResponse>

    companion object {
        private val retrofitService: RetrofitService by lazy {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://dummyjson.com/")
                .addConverterFactory(GsonConverterFactory.create()).
                build()
            retrofit.create(RetrofitService::class.java)
        }

        // static function to initilize retrofit service
        fun getInstance(): RetrofitService {
            return retrofitService
        }
    }
}