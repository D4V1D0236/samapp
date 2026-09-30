package com.example.samapp.eventos

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.samapp.R
import com.example.samapp.inicio.BottomBarSAM
import com.example.samapp.inicio.ChatBurbuja
import com.example.samapp.inicio.TopBarSAM
import com.example.samapp.navigation.NavActions

private val eventosFlyers = listOf(
    R.drawable.evento_1,
    R.drawable.evento_2
)

@Composable
fun EventosScreen(
    nav: NavActions = NavActions(),
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = { TopBarSAM() },
        bottomBar = { BottomBarSAM(nav) },
        floatingActionButton = { ChatBurbuja(onClick = nav.chats) },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "EVENTOS",
                fontFamily = FontFamily.Monospace,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "PRÓXIMOS EVENTOS",
                fontFamily = FontFamily.Monospace,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.widthIn(max = 600.dp)
            ) {
                eventosFlyers.forEach { EventoFlyer(it) }
            }
        }
    }
}

@Composable
private fun EventoFlyer(@DrawableRes imagen: Int) {
    Image(
        painter = painterResource(id = imagen),
        contentDescription = "Flyer de evento",
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
    )
}