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
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioContacto(
    contactoAEditar: Contacto? = null, // NUEVO: Si llega nulo es Creación, si trae datos es Edición
    onDismiss: () -> Unit,
    onSave: (Contacto) -> Unit
) {
    // Inicializamos con los datos del contacto o vacíos
    var txtNombre by remember { mutableStateOf(contactoAEditar?.name ?: "") }
    var txtTelefono by remember { mutableStateOf(contactoAEditar?.phoneNumber ?: "") }
    var txtCorreo by remember { mutableStateOf(contactoAEditar?.email ?: "") }
    var mensajeError by remember { mutableStateOf("") }

    var fechaSeleccionada by remember { mutableStateOf(contactoAEditar?.birthDate ?: LocalDate.now()) }
    var mostrarCalendario by remember { mutableStateOf(false) }
    val formateadorVista = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    // Inicializamos el calendario en la fecha del contacto
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = contactoAEditar?.birthDate?.atStartOfDay(ZoneOffset.UTC)?.toInstant()?.toEpochMilli()
    )

    val esEdicion = contactoAEditar != null

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (esEdicion) "Editar Contacto" else "Nuevo Contacto") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // El nombre se bloquea en edición porque es nuestro identificador para buscar en el JSON
                OutlinedTextField(
                    value = txtNombre,
                    onValueChange = { txtNombre = it },
                    label = { Text("Nombre") },
                    enabled = !esEdicion
                )

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

                OutlinedTextField(value = txtTelefono, onValueChange = { txtTelefono = it }, label = { Text("Teléfono") })
                OutlinedTextField(value = txtCorreo, onValueChange = { txtCorreo = it }, label = { Text("Correo") })

                if (mensajeError.isNotEmpty()) {
                    Text(mensajeError, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                try {
                    val contactoGuardado = Contacto(txtNombre, fechaSeleccionada, txtTelefono, txtCorreo)
                    onSave(contactoGuardado)
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
        ) { DatePicker(state = datePickerState) }
    }
}