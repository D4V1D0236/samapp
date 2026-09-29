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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.samapp.firebase.FirebaseRepository

val RecoveryTextNavy = Color(0xFF1B263B)
val RecoveryButtonBlue = Color(0xFF6CA0DC)
val RecoveryBorderGray = Color(0xFF9E9E9E)

@Composable
fun RecuperacionApp(onBackToLogin: () -> Unit) {
    var email by rememberSaveable { mutableStateOf("") }
    var sent by rememberSaveable { mutableStateOf(false) }
    var error by rememberSaveable { mutableStateOf<String?>(null) }
    val repository = remember { FirebaseRepository() }

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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            if (!sent) {
                Text(
                    text = "RECUPERA TU CONTRASEÑA",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                Text(
                    text = "Introduce tu correo electrónico registrado y\nte enviaremos un enlace para restablecer\ntu contraseña.",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                RecuperacionTextField("Correo electrónico", email) { email = it }

                error?.let {
                    Text(
                        text = it,
                        color = Color(0xFFE63946),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                Button(
                    onClick = {
                        error = null
                        repository.enviarRestablecimientoPassword(
                            email = email,
                            onSuccess = { sent = true },
                            onError = { error = it }
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RecoveryButtonBlue),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text("ENVIAR CORREO", fontWeight = FontWeight.Bold)
                }
            } else {
                Text(
                    text = "REVISA TU CORREO",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                Text(
                    text = "Enviamos un enlace de recuperación a:\n$email\n\nAbre el enlace desde tu correo y establece\nuna nueva contraseña.",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = onBackToLogin,
                colors = ButtonDefaults.buttonColors(containerColor = RecoveryButtonBlue),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("VOLVER AL INICIO DE SESIÓN", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun RecuperacionTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            color = RecoveryTextNavy,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(40.dp)
                .border(1.dp, RecoveryBorderGray)
                .background(Color.White)
        ) {
            TextField(
                value = value,
                onValueChange = onValueChange,
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
    }
}
