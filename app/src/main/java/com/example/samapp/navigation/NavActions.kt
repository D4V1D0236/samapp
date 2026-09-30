package com.example.samapp.navigation

data class NavActions(
    val inicio: () -> Unit = {},
    val adopciones: () -> Unit = {},
    val eventos: () -> Unit = {},
    val notificaciones: () -> Unit = {},
    val chats: () -> Unit = {},
    val salir: () -> Unit = {}
)