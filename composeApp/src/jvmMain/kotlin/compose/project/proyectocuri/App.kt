package compose.project.proyectocuri

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import controllers.ContactoController
import models.Contacto
import java.time.LocalDate

@Composable
fun App() {
    val controller = remember { ContactoController() }
    var listaContactos by remember { mutableStateOf(controller.readAll()) }

    // Este estado controla si la zona de agregar contacto es visible o no
    var mostrarFormulario by remember { mutableStateOf(false) }

    // Estados para los campos de texto dentro del formulario
    var txtNombre by remember { mutableStateOf("") }
    var txtFecha by remember { mutableStateOf("2000-01-01") }
    var txtTelefono by remember { mutableStateOf("") }
    var txtCorreo by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }

    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Button(
                onClick = { mostrarFormulario = true },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Text("Agregar contacto")
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Lista de contactos
            Text("Lista de Contactos", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)) {
                items(listaContactos) { contacto ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Nombre: ${contacto.name}", style = MaterialTheme.typography.bodyLarge)
                            Text("Cumple: ${contacto.birthDate} | Tel: ${contacto.phoneNumber}")
                            Text("Email: ${contacto.email}")
                        }
                    }
                }
            }
        }

        // Pop up para agregar contactos
        if (mostrarFormulario) {
            AlertDialog(
                onDismissRequest = { mostrarFormulario = false }, // se cierra con un missclick xd
                title = { Text("Nuevo Contacto") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        TextField(value = txtNombre, onValueChange = { txtNombre = it }, label = { Text("Nombre") })
                        TextField(value = txtFecha, onValueChange = { txtFecha = it }, label = { Text("Fecha (AAAA-MM-DD)") })
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
                            val fecha = LocalDate.parse(txtFecha)
                            val nuevoContacto = Contacto(txtNombre, fecha, txtTelefono, txtCorreo)

                            controller.create(nuevoContacto)
                            listaContactos = controller.readAll()

                            // clean all
                            txtNombre = ""
                            txtTelefono = ""
                            txtCorreo = ""
                            mensajeError = ""
                            mostrarFormulario = false
                        } catch (e: Exception) {
                            mensajeError = e.message ?: "Datos inválidos"
                        }
                    }) {
                        Text("Guardar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarFormulario = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}