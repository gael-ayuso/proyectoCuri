package models

interface ContactoDAO {
    fun create(contacto: Contacto)
    fun readByName(name: String): Contacto?
    fun readAll(): List<Contacto>
    fun update(contacto: Contacto)
    fun delete(contacto: Contacto)
}