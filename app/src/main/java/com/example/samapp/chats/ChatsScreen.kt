package com.example.samapp.chats

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.samapp.inicio.BottomBarSAM
import com.example.samapp.inicio.ChatBurbuja
import com.example.samapp.inicio.TopBarSAM
import com.example.samapp.navigation.NavActions

private val CheckLeido = Color(0xFF6DE2FA)
private val CheckEnviado = Color(0xFFB0B7B0)
private val Divisor = Color(0xFFD5DBD5)

data class ChatResumen(
    val id: String,
    val nombre: String,
    val ultimoMensaje: String,
    val leido: Boolean
)

private val chatsDeEjemplo = listOf(
    ChatResumen("1", "VICENTE", "Quisiera saber sobre Valent...", leido = false),
    ChatResumen("2", "PATITAS UNIDAS \uD83C\uDFD8\uFE0F", "Como es Garfield con los niños?", leido = true)
)

@Composable
fun ChatsScreen(
    nav: NavActions,
    chats: List<ChatResumen> = chatsDeEjemplo,
    onAbrirChat: (ChatResumen) -> Unit = {}
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
                text = "CHATS",
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
                chats.forEach { chat ->
                    ChatItem(chat = chat, onClick = { onAbrirChat(chat) })
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
private fun ChatItem(chat: ChatResumen, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 4.dp)
    ) {
        Text(
            text = chat.nombre,
            fontFamily = FontFamily.Monospace,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(Modifier.height(6.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = if (chat.leido) Icons.Default.DoneAll else Icons.Default.Check,
                contentDescription = if (chat.leido) "Leído" else "Enviado",
                tint = if (chat.leido) CheckLeido else CheckEnviado,
                modifier = Modifier.size(36.dp)
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = chat.ultimoMensaje,
                fontFamily = FontFamily.Monospace,
                fontSize = 16.sp,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}