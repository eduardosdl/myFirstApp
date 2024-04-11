package com.example.myfirstapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.firebasewithmvvm.util.UiState
import com.example.myfirstapp.databinding.ActivityLoginBinding
import com.example.myfirstapp.repositories.AuthRepository
import com.example.myfirstapp.viewmodels.LoginViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {
    lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var result: ActivityResultLauncher<Intent>
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        viewModel = ViewModelProvider(this, LoginViewModel.LoginViewModelFactory(AuthRepository(auth))).get(
            LoginViewModel::class.java
        )
    }

    override fun onStart() {
        super.onStart()
        observer()

        result = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == 1) {
                finish()
            }
        }

        binding.inputEmail.addTextChangedListener {
            binding.emailLayout.error = null
        }
        binding.inputPassword.addTextChangedListener {
            binding.passwordLayout.error = null
        }

        binding.btnSubmitLogin.setOnClickListener {
            val email = binding.inputEmail.text.toString()
            val password = binding.inputPassword.text.toString()

            if (validate(email, password)){
                viewModel.login(email, password)
            }
        }

        binding.btnCreateAccount.setOnClickListener {
            val intent = Intent(this, CreateAccoutActivity::class.java)
            result.launch(intent)
        }
    }

    private fun observer() {
        viewModel.login.observe(this) { state ->
            when(state) {
                is UiState.Loading -> {
                    binding.btnSubmitLogin.setText("")
                    binding.btnProgress.show()
                }

                is UiState.Failure -> {
                    binding.btnProgress.hide()
                    binding.btnSubmitLogin.setText("Entrar")
                    Toast.makeText(this, state.error, Toast.LENGTH_SHORT).show()
                }

                is UiState.Success -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }
            }
        }
    }

    fun validate(email: String, password: String): Boolean {
        var isValid = true
        if (email.isNullOrEmpty()){
            Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT)
                .show()
            binding.emailLayout.error = "Informe um email"
            isValid = false
        }
        if (password.isNullOrEmpty()){
            Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT)
                .show()
            binding.passwordLayout.error = "Informe uma senha"
            isValid = false
        }
        return isValid
    }
}