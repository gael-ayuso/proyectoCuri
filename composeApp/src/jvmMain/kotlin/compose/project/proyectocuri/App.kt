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

    }
}