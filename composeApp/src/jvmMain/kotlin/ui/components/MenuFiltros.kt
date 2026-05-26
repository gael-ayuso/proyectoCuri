package ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MenuFiltros(
    onOrdenCambio: (Int) -> Unit
) {
    var expandido by remember { mutableStateOf(false) }
    val opciones = listOf("Por Defecto", "A-Z (Alfabético)", "Próximos Cumpleaños")
    var seleccionActual by remember { mutableStateOf(opciones[0]) }

    Box {
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
                        expandido = false
                        seleccionActual = opcion
                        onOrdenCambio(index)
                    }
                )
            }
        }
    }
}
@Preview (showBackground = true, backgroundColor = 0xFFFFFFFF, showSystemUi = true)
@Composable
fun MenuFiltrosPreview() {
    MenuFiltros(
        onOrdenCambio = {}
    )
}
