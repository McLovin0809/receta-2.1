// com/example/receta_2/ui/screens/RecetaCard.kt
package com.example.receta_2.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.receta_2.data.model.Receta


@Composable
fun RecetaCard(receta: Receta, onClick: () -> Unit) {
    Card(onClick = onClick) {
        Column(Modifier.padding(8.dp)) {
            Text(receta.titulo, style = MaterialTheme.typography.titleMedium)
            Text(receta.descripcion ?: "")
        }
    }
}
