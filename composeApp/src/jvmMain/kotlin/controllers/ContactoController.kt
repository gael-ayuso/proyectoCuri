package controllers

import models.Contacto
import models.ContactoDAO
import models.ContactoDAOJSON
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class ContactoController : ContactoDAO {
    private val dao: ContactoDAO = ContactoDAOJSON()

    override fun create(contacto: Contacto) {
        val listaActual = dao.readAll()

        // Verificamos si ya existe alguien con:
        // mismo nombre o correo electrónico.
        val existeDuplicado = listaActual.any {
            it.name.equals(contacto.name, ignoreCase = true) ||
                    it.email.equals(contacto.email, ignoreCase = true)
        }

        if (existeDuplicado) {
            // Si existe, lanzamos un error
            throw Exception("Ya existe un contacto con ese nombre o correo electrónico.")
        }

        dao.create(contacto)
    }

    override fun readByName(name: String): Contacto? {
        return dao.readByName(name)
    }

    override fun readAll(): List<Contacto> {
        return dao.readAll()
    }

    override fun update(contacto: Contacto) {
        dao.update(contacto)
    }

    override fun delete(contacto: Contacto) {
        dao.delete(contacto)
    }

    /**
     *  Listar los contactos y su fecha de cumpleaños ordenados a partir del día actual.
     */
    fun obtenerContactosPorCumpleanosProximo(): List<Contacto> {
        val today = LocalDate.now()

        return dao.readAll().sortedBy { contacto ->
            // Cambiamos el año del cumpleaños al año actual para comparar los diasw
            val cumpleEsteAno = contacto.birthDate.withYear(today.year)

            // Si el cumpleaños ya paso, calculamos los dias para el proximo año
            val proximoCumple = if (cumpleEsteAno.isBefore(today)) {
                cumpleEsteAno.plusYears(1)
            } else {
                cumpleEsteAno
            }

            // Devuelve los dias que faltan (orden de menor a mayor)
            ChronoUnit.DAYS.between(today, proximoCumple)
        }
    }

    /**
     * Listar todos los contactos ordenados alfabéticamente por nombre.
     */
    fun obtenerContactosOrdenAlfabetico(): List<Contacto> {
        return dao.readAll().sortedBy { it.name.lowercase() }
    }
}