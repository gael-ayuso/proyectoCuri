package ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import models.Contacto
import java.time.format.DateTimeFormatter

@Composable
fun TarjetaContacto(
    contacto: Contacto,
    onDelete: () -> Unit // Le pasamos la acción de borrar como parámetro
) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Nombre: ${contacto.name}", style = MaterialTheme.typography.bodyLarge)
                val fechaVisual = contacto.birthDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                Text("Cumple: $fechaVisual | Tel: ${contacto.phoneNumber}")
                Text("Email: ${contacto.email}")
            }

            Button(
                onClick = onDelete,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Borrar")
            }
        }
    }
}