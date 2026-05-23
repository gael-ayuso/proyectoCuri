package ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.*
import androidx.compose.runtime.*

@Composable
fun MenuFiltros(
    onOrdenCambio: (Int) -> Unit
) {
    var expandido by remember { mutableStateOf(false) }
    val opciones = listOf("Por Defecto", "A-Z (Alfabético)", "Próximos Cumpleaños")
    var seleccionActual by remember { mutableStateOf(opciones[0]) }

    Box {
        // Usamos la flecha Unicode "▼" directamente en el string, cero dependencias extra
        OutlinedButton(onClick = { expandido = true }) {
            Text("Ordenar: $seleccionActual ▼")
        }

        DropdownMenu(
            expanded = expandido,
            onDismissRequest = { expandido = false }
        ) {
            opciones.forEachIndexed { index, opcion ->
                DropdownMenuItem(
                    text = { Text(opcion) },
                    onClick = {
                        seleccionActual = opcion
                        expandido = false
                        onOrdenCambio(index)
                    }
                )
            }
        }
    }
}