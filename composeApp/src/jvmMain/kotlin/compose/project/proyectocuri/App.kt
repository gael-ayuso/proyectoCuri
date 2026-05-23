package compose.project.proyectocuri

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import controllers.ContactoController
import ui.components.FormularioContacto
import ui.components.TarjetaContacto

@Composable
fun App() {
    val controller = remember { ContactoController() }

    var listaCompleta by remember { mutableStateOf(controller.readAll()) }
    var mostrarFormulario by remember { mutableStateOf(false) }
    var textoBusqueda by remember { mutableStateOf("") }

    // Buscador reactivo
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
            ) {
                Text("Agregar contacto")
            }

            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { textoBusqueda = it }, // Actualiza la búsqueda al teclear
                label = { Text(" Buscar") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Botones de ordenamiento
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { listaCompleta = controller.obtenerContactosOrdenAlfabetico() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) { Text("A-Z") }

                Button(
                    onClick = { listaCompleta = controller.obtenerContactosPorCumpleanosProximo() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) { Text("Cumpleanios") }

                Button(
                    onClick = { listaCompleta = controller.readAll() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) { Text("Restaurar") }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Directorio", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

            //Lista de contactos
            LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)) {
                // Usamos la lista filtrada, no la original
                items(listaMostrada) { contacto -> TarjetaContacto(
                        contacto = contacto,
                        onDelete = {
                            controller.delete(contacto)
                            listaCompleta = controller.readAll() // Refrescamos la fuente de verdad
                        }
                    )
                }
            }
        }

        if (mostrarFormulario) {
            FormularioContacto(
                onDismiss = { mostrarFormulario = false },
                onSave = { nuevoContacto ->
                    controller.create(nuevoContacto)
                    listaCompleta = controller.readAll() // Refrescamos la lista original
                    mostrarFormulario = false
                }
            )
        }
    }
}