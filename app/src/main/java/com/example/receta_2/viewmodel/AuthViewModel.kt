package com.example.receta_2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.receta_2.data.model.Usuario
import com.example.receta_2.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class AuthViewModel(
    private val authRepository: AuthRepository

) : ViewModel() {

    private val _userId = MutableStateFlow<Int?>(null)
    val userId: StateFlow<Int?> = _userId
    private val _user = MutableStateFlow<Usuario?>(null)
    val user: StateFlow<Usuario?> = _user

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = authRepository.login(email, password)
                if (response.isSuccessful) {
                    _isLoggedIn.value = true
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = "Credenciales incorrectas"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión"
            }
        }
    }
    fun register(usuario: Usuario, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val res = authRepository.registrar( usuario.nombre, usuario.email, usuario.password)
                if (res.isSuccessful) {
                    onSuccess()
                } else {
                    _error.value = "Error al registrar"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión"
            }
        }
    }

    fun setError(msg: String) {
        _errorMessage.value = msg
    }

    fun logout() {
        _isLoggedIn.value = false
    }
}


