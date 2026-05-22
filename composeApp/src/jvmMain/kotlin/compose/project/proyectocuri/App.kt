package compose.project.proyectocuri

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import controllers.ContactoController
import models.Contacto
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun App() {
    val controller = remember { ContactoController() }
    var listaContactos by remember { mutableStateOf(controller.readAll()) }
    var mostrarFormulario by remember { mutableStateOf(false) }

    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Button(
                onClick = { mostrarFormulario = true },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Text("Agregar contacto")
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text("Lista de Contactos", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

            ListaContactos(
                contactos = listaContactos,
                onDelete = { contactoABorrar ->
                    controller.delete(contactoABorrar)
                    listaContactos = controller.readAll()
                }
            )
        }

        if (mostrarFormulario) {
            FormularioContacto(
                onDismiss = { mostrarFormulario = false },
                onSave = { nuevoContacto ->
                    controller.create(nuevoContacto)
                    listaContactos = controller.readAll()
                    mostrarFormulario = false
                }
            )
        }
    }
}

/**
 * Formulario y calendario
 * */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioContacto(onDismiss: () -> Unit, onSave: (Contacto) -> Unit) {
    var txtNombre by remember { mutableStateOf("") }
    var txtTelefono by remember { mutableStateOf("") }
    var txtCorreo by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }

    var fechaSeleccionada by remember { mutableStateOf(LocalDate.now()) }
    var mostrarCalendario by remember { mutableStateOf(false) }
    val formateadorVista = DateTimeFormatter.ofPattern("dd/MM/yyyy") // Formato Día/Mes/Año

    val datePickerState = rememberDatePickerState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo Contacto") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TextField(value = txtNombre, onValueChange = { txtNombre = it }, label = { Text("Nombre") })

                // Abre el calendario interactivo
                OutlinedTextField(
                    value = fechaSeleccionada.format(formateadorVista),
                    onValueChange = { },
                    label = { Text("Fecha de Cumpleaños") },
                    enabled = false, // Lo deshabilitamos para que no escriban a mano
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { mostrarCalendario = true }, // Al hacer clic, abre el Dialog del calendario
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
                    mensajeError = e.message ?: "Datos inválidos"
                }
            }) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )

    // Calendario interactivo
    if (mostrarCalendario) {
        DatePickerDialog(
            onDismissRequest = { mostrarCalendario = false },
            confirmButton = {
                TextButton(onClick = {
                    // Convertimos ms a localDate
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

/**
 * Lista de contactos
 * */
@Composable
fun ListaContactos(contactos: List<Contacto>, onDelete: (Contacto) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(contactos) { contacto ->
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Nombre: ${contacto.name}", style = MaterialTheme.typography.bodyLarge)
                        // Mostramos la fecha formateada en la lista también
                        val fechaVisual = contacto.birthDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        Text("Cumpleaños: $fechaVisual | Telefono: ${contacto.phoneNumber}")
                        Text("Email: ${contacto.email}")
                    }

                    Button(
                        onClick = { onDelete(contacto) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) { Text("Borrar") }
                }
            }
        }
    }
}