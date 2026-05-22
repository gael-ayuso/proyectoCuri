package models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// Sirve para que se adapte y sepa como serializar un LocalDate
object LocalDateSerializer : KSerializer<LocalDate> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: LocalDate) {
        // Al guardar: Convierte la fecha a texto ( ejemplo"2026-05-14")
        encoder.encodeString(value.format(DateTimeFormatter.ISO_LOCAL_DATE))
    }
    override fun deserialize(decoder: Decoder): LocalDate {
        // Al leer: Toma el texto del JSON y lo regresa a un objeto LocalDate
        return LocalDate.parse(decoder.decodeString(), DateTimeFormatter.ISO_LOCAL_DATE)
    }
}

@Serializable
data class Contacto(
    val name: String,

    @Serializable(with = LocalDateSerializer::class) // usa el serializador de arriba
    val birthDate: LocalDate,

    val phoneNumber: String, // Lo cambiamos a String

    val email: String ) {
    init {
        require(Regex("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+\$").matches(email)) {
            "El formato del email no es válido"
        }
    }
}