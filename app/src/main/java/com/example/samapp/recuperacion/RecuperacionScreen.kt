package com.example.samapp.recuperacion

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val TextNavy = Color(0xFF1B263B)
val ButtonBlue = Color(0xFF6CA0DC)
val AsteriskRed = Color(0xFFE63946)
val BorderGray = Color(0xFF9E9E9E)

@Composable
fun RecuperacionApp(onBackToLogin: () -> Unit) {
    var currentStep by remember { mutableIntStateOf(1) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            when (currentStep) {
                1 -> RecuperaPasswordScreen(
                    onVerificar = { currentStep = 2 },
                    onBack = onBackToLogin
                )
                2 -> NuevaPasswordScreen(
                    onCambiarPassword = onBackToLogin, // Al terminar, regresa al login
                    onBack = { currentStep = 1 }
                )
            }
        }
    }
}

@Composable
fun RecuperaPasswordScreen(onVerificar: () -> Unit, onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "RECUPERA TU CONTRASEÑA",
            fontFamily = FontFamily.Monospace,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Text(
            text = "Introduce tu correo electrónico registrado y\nte enviaremos un código de verificación\napara recuperar tu contraseña.\nCambia tu contraseña y vuelve a iniciar\nsesión.",
            fontFamily = FontFamily.Monospace,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        RecuperacionTextField("Correo electrónico", required = false)

        Spacer(modifier = Modifier.height(32.dp))

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
                Text(text = "REENVIAR CODIGO", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = onVerificar,
                colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text(text = "VERIFICAR", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.weight(1f)) // Empuja el botón de atrás hacia abajo

        Box(modifier = Modifier.fillMaxWidth().padding(top = 64.dp), contentAlignment = Alignment.BottomStart) {
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

@Composable
fun NuevaPasswordScreen(onCambiarPassword: () -> Unit, onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "CREA UNA NUEVA CONTRASEÑA",
            fontFamily = FontFamily.Monospace,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        RecuperacionTextField("Nueva Contraseña")
        RecuperacionTextField("Confirma tu contraseña")
        RecuperacionTextField("Código de Verificación")

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "RECUERDA: Una contraseña segura debe tener\nal menos 8-12 caracteres, combinar\nmayúsculas, minúsculas, números y símbolos,\ny evitar información fácil de adivinar como\nnombres, fechas de nacimiento o palabras\ncomunes. Además, se recomienda usar una\ncontraseña diferente para cada cuenta y no\ncompartirla con otras personas.",
            fontFamily = FontFamily.Monospace,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .weight(1.3f)
                    .height(55.dp)
            ) {
                Text(text = "ATRAS", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.width(15.dp))

            Button(
                onClick = onCambiarPassword,
                colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .weight(1.3f)
                    .height(54.dp)
            ) {
                Text(text = "CAMBIAR CONTRASEÑA", fontWeight = FontWeight.Bold, fontSize = 11.sp, textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
fun RecuperacionTextField(label: String, required: Boolean = true) {
    var value by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = buildAnnotatedString {
                append(label)
                if (required) {
                    withStyle(style = SpanStyle(color = AsteriskRed, fontSize = 18.sp)) {
                        append(" *")
                    }
                }
            },
            color = TextNavy,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(40.dp)
                .border(1.dp, BorderGray)
                .background(Color.White)
        ) {
            TextField(
                value = value,
                onValueChange = { value = it },
                modifier = Modifier.fillMaxSize(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }
    }
}