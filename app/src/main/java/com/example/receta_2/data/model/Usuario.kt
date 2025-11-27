package com.example.receta_2.data.model

data class Usuario(
    val id: Int? = null,
    val nombre: String,
    val email: String,
    val password: String? = null
)