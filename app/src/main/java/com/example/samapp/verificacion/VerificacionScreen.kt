package com.example.samapp.verificacion

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val ButtonBlue = Color(0xFF6CA0DC)
val BorderGray = Color(0xFF9E9E9E)

@Composable
fun VerificacionScreen(
    onBack: () -> Unit,
    onVerificar: () -> Unit // <-- Parámetro agregado
) {
    // La declaración de la variable con sus importaciones de runtime.* activas
    var codigo by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            Text(
                text = "REVISA LA BANDEJA DE TU\nCORREO",
                fontFamily = FontFamily.Monospace,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Text(
                text = "Te hemos enviado un código de verificación\nal correo electrónico registrado para\nactivar tu cuenta",
                fontFamily = FontFamily.Monospace,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 40.dp)
            )

            // Campo de texto central
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(48.dp)
                    .border(1.dp, BorderGray)
                    .background(Color.White)
            ) {
                TextField(
                    value = codigo,
                    onValueChange = { codigo = it },
                    modifier = Modifier.fillMaxSize(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { /* Acción reenviar código */ },
                    colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text(
                        text = "REENVIAR CODIGO",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = onVerificar, // <-- AQUÍ USAMOS EL PARÁMETRO PARA ELIMINAR LA ADVERTENCIA
                    colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text(
                        text = "VERIFICAR",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f)) // Empuja el botón Atrás hacia el fondo

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.BottomStart
            ) {
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .width(150.dp)
                        .height(48.dp)
                ) {
                    Text(text = "ATRAS", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}