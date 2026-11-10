package com.example.samapp.navigation

/**
 * Acciones de navegación compartidas por la barra superior, la barra inferior y las pantallas.
 */
data class NavActions(
    val inicio: () -> Unit = {},
    val adopciones: () -> Unit = {},
    val eventos: () -> Unit = {},
    val donaciones: () -> Unit = {},
    val documentos: () -> Unit = {},
    val chats: () -> Unit = {},
    val salir: () -> Unit = {},
    val info: () -> Unit = {}
)
