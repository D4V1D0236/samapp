package com.example.samapp.model

data class RegistroState(
    val nombreCompleto: String = "",
    val tipoDocumento: String = "",
    val numeroDocumento: String = "",
    val correo: String = "",
    val fechaNacimiento: String = "",
    val ciudad: String = "",
    val direccion: String = "",
    val contrasena: String = "",
    val confirmarContrasena: String = "",
    val tipoVivienda: String = "",
    val personasEnCasa: String = "",
    val hayNinos: String = "",
    val tieneMascotas: String = "",
    val tieneZonaVerde: String = "",
    val edadesNinos: String = "",
    val cantidadMascotas: String = "",
    val tipoMascotas: String = "",
    val mascotasVacunadasEsterilizadas: String = "",
    val motivoAdopcion: String = "",
    val planesMudanzaViaje: String = "",
    val responsableCuidado: String = "",
    val participacion: String = ""
) {
    val rolSolicitado: String
        get() = when (participacion) {
            "Hogar de Paso" -> "HOGAR_DE_PASO"
            "Ambas" -> "HOGAR_DE_PASO"
            else -> "ADOPTANTE"
        }
}
