package com.example.receta_2.data.model

data class Subcategoria(
    val id: Int,
    val nombre: String,
    val categoria: Categoria,
    val recetas: List<Receta> = emptyList()
)