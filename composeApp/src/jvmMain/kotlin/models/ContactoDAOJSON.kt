package models

import models.Contacto
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import java.io.File

class ContactoDAOJSON : ContactoDAO {

    private val archivo = File("data/contactos.json")

    override fun readAll(): List<Contacto> {
        // Si el archivo no existe o está vacío, devolvemos una lista vacía
        if (!archivo.exists() || archivo.readText().isBlank()) {
            println("No existe el archivos")
            return emptyList()
        }

        val textoJson = archivo.readText()
        return Json.decodeFromString(textoJson)
    }

    override fun create(contacto: Contacto) {
        val listaActual = readAll().toMutableList()
        listaActual.add(contacto)
        guardarTodos(listaActual)
    }

    override fun readByName(name: String): Contacto? {
        // ignoramos mayúsculas y minúsculas
        return readAll().find { it.name.equals(name, ignoreCase = true) }
    }

    override fun update(contacto: Contacto) {
        val listaActual = readAll().toMutableList()
        // Ahora buscamos exactamente por el ID invisible
        val indice = listaActual.indexOfFirst { it.id == contacto.id }

        if (indice != -1) {
            listaActual[indice] = contacto
            guardarTodos(listaActual)
        }
    }

    override fun delete(contacto: Contacto) {
        val listaActual = readAll().toMutableList()
        // Borramos usando una función que filtra por el ID
        listaActual.removeAll { it.id == contacto.id }
        guardarTodos(listaActual)
    }

    // evita repetir código
    private fun guardarTodos(lista: List<Contacto>) {
        val textoJson = Json.encodeToString(lista)
        archivo.writeText(textoJson)
    }
}