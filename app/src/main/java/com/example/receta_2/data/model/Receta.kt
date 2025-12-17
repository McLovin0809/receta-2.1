// com/example/receta_2/data/model/Receta.kt
package com.example.receta_2.data.model

data class Receta(
    val id: Int? = null,
    val titulo: String,
    val descripcion: String,
    val ingredientes: String,
    val instrucciones: String,
    val usuario: IdWrapper,
    val categoria: IdWrapper,
    val subcategoria: IdWrapper
)

data class IdWrapper(
    val id: Int
)