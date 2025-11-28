package com.example.receta_2.data.model

data class Subcategoria(
    val id: Int,
    val nombre: String,
    val categoria: Categoria? = null // si el backend incluye la categoría
)
