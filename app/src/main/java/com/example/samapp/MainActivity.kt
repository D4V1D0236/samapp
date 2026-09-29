package com.example.samapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

// Importaciones de las otras pantallas
import com.example.samapp.formulario.FormularioApp
import com.example.samapp.recuperacion.RecuperacionApp
import com.example.samapp.verificacion.VerificacionScreen
import com.example.samapp.inicio.InicioScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppNavigation()
            }
        }
    }
}

val CyanBackground = Color(0xFF6DE2FA)
val BlueCard = Color(0xFF759FCB)
val TextDark = Color(0xFF1B263B)

@Composable
fun AppNavigation() {
    var currentScreen by remember { mutableStateOf("splash") }

    LaunchedEffect(Unit) {
        delay(3.seconds)
        currentScreen = "login"
    }

    when (currentScreen) {
        "splash" -> SplashScreen()
        "login" -> LoginScreen(
            onNavigateToRegister = { currentScreen = "formulario" },
            onNavigateToRecovery = { currentScreen = "com/example/samapp/recuperacion" },
            onNavigateToInicio = { currentScreen = "inicio" }
        )
        "formulario" -> FormularioApp(
            onCancelForm = { currentScreen = "login" },
            onFinishForm = { currentScreen = "verificacion" }
        )
        "com/example/samapp/recuperacion" -> RecuperacionApp(
            onBackToLogin = { currentScreen = "login" }
        )
        "verificacion" -> VerificacionScreen(
            onBack = { currentScreen = "formulario" },
            onVerificar = { currentScreen = "login" }
        )
        "inicio" -> InicioScreen()
    }
}

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CyanBackground),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_sam),
            contentDescription = "Logo SAM",
            modifier = Modifier.size(200.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onNavigateToRecovery: () -> Unit,
    onNavigateToInicio: () -> Unit
) {
    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .align(Alignment.BottomCenter)
                .background(CyanBackground)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_sam),
                contentDescription = "Logo SAM",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Sistema de Adopción de Mascotas",
                fontSize = 20.sp,
                color = TextDark,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BlueCard)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Usuario",
                        color = TextDark,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    TextField(
                        value = usuario,
                        onValueChange = { usuario = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Contraseña",
                        color = TextDark,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    TextField(
                        value = contrasena,
                        onValueChange = { contrasena = it },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "¿Olvidaste tu contraseña?",
                        color = TextDark,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(bottom = 24.dp)
                            .clickable { onNavigateToRecovery() }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = onNavigateToInicio,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(
                                text = "INICIAR SESIÓN",
                                color = TextDark,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = onNavigateToRegister,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(
                                text = "REGISTRARSE",
                                color = TextDark,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}