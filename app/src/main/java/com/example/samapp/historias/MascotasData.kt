package com.example.samapp.historias

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.samapp.R

enum class TipoMascota(val etiqueta: String, val emoji: String) {
    GATO("Gatos", "\uD83D\uDC31"),
    PERRO("Perros", "\uD83D\uDC36")
}

/**
 * [imagen] es el NOMBRE del archivo en res/drawable, sin la extensión.
 * Ejemplo: "animal9" -> res/drawable/animal9.png (o .jpg / .webp).
 * Si el archivo todavía no existe, la app muestra el logo de SAM como marcador de posición.
 */
data class Mascota(
    val nombre: String,      // debe ser único: se usa para abrir el detalle
    val tipo: TipoMascota,
    val imagen: String,
    val sexo: String,
    val edad: String,
    val peso: String,
    val raza: String,
    val salud: String,
    val vacunas: String
)

// =====================================================================
//  LISTA DE MASCOTAS EN ADOPCIÓN
//  Para agregar o quitar una mascota basta con editar esta lista.
// =====================================================================
val MASCOTAS = listOf(
    // ---------------- GATOS ----------------
    Mascota("Tigre",    TipoMascota.GATO, "animal5",  "Macho",  "3 años",   "5 Kg",   "Criollo", "Excelente",  "Al día"),
    Mascota("Oreo",     TipoMascota.GATO, "animal6",  "Hembra", "1 año",    "2.5 Kg", "Criollo", "Excelente",  "Al día"),
    Mascota("Mishi",    TipoMascota.GATO, "animal7",  "Hembra", "2 meses",  "2 Kg",   "Criollo", "Excelente",  "Al día"),
    Mascota("Garfield", TipoMascota.GATO, "animal8",  "Macho",  "3 meses",  "3 Kg",   "Criollo", "Excelente!", "Al día"),
    Mascota("Nala",     TipoMascota.GATO, "animal9",  "Hembra", "2 años",  "3.8 Kg", "Criollo", "Excelente",  "Al día"),
    Mascota("Simba",    TipoMascota.GATO, "animal10", "Macho",  "8 meses",    "4 Kg",   "Criollo", "Excelente",  "Al día"),

    // ---------------- PERROS ----------------
    Mascota("Negrita",      TipoMascota.PERRO, "animal11", "Hembra",  "7 meses",   "8 Kg", "Mestizo", "Excelente",  "Al día"),
    Mascota("Tony",    TipoMascota.PERRO, "animal12", "Macho",  "1 año",    "7 Kg", "Mestizo", "Excelente",  "Al día"),
    Mascota("Bruno",    TipoMascota.PERRO, "animal13", "Macho",  "2 años",   "22 Kg", "Mestizo", "Buena",      "Al día"),
    Mascota("Niña",   TipoMascota.PERRO, "animal14", "Hembra", "1 año",  "9 Kg",  "Mestizo", "Excelente",  "En proceso"),
    Mascota("Toby",     TipoMascota.PERRO, "animal15", "Macho",  "4 meses",  "5 Kg",  "Huskee", "Excelente",  "En proceso"),
    Mascota("Nala",     TipoMascota.PERRO, "animal16", "Hembra", "2 años",   "8 Kg", "Mestizo", "Excelente",  "Al día")
)

/**
 * Muestra la foto de la mascota buscando el archivo por nombre en res/drawable.
 * Así la app compila aunque todavía no hayas copiado la imagen.
 */
@SuppressLint("DiscouragedApi")
@Composable
fun MascotaFoto(imagen: String, descripcion: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val resId = remember(imagen) {
        context.resources.getIdentifier(imagen, "drawable", context.packageName)
    }

    if (resId != 0) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = descripcion,
            contentScale = ContentScale.Crop,
            modifier = modifier
        )
    } else {
        // Marcador de posición: todavía no existe res/drawable/<imagen>
        Box(
            modifier = modifier.background(Color(0xFFE3EEF8)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_sam),
                contentDescription = "Foto pendiente de $descripcion",
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxWidth(0.5f)
            )
        }
    }
}