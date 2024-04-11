package com.example.myfirstapp.viewmodels

import android.icu.text.LocaleDisplayNames.UiListItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.firebasewithmvvm.util.UiState
import com.example.myfirstapp.repositories.AuthRepository

class CreateAccountViewModel( private val authRepository: AuthRepository): ViewModel() {
    private val _register = MutableLiveData<UiState<String>>()
    val register: LiveData<UiState<String>>
        get() = _register

    fun register (email: String, password: String) {
        _register.value = UiState.Loading
        authRepository.registerUser(email, password) {
            _register.value = it
        }
    }

    class CreateAccountViewModelFactory (private val authRepository: AuthRepository) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(CreateAccountViewModel::class.java)) {
                CreateAccountViewModel(this.authRepository) as T
            } else {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }
    }
}