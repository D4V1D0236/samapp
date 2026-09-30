package com.example.samapp.notificaciones

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.samapp.inicio.BottomBarSAM
import com.example.samapp.inicio.ChatBurbuja
import com.example.samapp.inicio.TopBarSAM
import com.example.samapp.navigation.NavActions

private val Divisor = Color(0xFFD5DBD5)

enum class TipoNotificacion { ADOPCION, EVENTO }

data class Notificacion(
    val id: String,
    val texto: String,
    val tipo: TipoNotificacion
)

private val notificacionesDeEjemplo = listOf(
    Notificacion("1", "NUEVAS OPCIONES DE ADOPCIÓN! \uD83C\uDFE0", TipoNotificacion.ADOPCION),
    Notificacion("2", "NUEVO EVENTO CERCA A TU ZONA! \uD83D\uDC3E\uD83D\uDC89", TipoNotificacion.EVENTO)
)

@Composable
fun NotificacionesScreen(
    nav: NavActions,
    notificaciones: List<Notificacion> = notificacionesDeEjemplo
) {
    Scaffold(
        topBar = { TopBarSAM() },
        bottomBar = { BottomBarSAM(nav) },
        floatingActionButton = { ChatBurbuja(onClick = nav.chats) },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))

            Text(
                text = "NOTIFICACIONES",
                fontFamily = FontFamily.Monospace,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                HorizontalDivider(thickness = 3.dp, color = Divisor)
                notificaciones.forEach { notificacion ->
                    NotificacionItem(
                        notificacion = notificacion,
                        onClick = {
                            when (notificacion.tipo) {
                                TipoNotificacion.ADOPCION -> nav.adopciones()
                                TipoNotificacion.EVENTO -> nav.eventos()
                            }
                        }
                    )
                    HorizontalDivider(thickness = 3.dp, color = Divisor)
                }
            }

            Spacer(Modifier.height(32.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(3) {
                    Box(Modifier.size(8.dp).background(Color.LightGray, CircleShape))
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun NotificacionItem(notificacion: Notificacion, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 28.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = notificacion.texto,
            fontFamily = FontFamily.Monospace,
            fontSize = 18.sp,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
    }
}