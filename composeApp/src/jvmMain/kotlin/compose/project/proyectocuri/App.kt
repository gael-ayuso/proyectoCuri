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
import ui.components.FormularioContacto
import ui.components.MenuFiltros
import ui.components.TarjetaContacto

@Composable
fun App() {
    val controller = remember { ContactoController() }

    var listaCompleta by remember { mutableStateOf(controller.readAll()) }
    var mostrarFormulario by remember { mutableStateOf(false) }
    var textoBusqueda by remember { mutableStateOf("") }
    var contactoParaBorrar by remember { mutableStateOf<Contacto?>(null) }

    val listaMostrada = if (textoBusqueda.isBlank()) {
        listaCompleta
    } else {
        listaCompleta.filter { it.name.contains(textoBusqueda, ignoreCase = true) }
    }

    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

            Button(
                onClick = { mostrarFormulario = true },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) { Text("Agregar contacto") }

            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { textoBusqueda = it },
                label = { Text("Buscar") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            MenuFiltros(
                onOrdenCambio = { indiceSeleccionado ->
                    when (indiceSeleccionado) {
                        0 -> listaCompleta = controller.readAll() // Por Defecto
                        1 -> listaCompleta = controller.obtenerContactosOrdenAlfabetico() // A-Z
                        2 -> listaCompleta = controller.obtenerContactosPorCumpleanosProximo() // Cumpleaños
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text("Directorio", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)) {
                items(listaMostrada) { contacto ->
                    TarjetaContacto(
                        contacto = contacto,
                        onDeleteClick = {
                            // En lugar de borrarlo directamente, lo guardamos en el estado
                            // Esto detonará automáticamente el AlertDialog de abajo
                            contactoParaBorrar = contacto
                        },
                        onEditClick = {
                            TODO("Implementar la lógica de edición")
                        }
                    )
                }
            }
        }

        // CONFIRMACIÓN DE BORRADO
        if (contactoParaBorrar != null) {
            AlertDialog(
                onDismissRequest = { contactoParaBorrar = null }, // se cancela con missclick
                title = { Text("Confirmar eliminación") },
                text = { Text("¿Estás seguro de que deseas eliminar a ${contactoParaBorrar?.name}? Esta acción no se puede deshacer.") },
                confirmButton = {
                    Button(
                        onClick = {
                            controller.delete(contactoParaBorrar!!)
                            listaCompleta = controller.readAll()
                            contactoParaBorrar = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) { Text("Sí, eliminar")}
                }, dismissButton = {
                    TextButton(onClick = { contactoParaBorrar = null })
                    { Text("Cancelar") }
                }
            )
        }

        if (mostrarFormulario) {
            FormularioContacto(
                onDismiss = { mostrarFormulario = false },
                onSave = { nuevoContacto ->
                    controller.create(nuevoContacto)
                    listaCompleta = controller.readAll()
                    mostrarFormulario = false
                }
            )
        }
    }
}