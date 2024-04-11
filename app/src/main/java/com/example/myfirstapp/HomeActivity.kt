package com.example.myfirstapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.firebasewithmvvm.util.UiState
import com.example.myfirstapp.adapters.HomeAdapter
import com.example.myfirstapp.databinding.ActivityHomeBinding
import com.example.myfirstapp.repositories.ProductRepository
import com.example.myfirstapp.rest.RetrofitService
import com.example.myfirstapp.viewmodels.HomeViewModel

class HomeActivity : AppCompatActivity() {
    // variables and states
    lateinit var viewModel: HomeViewModel
    private lateinit var binding: ActivityHomeBinding
    private val retrofitService = RetrofitService.getInstance()
    private val adapter = HomeAdapter{ product ->
        Toast.makeText(this, product.title, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initialize viewModel passing all parameters
        viewModel =
            ViewModelProvider(this, HomeViewModel.HomeViewModelFactory(ProductRepository(retrofitService))).get(
                HomeViewModel::class.java
            )

        // init ricycle view
        binding.recyclerview.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        observer()
    }

    private fun observer() {
        viewModel.productsList.observe(this) { state ->
            when(state) {
                is UiState.Loading -> {
                    binding.progressBar.show()
                }

                is UiState.Failure -> {
                    binding.progressBar.hide()
                    Toast.makeText(this, state.error, Toast.LENGTH_LONG).show()
                }

                is UiState.Success -> {
                    binding.progressBar.hide()
                    adapter.setProductsList(state.data)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.getAllProducts()
    }
}

