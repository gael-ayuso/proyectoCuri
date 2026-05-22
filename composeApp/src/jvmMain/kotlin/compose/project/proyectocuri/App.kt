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

        }

    }
}