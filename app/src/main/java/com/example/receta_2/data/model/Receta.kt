package com.example.receta_2.data.model

data class Receta(
    val id: Int? = null,
    val titulo: String,
    val descripcion: String,
    val ingredientes: String,
    val instrucciones: String,
    val usuario: Usuario? = null,
    val categoria: Categoria? = null,
    val subcategoria: Subcategoria? = null,
    var imagenUrl: String? = null
)
