package com.example.samapp.historias

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.samapp.navigation.NavActions

import com.example.samapp.inicio.TopBarSAM
import com.example.samapp.inicio.BottomBarSAM
import com.example.samapp.inicio.ButtonBlue

private val AzulOscuro = Color(0xFF1B263B)

@Composable
fun HistoriasScreen(
    nav: NavActions,
    tipoSeleccionado: TipoMascota,
    onTipoChange: (TipoMascota) -> Unit,
    onNavigateToDetalle: (String) -> Unit
) {
    val mascotas = remember(tipoSeleccionado) { MASCOTAS.filter { it.tipo == tipoSeleccionado } }

    Scaffold(
        topBar = { TopBarSAM(nav) },
        bottomBar = { BottomBarSAM(nav) },
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
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Mascotas en adopción",
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                fontSize = 24.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Filtrar por",
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            FiltroMascotas(seleccionado = tipoSeleccionado, onSeleccion = onTipoChange)

            Spacer(modifier = Modifier.height(24.dp))

            // Tarjetas de 2 en 2
            mascotas.chunked(2).forEach { fila ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    fila.forEach { mascota ->
                        PetCard(
                            mascota = mascota,
                            modifier = Modifier.weight(1f)
                        ) { onNavigateToDetalle(mascota.nombre) }
                    }
                    if (fila.size == 1) Spacer(modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun FiltroMascotas(
    seleccionado: TipoMascota,
    onSeleccion: (TipoMascota) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TipoMascota.values().forEach { tipo ->
            val activo = tipo == seleccionado
            val cantidad = MASCOTAS.count { it.tipo == tipo }

            Button(
                onClick = { onSeleccion(tipo) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (activo) ButtonBlue else Color.White
                ),
                border = BorderStroke(2.dp, ButtonBlue),
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                modifier = Modifier.weight(1f).height(44.dp)
            ) {
                Text(
                    text = "${tipo.emoji} ${tipo.etiqueta} ($cantidad)",
                    color = if (activo) Color.Black else AzulOscuro,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun PetCard(mascota: Mascota, modifier: Modifier = Modifier, onClickDetalles: () -> Unit) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MascotaFoto(
            imagen = mascota.imagen,
            descripcion = mascota.nombre,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.75f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "${mascota.nombre} (${mascota.edad})",
            fontFamily = FontFamily.Monospace,
            fontSize = 14.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onClickDetalles,
            colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.height(36.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
        ) {
            Text("DETALLES", fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 12.sp)
        }
    }
}