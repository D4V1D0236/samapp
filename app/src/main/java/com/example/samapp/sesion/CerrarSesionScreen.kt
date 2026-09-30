package com.example.samapp.sesion

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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

private val FondoAzul = Color(0xFF6CA0D0)
private val RojoCerrar = Color(0xFFFF2D2D)

@Composable
fun CerrarSesionScreen(
    onCerrarSesion: () -> Unit,
    onContinuar: () -> Unit,
    version: String = "v1.0"
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoAzul)
            .systemBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_sam),
                contentDescription = "Logo SAM",
                modifier = Modifier.size(180.dp)
            )

            Spacer(Modifier.height(48.dp))

            Text(
                text = "¿ESTÁS SEGURO DE CERRAR SESIÓN?",
                fontFamily = FontFamily.Monospace,
                fontSize = 24.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(56.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
            ) {
                Button(
                    onClick = onCerrarSesion,
                    colors = ButtonDefaults.buttonColors(containerColor = RojoCerrar),
                    shape = RoundedCornerShape(28.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        "CERRAR SESIÓN",
                        color = Color.Black,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        maxLines = 1
                    )
                }
                Button(
                    onClick = onContinuar,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFAFCFB)),
                    shape = RoundedCornerShape(28.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        "NO, CONTINUAR",
                        color = Color.Black,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        maxLines = 1
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("SAMAPP", color = Color.White, fontFamily = FontFamily.Monospace, fontSize = 16.sp)
            Text("Términos y Condiciones", color = Color.White, fontFamily = FontFamily.Monospace, fontSize = 11.sp)
            Text(version, color = Color.White, fontFamily = FontFamily.Monospace, fontSize = 11.sp)
        }
    }
}