package ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import models.Contacto
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioContacto(
    onDismiss: () -> Unit,
    onSave: (Contacto) -> Unit
) {
    var txtNombre by remember { mutableStateOf("") }
    var txtTelefono by remember { mutableStateOf("") }
    var txtCorreo by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }

    var fechaSeleccionada by remember { mutableStateOf(LocalDate.now()) }
    var mostrarCalendario by remember { mutableStateOf(false) }
    val formateadorVista = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val datePickerState = rememberDatePickerState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo Contacto") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TextField(value = txtNombre, onValueChange = { txtNombre = it }, label = { Text("Nombre") })

                OutlinedTextField(
                    value = fechaSeleccionada.format(formateadorVista),
                    onValueChange = { },
                    label = { Text("Fecha de Cumpleaños") },
                    enabled = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { mostrarCalendario = true },
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                TextField(value = txtTelefono, onValueChange = { txtTelefono = it }, label = { Text("Teléfono") })
                TextField(value = txtCorreo, onValueChange = { txtCorreo = it }, label = { Text("Correo") })

                if (mensajeError.isNotEmpty()) {
                    Text(mensajeError, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                try {
                    val nuevoContacto = Contacto(txtNombre, fechaSeleccionada, txtTelefono, txtCorreo)
                    onSave(nuevoContacto)
                } catch (e: Exception) {
                    mensajeError = e.message ?: "Datos inválidos. Revisa el formato del correo."
                }
            }) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )

    if (mostrarCalendario) {
        DatePickerDialog(
            onDismissRequest = { mostrarCalendario = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        fechaSeleccionada = Instant.ofEpochMilli(millis).atZone(ZoneId.of("UTC")).toLocalDate()
                    }
                    mostrarCalendario = false
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarCalendario = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}