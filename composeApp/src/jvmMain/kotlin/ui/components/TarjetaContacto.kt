package ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import models.Contacto
import java.time.format.DateTimeFormatter

@Composable
fun TarjetaContacto(
    contacto: Contacto,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit
) {
    var expandido by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { expandido = !expandido } // Al hacer clic en cualquier parte, cambia el estado
            .animateContentSize(), // ¡Esta línea hace toda la magia de la animación suave!
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp) // Sombra sutil
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = contacto.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                // Flecha indicadora que cambia según el estado
                Text(if (expandido) "▲" else "▼", style = MaterialTheme.typography.bodyLarge)
            }

            if (expandido) {
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
                Spacer(modifier = Modifier.height(12.dp))

                val fechaVisual = contacto.birthDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

                Text("Cumpleaños: $fechaVisual", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(6.dp))
                Text("Teléfono: ${contacto.phoneNumber}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(6.dp))
                Text("Correo: ${contacto.email}", style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onEditClick,
                        modifier = Modifier.padding(end = 8.dp)
                    ) { Text("Editar ") }

                    Button(
                        onClick = onDeleteClick,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) { Text("Borrar ") }
                }
            }
        }
    }
}

@Preview
@Composable
fun TarjetaContactoPreview() {
    TarjetaContacto(
        contacto = Contacto(
            name = "Juan",
            phoneNumber = "1234567890",
            email = "hola@gmail.com",
            birthDate = java.time.LocalDate.now()
        ),
        onDeleteClick = {},
        onEditClick = {}
    )
}