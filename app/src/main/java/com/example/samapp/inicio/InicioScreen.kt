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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.samapp.R
import com.example.samapp.navigation.NavActions

val TopBarBlue = Color(0xFF759FCB)
val ButtonBlue = Color(0xFF6CA0DC)
val TextGray = Color(0xFF9E9E9E)

@Composable
fun InicioScreen(
    rol: String = "ADOPTANTE",
    nav: NavActions = NavActions()
) {
    Scaffold(
        topBar = { TopBarSAM(nav) }, // Aquí pasamos el nav a la barra superior
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
                text = "Ingresa al menú de ADOPTA AQUÍ en las tres\nlíneas para encontrar a tu amigo ideal de\ncuatro patas \uD83D\uDC36\uD83D\uDC31",
                fontFamily = FontFamily.Monospace,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "ÚLTIMAS ADOPCIONES",
                fontFamily = FontFamily.Monospace,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AdoptionCard(imageRes = R.drawable.animal1, modifier = Modifier.weight(1f))
                    AdoptionCard(imageRes = R.drawable.animal2, modifier = Modifier.weight(1f))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AdoptionCard(imageRes = R.drawable.animal3, modifier = Modifier.weight(1f))
                    AdoptionCard(imageRes = R.drawable.animal4, modifier = Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// Aquí actualizamos la función para recibir el nav
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
            // Aquí le asignamos la acción para ir a donaciones (pantalla de notificaciones)
            onClick = { nav?.notificaciones() },
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
fun AdoptionCard(imageRes: Int, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.BottomCenter
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "Mascota Adoptada",
            contentScale = ContentScale.Crop,
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
}

@Composable
fun BottomBarSAM(nav: NavActions = NavActions()) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(TopBarBlue)
            .navigationBarsPadding()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.Notifications,
            contentDescription = "Notificaciones", // Ahora nos lleva a Donaciones
            tint = Color.White,
            modifier = Modifier.size(32.dp).clickable { nav.notificaciones() }
        )
        Icon(
            Icons.Default.DateRange,
            contentDescription = "Eventos",
            tint = Color.White,
            modifier = Modifier.size(32.dp).clickable { nav.eventos() }
        )
        Icon(
            Icons.Default.Home,
            contentDescription = "Inicio",
            tint = Color.White,
            modifier = Modifier.size(40.dp).clickable { nav.inicio() }
        )
        Icon(
            Icons.Default.Edit,
            contentDescription = "Adopciones",
            tint = Color.White,
            modifier = Modifier.size(32.dp).clickable { nav.adopciones() }
        )
        Icon(
            Icons.Default.ExitToApp,
            contentDescription = "Salir",
            tint = Color.White,
            modifier = Modifier.size(32.dp).clickable { nav.salir() }
        )
    }
}