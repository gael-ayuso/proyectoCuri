package models

import java.time.LocalDate

//Al ser un data class se "generan"
// en automatico los setters y getters
data class Contacto(
    val name: String,
    val birthDate: LocalDate,
    val phoneNumber: String,
    val email: String
) {
    init {
        // El bloque init se ejecuta al crear la instancia. Aquí va tu validación.
        if (!Regex("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$").matches(email)) {
            throw IllegalArgumentException("Email no valido")
        }
    }
}