package compose.project.proyectocuri

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import controllers.ContactoController
import models.Contacto
import ui.components.FormularioContacto
import ui.components.MenuFiltros
import ui.components.TarjetaContacto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val controller = remember { ContactoController() }

    var listaCompleta by remember { mutableStateOf(controller.readAll()) }
    var mostrarFormulario by remember { mutableStateOf(false) }
    var textoBusqueda by remember { mutableStateOf("") }
    var contactoParaBorrar by remember { mutableStateOf<Contacto?>(null) }
    var contactoEnEdicion by remember { mutableStateOf<Contacto?>(null) }

    val listaMostrada = if (textoBusqueda.isBlank()) {
        listaCompleta
    } else {
        listaCompleta.filter { it.name.contains(textoBusqueda, ignoreCase = true) }
    }

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Directorio Telefónico", style = MaterialTheme.typography.titleLarge) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { mostrarFormulario = true },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) { Text("+", style = MaterialTheme.typography.headlineMedium) }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = textoBusqueda,
                    onValueChange = { textoBusqueda = it },
                    label = { Text("Buscar contacto") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                MenuFiltros(
                    onOrdenCambio = { indiceSeleccionado ->
                        when (indiceSeleccionado) {
                            0 -> listaCompleta = controller.readAll()
                            1 -> listaCompleta = controller.obtenerContactosOrdenAlfabetico()
                            2 -> listaCompleta = controller.obtenerContactosPorCumpleanosProximo()
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)) {
                    items(listaMostrada) { contacto ->
                        TarjetaContacto(
                            contacto = contacto,
                            onDeleteClick = {
                                contactoParaBorrar = contacto
                            },
                            onEditClick = {
                                contactoEnEdicion = contacto
                                mostrarFormulario = true
                            }
                        )
                    }
                }
            }
        }

        // CONFIRMACIÓN DE BORRADO
        if (contactoParaBorrar != null) {
            AlertDialog(
                onDismissRequest = { contactoParaBorrar = null },
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

        // Formulario de agregación/edición
        if (mostrarFormulario) {
            FormularioContacto(
                contactoAEditar = contactoEnEdicion,
                onDismiss = {
                    mostrarFormulario = false
                    contactoEnEdicion = null
                },
                onSave = { contactoGuardado ->
                    if (contactoEnEdicion != null) {
                        controller.update(contactoGuardado)
                    } else {
                        controller.create(contactoGuardado)
                    }

                    listaCompleta = controller.readAll()
                    mostrarFormulario = false
                    contactoEnEdicion = null
                }
            )
        }
    }
}

@Preview
@Composable
fun AppPreview() {
    App()
}