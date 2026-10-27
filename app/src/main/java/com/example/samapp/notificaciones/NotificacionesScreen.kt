package com.example.samapp.notificaciones

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.samapp.inicio.TopBarSAM
import com.example.samapp.navigation.NavActions

@Composable
fun NotificacionesScreen(
    nav: NavActions
) {
    Scaffold(
        topBar = { TopBarSAM(nav) },
        bottomBar = { BottomBarSAM(nav) },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(32.dp))

            Text(
                text = "DONACIONES",
                fontFamily = FontFamily.Monospace,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.LightGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(32.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "LLAVE BRE-B\n@BBVA32169449417\nBANCO BBVA\nTITULAR SHAYRA MELO",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    lineHeight = 28.sp
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "BBVA",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF004481) // Color azul BBVA
                )

                Spacer(Modifier.height(32.dp))

                Text(
                    text = "Comparte tu QR",
                    fontSize = 12.sp,
                    color = Color.Black
                )

                Spacer(Modifier.height(8.dp))
            }

            Image(
                painter = painterResource(id = R.drawable.imagen_qr),
                contentDescription = "Código QR Donaciones",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .aspectRatio(1f)
            )

            Spacer(Modifier.height(40.dp))
        }
    }
}