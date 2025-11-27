package com.example.receta_2.data.model

data class Receta(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val ingredientes: String,
    val instrucciones: String,
    val usuario: Usuario,
    val categoria: Categoria,
    val subcategoria: Subcategoria
)