package com.example.samapp.model

/**
 * Datos del registro de usuario (pantalla única).
 * Los datos de vivienda, mascotas y responsabilidad se piden después,
 * en la pantalla de adopción.
 */
data class RegistroState(
    val nombreCompleto: String = "",
    val tipoDocumento: String = "",
    val numeroDocumento: String = "",
    val correo: String = "",
    val fechaNacimiento: String = "",
    val ciudad: String = "",
    val direccion: String = "",
    val contrasena: String = "",
    val confirmarContrasena: String = ""
)