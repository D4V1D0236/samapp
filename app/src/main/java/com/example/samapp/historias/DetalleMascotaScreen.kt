package com.example.samapp.historias

import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.samapp.R
import com.example.samapp.inicio.TopBarSAM
import com.example.samapp.inicio.BottomBarSAM
import com.example.samapp.navigation.NavActions
import com.example.samapp.inicio.ButtonBlue

data class MascotaInfo(val imageRes: Int, val sexo: String, val edad: String, val peso: String, val raza: String, val salud: String, val vacunas: String)

@Composable
fun DetalleMascotaScreen(
    nombreMascota: String,
    nav: NavActions
) {
    val context = LocalContext.current

    // Recuerda cambiar esto por tu número real
    val numeroWhatsApp = "573216944941"

    val info = when (nombreMascota) {
        "Tigre" -> MascotaInfo(R.drawable.animal5, "Macho", "3 años", "5 Kg", "Criollo", "Excelente", "Al día")
        "Oreo" -> MascotaInfo(R.drawable.animal6, "Hembra", "1 año", "2.5 Kg", "Criollo", "Excelente", "Al día")
        "Mishi" -> MascotaInfo(R.drawable.animal7, "Hembra", "2 meses", "2 Kg", "Criollo", "Excelente", "Al día")
        "Garfield" -> MascotaInfo(R.drawable.animal8, "Macho", "3 meses", "3 Kg", "Criollo", "Excelente!", "Al día")
        else -> MascotaInfo(R.drawable.animal8, "Macho", "3 meses", "3 Kg", "Criollo", "Excelente!", "Al día")
    }

    Scaffold(
        topBar = { TopBarSAM() },
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 16.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Button(
                    // AQUÍ ESTÁ EL CAMBIO: Ahora llama a nav.adopciones()
                    onClick = { nav.adopciones() },
                    colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                    modifier = Modifier.height(28.dp)
                ) {
                    Text("Regresar", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .aspectRatio(0.8f),
                contentAlignment = Alignment.BottomCenter
            ) {
                Image(
                    painter = painterResource(id = info.imageRes),
                    contentDescription = nombreMascota,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Button(
                    onClick = {
                        val mensaje = "Hola, vengo de SAM APP y estoy interesado en adoptar a $nombreMascota."
                        val url = "https://wa.me/$numeroWhatsApp?text=${Uri.encode(mensaje)}"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .offset(y = 20.dp)
                        .fillMaxWidth(0.8f)
                        .height(48.dp)
                ) {
                    Text("ADOPTAR", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = nombreMascota,
                fontFamily = FontFamily.Monospace,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Sexo: ${info.sexo}\nEdad: ${info.edad}\nPeso: ${info.peso}\nRaza: ${info.raza}\nEstado de Salud: ${info.salud}\nVacunas: ${info.vacunas}",
                fontFamily = FontFamily.Monospace,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                color = Color.Black,
                lineHeight = 28.sp
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}