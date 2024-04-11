package com.example.myfirstapp.repositories

import com.example.myfirstapp.rest.RetrofitService

class ProductRepository (private val retrofitService: RetrofitService) {
    // implement get all products methods
    fun getAllProducts() = retrofitService.getAllProducts()
}