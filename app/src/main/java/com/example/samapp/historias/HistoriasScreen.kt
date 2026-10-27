package com.example.samapp.historias

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.samapp.R
import com.example.samapp.navigation.NavActions

import com.example.samapp.inicio.TopBarSAM
import com.example.samapp.inicio.BottomBarSAM
import com.example.samapp.inicio.ButtonBlue

@Composable
fun HistoriasScreen(
    nav: NavActions,
    onNavigateToDetalle: (String) -> Unit
) {
    Scaffold(
        topBar = { TopBarSAM() },
        bottomBar = { BottomBarSAM(nav) },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Mascotas en adopción",
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                fontSize = 24.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PetCard(imageRes = R.drawable.animal5, nombre = "Tigre", edad = "3 años", modifier = Modifier.weight(1f)) { onNavigateToDetalle("Tigre") }
                PetCard(imageRes = R.drawable.animal6, nombre = "Oreo", edad = "1 año", modifier = Modifier.weight(1f)) { onNavigateToDetalle("Oreo") }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PetCard(imageRes = R.drawable.animal7, nombre = "Mishi", edad = "2 meses", modifier = Modifier.weight(1f)) { onNavigateToDetalle("Mishi") }
                PetCard(imageRes = R.drawable.animal8, nombre = "Garfield", edad = "3 meses", modifier = Modifier.weight(1f)) { onNavigateToDetalle("Garfield") }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun PetCard(imageRes: Int, nombre: String, edad: String, modifier: Modifier = Modifier, onClickDetalles: () -> Unit) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = nombre,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.75f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "$nombre ($edad)",
            fontFamily = FontFamily.Monospace,
            fontSize = 14.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onClickDetalles,
            colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.height(36.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
        ) {
            Text("DETALLES", fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 12.sp)
        }
    }
}