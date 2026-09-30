package com.example.samapp.historias

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.samapp.R
import com.example.samapp.inicio.TopBarSAM
import com.example.samapp.inicio.BottomBarSAM
import com.example.samapp.inicio.ChatBurbuja

// Estructura para guardar los datos de cada animal
data class MascotaInfo(val imageRes: Int, val sexo: String, val edad: String, val peso: String, val raza: String, val salud: String, val vacunas: String)

@Composable
fun DetalleMascotaScreen(
    nombreMascota: String,
    onNavigateToInicio: () -> Unit,
    onNavigateToHistorias: () -> Unit
) {
    // Aquí asignamos los datos exactos que pediste para cada animal
    val info = when (nombreMascota) {
        "Tigre" -> MascotaInfo(R.drawable.animal5, "Macho", "3 años", "5 Kg", "Criollo", "Excelente", "Al día")
        "Oreo" -> MascotaInfo(R.drawable.animal6, "Hembra", "1 año", "2.5 Kg", "Criollo", "Excelente", "Al día")
        "Mishi" -> MascotaInfo(R.drawable.animal7, "Hembra", "2 meses", "2 Kg", "Criollo", "Excelente", "Al día")
        "Garfield" -> MascotaInfo(R.drawable.animal8, "Macho", "3 meses", "3 Kg", "Criollo", "Excelente!", "Al día")
        // Garfield es el mismo de tu imagen (Simba)
        else -> MascotaInfo(R.drawable.animal8, "Macho", "3 meses", "3 Kg", "Criollo", "Excelente!", "Al día")
    }

    Scaffold(
        topBar = { TopBarSAM() },
        bottomBar = {
            BottomBarSAM(
                onNavigateToInicio = onNavigateToInicio,
                onNavigateToHistorias = onNavigateToHistorias
            )
        },
        floatingActionButton = { ChatBurbuja() },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Imagen del animal
            Image(
                painter = painterResource(id = info.imageRes),
                contentDescription = nombreMascota,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth(0.7f) // Hace que ocupe el 70% del ancho, tal como en tu foto
                    .aspectRatio(0.8f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Nombre
            Text(
                text = nombreMascota,
                fontFamily = FontFamily.Monospace,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Detalles
            Text(
                text = "Sexo: ${info.sexo}\nEdad: ${info.edad}\nPeso: ${info.peso}\nRaza: ${info.raza}\nEstado de Salud: ${info.salud}\nVacunas: ${info.vacunas}",
                fontFamily = FontFamily.Monospace,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                color = Color.Black,
                lineHeight = 28.sp // Separa las líneas un poco para que sea fácil de leer
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}