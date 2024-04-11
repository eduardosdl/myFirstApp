package com.example.myfirstapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.firebasewithmvvm.util.UiState
import com.example.myfirstapp.databinding.ActivityCreateAccoutBinding
import com.example.myfirstapp.repositories.AuthRepository
import com.example.myfirstapp.viewmodels.CreateAccountViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class CreateAccoutActivity : AppCompatActivity() {
    lateinit var viewModel: CreateAccountViewModel
    private lateinit var binding: ActivityCreateAccoutBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth

        viewModel =
            ViewModelProvider(this, CreateAccountViewModel.CreateAccountViewModelFactory(AuthRepository(auth))).get(
                CreateAccountViewModel::class.java
            )
    }

    override fun onStart() {
        super.onStart()
        observer()

        binding.inputEmail.addTextChangedListener {
            binding.emailLayout.error = null
        }
        binding.inputPassword.addTextChangedListener {
            binding.passwordLayout.error = null
            binding.confirmPasswordLayout.error = null
        }
        binding.inputConfirmPassword.addTextChangedListener {
            binding.passwordLayout.error = null
            binding.confirmPasswordLayout.error = null
        }

        binding.btnSubmitCreate.setOnClickListener {
            val email = binding.inputEmail.text.toString()
            val password = binding.inputPassword.text.toString()
            val confirmPassword = binding.inputConfirmPassword.text.toString()

            if (validate(email, password, confirmPassword)) {
                viewModel.register(email, password)
            }
        }

        binding.btnLogin.setOnClickListener {
            val i = intent
            setResult(1, i)
            finish()
        }
    }

    fun observer() {
        viewModel.register.observe(this) {state ->
            when(state) {
                is UiState.Loading -> {
                    binding.btnSubmitCreate.setText("")
                    binding.btnProgress.show()
                }

                is UiState.Failure -> {
                    binding.btnProgress.hide()
                    binding.btnSubmitCreate.setText("Criar conta")
                    Toast.makeText(this, state.error, Toast.LENGTH_SHORT).show()
                }

                is UiState.Success -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    val i = intent
                    setResult(1, i)
                    finish()
                }
            }
        }
    }

    fun validate(email: String, password: String, confirmPassword: String) : Boolean {
        var isValid = true
        if (email.isNullOrEmpty()){
            Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT)
                .show()
            binding.emailLayout.error = "Informe um email"
            isValid = false
        }

        if (password.isNullOrEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT)
                .show()
            binding.passwordLayout.error = "Informe uma senha"
            isValid = false
        }

        if (confirmPassword.isNullOrEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT)
                .show()
            binding.confirmPasswordLayout.error = "Informe uma senha"
            isValid = false
        } else if (password != confirmPassword) {
            Toast.makeText(this, "As senhas estão diferentes", Toast.LENGTH_SHORT)
                .show()
            binding.passwordLayout.error = "Senhas diferentes"
            binding.confirmPasswordLayout.error = "Senhas diferentes"
            isValid = false
        }
        return isValid
    }
}