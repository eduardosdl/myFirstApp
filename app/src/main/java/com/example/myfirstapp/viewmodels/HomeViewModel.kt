package com.example.myfirstapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.firebasewithmvvm.util.UiState
import com.example.myfirstapp.models.Product
import com.example.myfirstapp.models.ProductsResponse
import com.example.myfirstapp.repositories.ProductRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel (private val productRepository: ProductRepository) : ViewModel() {
    // mutable states to be observable
    private val _productsList = MutableLiveData<UiState<List<Product>>>()
    val productsList: LiveData<UiState<List<Product>>>
        get() = _productsList

    fun getAllProducts() {
        // set loading
        _productsList.value = UiState.Loading
        // init request
        val request = productRepository.getAllProducts()
        request.enqueue(object : Callback<ProductsResponse> {
            // method on response is success
            override fun onResponse(
                call: Call<ProductsResponse>,
                response: Response<ProductsResponse>
            ) {
                val productsResponse = response.body()
                if (productsResponse != null) {
                    Log.d("my-app", productsResponse.toString())
                    _productsList.value = UiState.Success(productsResponse.products)
                } else {
                    Log.e("my-app", "Resposta vazia ou nula")
                }
            }

            // method on response is failure
            override fun onFailure(call: Call<ProductsResponse>, t: Throwable) {
                Log.e("my-app", "Erro na chamada da API", t)
                _productsList.value = UiState.Failure("Houve um erro na chamada da API")
            }
        })
    }

    class HomeViewModelFactory (private val productsRepository: ProductRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                HomeViewModel(this.productsRepository) as T
            } else {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }
    }
}