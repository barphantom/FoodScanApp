package com.example.foodscanapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authResult = MutableStateFlow<String?>(null)
    val authResult = _authResult.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                auth.createUserWithEmailAndPassword(email, password).await()
                _authResult.value = "Zarejestrowano pomyślnie"
            } catch (e: Exception) {
                _authResult.value = "Błąd rejestracji: ${e.message}"
            }
            _isLoading.value = false
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                _authResult.value = "Zalogowano pomyślnie"
                auth.currentUser?.getIdToken(true)
                    ?.addOnSuccessListener { result ->
                        val token = result.token
                        println("TOKEN: $token")
                    }
                    ?.addOnFailureListener { error ->
                        println("Błąd pobierania tokenu ${error.message}")
                    }
            } catch (e: Exception) {
                _authResult.value = "Bład logowania: ${e.message}"
            }
            _isLoading.value = false
        }
    }

    fun clearAuthResult() {
        _authResult.value = null
    }

    fun logout() {
        auth.signOut()
        clearAuthResult()
    }

    fun setAuthResult(message: String?) {
        _authResult.value = message
    }
}
