package com.example.samapp.inicio

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.samapp.R
import com.example.samapp.historias.MascotaFoto
import com.example.samapp.navigation.NavActions

val TopBarBlue = Color(0xFF759FCB)
val ButtonBlue = Color(0xFF6CA0DC)
val TextGray = Color(0xFF9E9E9E)

@Composable
fun InicioScreen(
    rol: String = "ADOPTANTE",
    nav: NavActions = NavActions()
) {
    var historiaAbierta by remember { mutableStateOf<HistoriaFeliz?>(null) }

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
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "¡BIENVENID@!",
                fontFamily = FontFamily.Monospace,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = TextGray,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = ButtonBlue.copy(alpha = 0.12f)
            ) {
                Text(
                    text = "ROL: ${rol.replace("_", " ")}",
                    color = TopBarBlue,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Para interactuar con la aplicación, utiliza el menú de navegación de la parte inferior y esperamos que encuentres a tu mejor amigo. \uD83D\uDC36\uD83D\uDC31",
                fontFamily = FontFamily.Monospace,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Acceso a la carga de documentos del adoptante
            Button(
                onClick = nav.documentos,
                colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue),
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 0.dp),
                modifier = Modifier.height(40.dp)
            ) {
                Icon(Icons.Default.Description, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("MIS DOCUMENTOS", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "HISTORIAS FELICES",
                fontFamily = FontFamily.Monospace,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "Últimas adopciones. Toca una para conocer su historia.",
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp,
                color = TextGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                HISTORIAS_FELICES.chunked(2).forEach { fila ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        fila.forEach { historia ->
                            HistoriaFelizCard(
                                historia = historia,
                                modifier = Modifier.weight(1f),
                                onClick = { historiaAbierta = historia }
                            )
                        }
                        if (fila.size == 1) Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    historiaAbierta?.let { historia ->
        HistoriaFelizDialog(historia = historia, onClose = { historiaAbierta = null })
    }
}

@Composable
fun TopBarSAM(nav: NavActions? = null) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(TopBarBlue)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .statusBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_sam),
                contentDescription = "Logo SAM",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )
            Icon(
                Icons.Default.Info,
                contentDescription = "Información",
                tint = Color.White,
                modifier = Modifier
                    .size(32.dp)
                    .clickable { nav?.info() }
            )
        }

        Button(
            onClick = { nav?.donaciones() },
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 0.dp),
            modifier = Modifier.height(32.dp)
        ) {
            Text("DONA AQUI", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun HistoriaFelizCard(
    historia: HistoriaFeliz,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.BottomCenter
        ) {
            MascotaFoto(
                imagen = historia.imagen,
                descripcion = historia.nombre,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .background(Color.White, RoundedCornerShape(20.dp))
                    .border(2.dp, ButtonBlue, RoundedCornerShape(20.dp))
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "ADOPTADO",
                    color = ButtonBlue,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "${historia.nombre} · ${historia.especie}",
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = Color.Black
        )
        Text(
            text = historia.fecha,
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp,
            color = TextGray
        )
        Text(
            text = "Ver historia",
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            color = ButtonBlue
        )
    }
}

@Composable
private fun HistoriaFelizDialog(historia: HistoriaFeliz, onClose: () -> Unit) {
    AlertDialog(
        onDismissRequest = onClose,
        confirmButton = { TextButton(onClick = onClose) { Text("CERRAR") } },
        title = {
            Text(
                text = "${historia.nombre} · ${historia.especie}",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                MascotaFoto(
                    imagen = historia.imagen,
                    descripcion = historia.nombre,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Adopción: ${historia.fecha}",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = historia.historia,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp,
                    lineHeight = 21.sp
                )
            }
        }
    )
}

/** Barra inferior: cada ícono lleva su nombre para que sea fácil de identificar. */
@Composable
fun BottomBarSAM(nav: NavActions = NavActions()) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(TopBarBlue)
            .navigationBarsPadding()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BarraItem(Icons.Default.FavoriteBorder, "Donar", Modifier.weight(1f)) { nav.donaciones() }
        BarraItem(Icons.Default.DateRange, "Eventos", Modifier.weight(1f)) { nav.eventos() }
        BarraItem(Icons.Default.Home, "Inicio", Modifier.weight(1f)) { nav.inicio() }
        BarraItem(Icons.Default.Pets, "Adoptar", Modifier.weight(1f)) { nav.adopciones() }
        BarraItem(Icons.AutoMirrored.Filled.ExitToApp, "Salir", Modifier.weight(1f)) { nav.salir() }
    }
}

@Composable
private fun BarraItem(
    icono: ImageVector,
    texto: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icono,
            contentDescription = texto,
            tint = Color.White,
            modifier = Modifier.size(28.dp)
        )
        Text(
            text = texto,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
    }
}
