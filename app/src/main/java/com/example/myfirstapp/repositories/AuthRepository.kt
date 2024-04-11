package com.example.myfirstapp.repositories

import android.util.Log
import com.example.firebasewithmvvm.util.UiState
import com.google.firebase.auth.FirebaseAuth

class AuthRepository(val auth : FirebaseAuth) {
    fun registerUser(email: String, password: String, result: (UiState<String>) -> Unit) {
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("my-app", "Usuário criado com sucesso")
                    result.invoke(UiState.Success("Registro realizado com sucesso"))
                }
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure("Falha na criação"))
            }
    }

    fun loginUser(email: String, password: String, result: (UiState<String>) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("my-app", "Usuario logado com sucesso")
                    result.invoke(UiState.Success("Login realizado com sucesso"))
                }
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure("Falha na autenticação. Verifique email e senha"))
            }
    }
}