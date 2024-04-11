package com.example.myfirstapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.firebasewithmvvm.util.UiState
import com.example.myfirstapp.repositories.AuthRepository

class LoginViewModel (private val authRepository: AuthRepository): ViewModel() {
    private val _login = MutableLiveData<UiState<String>>()
    val login: LiveData<UiState<String>>
        get() = _login

    fun login(email: String, password: String) {
        _login.value = UiState.Loading
        authRepository.loginUser(email, password){
            _login.value = it
        }
    }

    class LoginViewModelFactory (private val authRepository: AuthRepository): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                LoginViewModel(this.authRepository) as T
            } else {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }
    }
}