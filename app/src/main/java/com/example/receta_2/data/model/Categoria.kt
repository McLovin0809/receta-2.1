package com.example.receta_2.data.model

data class Categoria(
    val id: Int,
    val nombre: String,
    val subcategorias: List<Subcategoria> = emptyList(),
    val recetas: List<Receta> = emptyList()
)