package com.example.samapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.samapp.firebase.FirebaseRepository
import com.example.samapp.formulario.FormularioApp
import com.example.samapp.inicio.InicioScreen
import com.example.samapp.recuperacion.RecuperacionApp
import com.example.samapp.verificacion.VerificacionScreen
import com.example.samapp.model.RegistroState
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds
import android.content.res.Configuration
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalConfiguration
import com.example.samapp.historias.HistoriasScreen
import com.example.samapp.chats.ChatsScreen
import com.example.samapp.eventos.EventosScreen
import com.example.samapp.navigation.NavActions
import com.example.samapp.notificaciones.NotificacionesScreen
import com.example.samapp.sesion.CerrarSesionScreen

// 1. IMPORTANTE: Agregamos la importación de la pantalla de Detalles
import com.example.samapp.historias.DetalleMascotaScreen

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
    var currentScreen by rememberSaveable { mutableStateOf("splash") }
    var correoPendiente by rememberSaveable { mutableStateOf("") }
    var rolActual by rememberSaveable { mutableStateOf("ADOPTANTE") }

    // 2. IMPORTANTE: Variable para recordar qué mascota seleccionó el usuario
    var mascotaSeleccionada by rememberSaveable { mutableStateOf("") }
    var pantallaAntesDeSalir by rememberSaveable { mutableStateOf("inicio") }

    val repository = remember { FirebaseRepository() }
    val context = LocalContext.current
    val showMessage: (String) -> Unit = { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
    var loginError by rememberSaveable { mutableStateOf<String?>(null) }
    val nav = NavActions(
        inicio = { currentScreen = "inicio" },
        adopciones = { currentScreen = "historias" },
        eventos = { currentScreen = "eventos" },
        notificaciones = { currentScreen = "notificaciones" },
        chats = { currentScreen = "chats" },
        salir = {
            if (currentScreen != "salir") pantallaAntesDeSalir = currentScreen
            currentScreen = "salir"
        }
    )

    LaunchedEffect(Unit) {
        if (currentScreen == "splash") {
            delay(3.seconds)
            if (currentScreen == "splash") currentScreen = "login"
        }
    }

    when (currentScreen) {
        "splash" -> SplashScreen()

        "login" -> LoginScreen(
            errorMessage = loginError,
            onEditing = { loginError = null },
            onNavigateToRegister = { loginError = null; currentScreen = "formulario" },
            onNavigateToRecovery = { loginError = null; currentScreen = "recuperacion" },
            onLogin = { usuario, contrasena ->
                loginError = null
                repository.iniciarSesion(
                    email = usuario,
                    password = contrasena,
                    onSuccess = { rol ->
                        rolActual = rol
                        loginError = null
                        currentScreen = "inicio"
                    },
                    onError = { loginError = it }
                )
            }
        )

        "formulario" -> FormularioApp(
            onCancelForm = { currentScreen = "login" },
            onFinishForm = { registro: RegistroState ->
                correoPendiente = registro.correo
                repository.registrarUsuario(
                    registro = registro,
                    onSuccess = {
                        currentScreen = "verificacion"
                        showMessage("Cuenta creada. Revisa tu correo para verificarla.")
                    },
                    onError = { showMessage(it) }
                )
            }
        )

        "recuperacion" -> RecuperacionApp(
            onBackToLogin = { currentScreen = "login" }
        )

        "verificacion" -> VerificacionScreen(
            correo = correoPendiente,
            onBack = {
                repository.cerrarSesion()
                currentScreen = "login"
            },
            onReenviar = {
                repository.reenviarVerificacion(
                    onSuccess = { showMessage("Correo de verificación reenviado.") },
                    onError = { showMessage(it) }
                )
            },
            onVerificar = {
                repository.comprobarVerificacion(
                    onVerified = {
                        repository.cerrarSesion()
                        showMessage("Correo verificado. Ya puedes iniciar sesión.")
                        currentScreen = "login"
                    },
                    onNotVerified = {
                        showMessage("Todavía no aparece la verificación. Abre el enlace recibido y vuelve a comprobar.")
                    },
                    onError = { showMessage(it) }
                )
            }
        )

        "inicio" -> InicioScreen(
            rol = rolActual,
            nav = nav
        )

        "historias" -> HistoriasScreen(
            nav = nav,
            onNavigateToDetalle = { nombre ->
                mascotaSeleccionada = nombre
                currentScreen = "detalle"
            }
        )

        "detalle" -> DetalleMascotaScreen(
            nombreMascota = mascotaSeleccionada,
            nav = nav
        )

        "eventos" -> EventosScreen(nav = nav)

        "notificaciones" -> NotificacionesScreen(nav = nav)

        "chats" -> ChatsScreen(nav = nav)

        "salir" -> CerrarSesionScreen(
            onCerrarSesion = {
                repository.cerrarSesion()
                rolActual = "ADOPTANTE"
                mascotaSeleccionada = ""
                pantallaAntesDeSalir = "inicio"
                loginError = null
                currentScreen = "login"
            },
            onContinuar = { currentScreen = pantallaAntesDeSalir }
        )
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
    errorMessage: String?,
    onEditing: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToRecovery: () -> Unit,
    onLogin: (String, String) -> Unit
) {
    var usuario by rememberSaveable { mutableStateOf("") }
    var contrasena by rememberSaveable { mutableStateOf("") }
    val horizontal = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

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
                .verticalScroll(rememberScrollState())
                .padding(top = if (horizontal) 16.dp else 60.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_sam),
                contentDescription = "Logo SAM",
                modifier = Modifier.size(if (horizontal) 90.dp else 120.dp)
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
                    .padding(horizontal = 32.dp)
                    .widthIn(max = 480.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BlueCard)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Correo electrónico",
                        color = TextDark,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    TextField(
                        value = usuario,
                        onValueChange = { usuario = it; onEditing() },
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
                        onValueChange = { contrasena = it; onEditing() },
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

                    errorMessage?.let { msg ->
                        Text(
                            text = msg,
                            color = Color(0xFFB00020),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                                .background(Color.White, RoundedCornerShape(8.dp))
                                .padding(10.dp)
                        )
                    }

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
                            onClick = { onLogin(usuario, contrasena) },
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