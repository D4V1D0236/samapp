package com.example.samapp.info

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.samapp.R
import com.example.samapp.inicio.BottomBarSAM
import com.example.samapp.inicio.ButtonBlue
import com.example.samapp.inicio.TopBarSAM
import com.example.samapp.navigation.NavActions

private val AzulOscuro = Color(0xFF1B263B)

@Composable
fun InfoScreen(
    nav: NavActions,
    onRegresar: () -> Unit,
    version: String = "v1.0"
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
            // Botón regresar (mismo estilo que en el detalle de la mascota)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 16.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Button(
                    onClick = onRegresar,
                    colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                    modifier = Modifier.height(28.dp)
                ) {
                    Text("Regresar", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = R.drawable.logo_sam),
                contentDescription = "Logo SAM",
                modifier = Modifier.size(140.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "SAMAPP",
                fontFamily = FontFamily.Monospace,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
            Text(
                text = "Sistema de Adopción de Mascotas",
                fontFamily = FontFamily.Monospace,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "SAM es una aplicación que facilita la adopción de mascotas rescatadas. " +
                            "Reúne en un solo lugar a los perros y gatos que buscan un hogar, información " +
                            "sobre su cuidado responsable y a las personas dispuestas a darles una segunda " +
                            "oportunidad. Nuestro propósito es promover una adopción ética, consciente y " +
                            "para toda la vida.",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Justify
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "¿QUÉ PUEDES HACER EN SAM?",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulOscuro
                )
                Spacer(modifier = Modifier.height(8.dp))
                listOf(
                    "Conocer a los perros y gatos disponibles para adopción.",
                    "Enterarte de jornadas de esterilización, vacunación y mucho más.",
                    "Conocer las últimas adopciones que han cambiado la vida de un peludito.",
                    "Escribir a los rescatistas y apoyar con tu donación."
                ).forEach { item ->
                    Row(modifier = Modifier.padding(vertical = 3.dp)) {
                        Text("\u2022", fontFamily = FontFamily.Monospace, fontSize = 14.sp, color = Color.Black)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = item,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            color = Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "SAM APP S.A.S \u00B7 Bogotá D.C., Colombia",
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                color = Color.Gray
            )
            Text(
                text = version,
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}