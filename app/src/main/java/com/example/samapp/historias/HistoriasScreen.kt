package com.example.samapp.historias

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.samapp.R
import com.example.samapp.navigation.NavActions


// Importamos los componentes que ya creaste en inicio
import com.example.samapp.inicio.TopBarSAM
import com.example.samapp.inicio.BottomBarSAM
import com.example.samapp.inicio.ChatBurbuja
import com.example.samapp.inicio.ButtonBlue

@Composable
fun HistoriasScreen(
    nav: NavActions,
    onNavigateToDetalle: (String) -> Unit
) {
    Scaffold(
        topBar = { TopBarSAM() },
        bottomBar = { BottomBarSAM(nav) },
        floatingActionButton = { ChatBurbuja(onClick = nav.chats) },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Sección: Ordenar por
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Ordenar por",
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    fontSize = 18.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DropdownSimulado(texto = "Gato", modifier = Modifier.weight(1f))
                    DropdownSimulado(texto = "Todas las edades", modifier = Modifier.weight(1.5f))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Fila 1 de mascotas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 2. Conectamos cada tarjeta para que envíe el nombre al hacer clic
                PetCard(imageRes = R.drawable.animal5, nombre = "Tigre", edad = "3 años", modifier = Modifier.weight(1f)) { onNavigateToDetalle("Tigre") }
                PetCard(imageRes = R.drawable.animal6, nombre = "Oreo", edad = "1 año", modifier = Modifier.weight(1f)) { onNavigateToDetalle("Oreo") }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Fila 2 de mascotas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PetCard(imageRes = R.drawable.animal7, nombre = "Mishi", edad = "2 meses", modifier = Modifier.weight(1f)) { onNavigateToDetalle("Mishi") }
                PetCard(imageRes = R.drawable.animal8, nombre = "Garfield", edad = "3 meses", modifier = Modifier.weight(1f)) { onNavigateToDetalle("Garfield") }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun DropdownSimulado(texto: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .height(40.dp)
            .border(1.dp, Color.Gray)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = texto, color = Color.Gray, fontSize = 14.sp)
        Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color.Gray)
    }
}

// 3. Modificamos PetCard para que reciba y ejecute la acción del clic
@Composable
fun PetCard(imageRes: Int, nombre: String, edad: String, modifier: Modifier = Modifier, onClickDetalles: () -> Unit) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = nombre,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.75f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "$nombre ($edad)",
            fontFamily = FontFamily.Monospace,
            fontSize = 14.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onClickDetalles, // 4. El botón ahora ejecuta la acción en lugar de estar vacío
            colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.height(36.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
        ) {
            Text("DETALLES", fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 12.sp)
        }
    }
}