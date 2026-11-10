package com.example.samapp.historias

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.samapp.documentos.CATEGORIAS_DOCUMENTOS_MASCOTA
import com.example.samapp.documentos.DocumentosPanel
import com.example.samapp.documentos.TIPO_PROPIETARIO_MASCOTA
import com.example.samapp.inicio.BottomBarSAM
import com.example.samapp.inicio.ButtonBlue
import com.example.samapp.inicio.TopBarSAM
import com.example.samapp.navigation.NavActions

private val AzulOscuro = Color(0xFF1B263B)
private val FondoSeccion = Color(0xFFF1F6FC)

@Composable
fun DetalleMascotaScreen(
    mascotaId: String,
    nav: NavActions,
    esAdmin: Boolean = false
) {
    val context = LocalContext.current

    // Recuerda cambiar esto por tu número real
    val numeroWhatsApp = "573216944941"

    // Los datos salen de MASCOTAS (MascotasData.kt)
    val mascota = MASCOTAS.find { it.id == mascotaId } ?: MASCOTAS.first()

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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 16.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Button(
                    onClick = { nav.adopciones() },
                    colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                    modifier = Modifier.height(28.dp)
                ) {
                    Text("Regresar", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .aspectRatio(0.8f),
                contentAlignment = Alignment.BottomCenter
            ) {
                MascotaFoto(
                    imagen = mascota.imagen,
                    descripcion = mascota.nombre,
                    modifier = Modifier.fillMaxSize()
                )

                Button(
                    onClick = {
                        val mensaje = "Hola, vengo de SAM APP y estoy interesado en adoptar a " +
                            "${mascota.nombre} (${mascota.especie.lowercase()}) del refugio ${mascota.refugio.nombre}."
                        val url = "https://wa.me/$numeroWhatsApp?text=${Uri.encode(mensaje)}"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .offset(y = 20.dp)
                        .fillMaxWidth(0.8f)
                        .height(48.dp)
                ) {
                    Text("ADOPTAR", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = mascota.nombre,
                fontFamily = FontFamily.Monospace,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Seccion("INFORMACIÓN GENERAL") {
                    Dato("Especie", mascota.especie)
                    Dato("Sexo", mascota.sexo)
                    Dato("Edad aproximada", mascota.edad)
                    Dato("Tamaño", mascota.tamano)
                    Dato("Peso", mascota.peso)
                    Dato("Raza", mascota.raza)
                    Dato("Ubicación", mascota.ubicacion)
                }

                Seccion("SALUD") {
                    Dato("Estado de salud", mascota.salud)
                    Dato("Vacunas", mascota.vacunas)
                    Dato("Esterilizado", mascota.esterilizado)
                }

                Seccion("PERSONALIDAD") { Parrafo(mascota.personalidad) }

                Seccion("NECESIDADES ESPECIALES") { Parrafo(mascota.necesidadesEspeciales) }

                Seccion("HISTORIA DEL RESCATE") { Parrafo(mascota.historiaRescate) }

                Seccion("REFUGIO RESPONSABLE") {
                    Dato("Nombre", mascota.refugio.nombre)
                    Dato("Ubicación", mascota.refugio.ubicacion)
                    Spacer(modifier = Modifier.height(4.dp))
                    Parrafo(mascota.refugio.descripcion)
                }

                Seccion("HISTORIAL MÉDICO") {
                    Dato("Vacunas aplicadas", mascota.historial.vacunasAplicadas)
                    Dato("Desparasitación", mascota.historial.desparasitacion)
                    Dato("Esterilización", mascota.historial.esterilizacion)
                    Dato("Alergias", mascota.historial.alergias)
                    Dato("Enfermedades conocidas", mascota.historial.enfermedades)
                    Dato("Tratamientos", mascota.historial.tratamientos)
                    Dato("Recomendaciones veterinarias", mascota.historial.recomendaciones)
                }

                Seccion("DOCUMENTOS DE LA MASCOTA") {
                    DocumentosPanel(
                        titulo = "Certificados y documentos",
                        propietarioTipo = TIPO_PROPIETARIO_MASCOTA,
                        propietarioId = mascota.id,
                        categorias = CATEGORIAS_DOCUMENTOS_MASCOTA,
                        puedeSubir = esAdmin,
                        puedeEliminar = esAdmin,
                        textoVacio = if (esAdmin) "Aún no hay documentos. Adjunta el primero."
                        else "El refugio aún no ha adjuntado documentos."
                    )
                }

                Seccion("¿QUIERES ADOPTAR?") {
                    Parrafo("Para iniciar el proceso, el refugio te pedirá algunos documentos. Puedes cargarlos desde la app.")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = nav.documentos,
                        colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.fillMaxWidth().height(44.dp)
                    ) {
                        Text("SUBIR MIS DOCUMENTOS", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun Seccion(titulo: String, contenido: @Composable ColumnScope.() -> Unit) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = FondoSeccion,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = titulo,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = AzulOscuro
            )
            Spacer(modifier = Modifier.height(8.dp))
            contenido()
        }
    }
}

@Composable
private fun Dato(etiqueta: String, valor: String) {
    Text(
        text = buildAnnotatedString {
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("$etiqueta: ") }
            append(valor)
        },
        fontFamily = FontFamily.Monospace,
        fontSize = 14.sp,
        lineHeight = 21.sp,
        color = Color.Black,
        modifier = Modifier.padding(vertical = 2.dp)
    )
}

@Composable
private fun Parrafo(texto: String) {
    Text(
        text = texto,
        fontFamily = FontFamily.Monospace,
        fontSize = 14.sp,
        lineHeight = 21.sp,
        color = Color.Black
    )
}
