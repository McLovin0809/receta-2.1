package com.example.receta_2.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.lifecycle.viewModelScope
import com.example.receta_2.data.model.Usuario
import com.example.receta_2.data.repository.UsuarioRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val usuarioRepository = UsuarioRepository()

    // Estado de login
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    // Estado de errores
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // Función para login
    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val usuario = usuarioRepository.login(email, password)
                if (usuario != null) {
                    _isLoggedIn.value = true // Aquí debería cambiar a true
                    _errorMessage.value = null // Limpiar errores
                } else {
                    _errorMessage.value = "Correo o contraseña incorrectos"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al iniciar sesión: ${e.message}"
            }
        }
    }

    // Función para logout
    fun logout() {
        _isLoggedIn.value = false
    }

    // Función para registro
    fun register(nombre: String, email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val nuevoUsuario = Usuario(nombre = nombre, email = email, password = password)
                val response = usuarioRepository.crearUsuario(nuevoUsuario) // Suponiendo que este método realiza la llamada a la API
                if (response.isSuccessful) {
                    // Si la respuesta es exitosa, ejecutamos el callback onSuccess
                    onSuccess()
                    _errorMessage.value = null // Limpiar errores
                } else {
                    // Si el código de respuesta no es exitoso, mostramos el error
                    _errorMessage.value = "Error al registrar: ${response.code()}"
                }
            } catch (e: Exception) {
                // Si ocurre una excepción, mostramos el error en el mensaje
                _errorMessage.value = "Excepción: ${e.message}"
            }
        }
    }
}
