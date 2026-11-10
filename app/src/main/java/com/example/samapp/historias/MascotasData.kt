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

/** Refugio u hogar de paso responsable del animal. */
data class Refugio(
    val nombre: String,
    val ubicacion: String,
    val descripcion: String
)

/** Historial médico básico del animal. */
data class HistorialMedico(
    val vacunasAplicadas: String,
    val desparasitacion: String,
    val esterilizacion: String,
    val alergias: String = "Ninguna conocida",
    val enfermedades: String = "Ninguna conocida",
    val tratamientos: String = "Ninguno por el momento",
    val recomendaciones: String = "Control veterinario anual y refuerzo de vacunas."
)

/**
 * [imagen] es el NOMBRE del archivo en res/drawable, sin la extensión.
 * Ejemplo: "animal9" -> res/drawable/animal9.png (o .jpg / .jpeg / .webp).
 * Si el archivo todavía no existe, la app muestra el logo de SAM como marcador de posición.
 */
data class Mascota(
    val nombre: String,
    val tipo: TipoMascota,
    val imagen: String,
    val sexo: String,
    val edad: String,                 // edad aproximada
    val tamano: String,               // Pequeño / Mediano / Grande
    val peso: String,
    val raza: String,
    val salud: String,                // estado de salud
    val vacunas: String,              // resumen: "Al día", "En proceso"...
    val esterilizado: String,         // "Sí", "No", "Pendiente (por su edad)"
    val personalidad: String,
    val necesidadesEspeciales: String = "Ninguna",
    val historiaRescate: String,
    val refugio: Refugio = REFUGIO_PATITAS_UNIDAS,
    val ubicacion: String = refugio.ubicacion,
    val historial: HistorialMedico
) {
    /** Identificador único (tipo + nombre): permite una "Nala" gata y una "Nala" perra. */
    val id: String get() = "${tipo.name.lowercase()}_${nombre.lowercase()}"

    val especie: String get() = if (tipo == TipoMascota.GATO) "Gato" else "Perro"
}

// =====================================================================
//  REFUGIOS  (para agregar otro, crea otro Refugio y úsalo en la mascota)
// =====================================================================
val REFUGIO_PATITAS_UNIDAS = Refugio(
    nombre = "Patitas Unidas",
    ubicacion = "Bogotá D.C.",
    descripcion = "Refugio dedicado al rescate y a la adopción responsable de perros y gatos. " +
        "Para coordinar una visita o resolver dudas escribe por WhatsApp con el botón ADOPTAR."
)

// =====================================================================
//  LISTA DE MASCOTAS EN ADOPCIÓN
//  Para agregar o quitar una mascota basta con editar esta lista.
//  OJO: los textos de personalidad, historia e historial médico son de EJEMPLO.
// =====================================================================
val MASCOTAS = listOf(
    // ---------------- GATOS ----------------
    Mascota(
        nombre = "Tigre", tipo = TipoMascota.GATO, imagen = "animal5",
        sexo = "Macho", edad = "3 años", tamano = "Mediano", peso = "5 Kg", raza = "Criollo",
        salud = "Excelente", vacunas = "Al día", esterilizado = "Sí",
        personalidad = "Tranquilo y cariñoso. Le gusta dormir en lugares altos y buscar mimos.",
        historiaRescate = "Fue rescatado de un patio donde vivía sin cuidados. Hoy está sano y busca un hogar tranquilo.",
        historial = HistorialMedico(
            vacunasAplicadas = "Triple felina y antirrábica, con refuerzo anual al día",
            desparasitacion = "Interna y externa hace 2 meses",
            esterilizacion = "Realizada"
        )
    ),
    Mascota(
        nombre = "Oreo", tipo = TipoMascota.GATO, imagen = "animal6",
        sexo = "Hembra", edad = "1 año", tamano = "Pequeño", peso = "2.5 Kg", raza = "Criollo",
        salud = "Excelente", vacunas = "Al día", esterilizado = "Sí",
        personalidad = "Juguetona y curiosa. Se lleva bien con otros gatos.",
        historiaRescate = "Fue encontrada en la calle cuando era muy pequeña. Se recuperó muy bien y es muy sociable.",
        historial = HistorialMedico(
            vacunasAplicadas = "Triple felina y antirrábica al día",
            desparasitacion = "Interna y externa hace 1 mes",
            esterilizacion = "Realizada"
        )
    ),
    Mascota(
        nombre = "Mishi", tipo = TipoMascota.GATO, imagen = "animal7",
        sexo = "Hembra", edad = "2 meses", tamano = "Pequeño", peso = "2 Kg", raza = "Criollo",
        salud = "Excelente", vacunas = "Al día", esterilizado = "Pendiente (por su edad)",
        personalidad = "Gatita muy activa que alterna juegos con largas siestas. Necesita compañía.",
        necesidadesEspeciales = "Alimento para gatitos y atención constante por su corta edad.",
        historiaRescate = "Fue encontrada abandonada siendo recién nacida y criada por el equipo de rescate.",
        historial = HistorialMedico(
            vacunasAplicadas = "Esquema de gatito al día para su edad",
            desparasitacion = "Cada 15 días según su edad",
            esterilizacion = "Pendiente: se recomienda después de los 6 meses",
            recomendaciones = "Completar el esquema de vacunación y esterilizar después de los 6 meses."
        )
    ),
    Mascota(
        nombre = "Garfield", tipo = TipoMascota.GATO, imagen = "animal8",
        sexo = "Macho", edad = "3 meses", tamano = "Pequeño", peso = "3 Kg", raza = "Criollo",
        salud = "Excelente!", vacunas = "Al día", esterilizado = "Sí",
        personalidad = "Cariñoso, juguetón y muy comilón.",
        historiaRescate = "Fue rescatado de la calle siendo muy pequeño. Hoy está sano y listo para conocer a su familia.",
        historial = HistorialMedico(
            vacunasAplicadas = "Esquema de gatito al día para su edad",
            desparasitacion = "Interna y externa hace 1 mes",
            esterilizacion = "Realizada"
        )
    ),
    Mascota(
        nombre = "Nala", tipo = TipoMascota.GATO, imagen = "animal9",
        sexo = "Hembra", edad = "2 años", tamano = "Mediano", peso = "3.8 Kg", raza = "Criollo",
        salud = "Excelente", vacunas = "Al día", esterilizado = "Sí",
        personalidad = "Independiente pero cariñosa. Le gusta mirar por la ventana.",
        historiaRescate = "Fue entregada por una familia que debió mudarse y no pudo llevarla consigo.",
        historial = HistorialMedico(
            vacunasAplicadas = "Triple felina y antirrábica al día",
            desparasitacion = "Interna y externa hace 2 meses",
            esterilizacion = "Realizada"
        )
    ),
    Mascota(
        nombre = "Simba", tipo = TipoMascota.GATO, imagen = "animal10",
        sexo = "Macho", edad = "8 meses", tamano = "Mediano", peso = "4 Kg", raza = "Criollo",
        salud = "Excelente", vacunas = "Al día", esterilizado = "Pendiente (programada)",
        personalidad = "Aventurero y muy activo. Le encanta jugar con cuerdas y trepar.",
        necesidadesEspeciales = "Espacio para jugar y trepar.",
        historiaRescate = "Fue rescatado de la calle cuando tenía pocas semanas y creció en un hogar de paso.",
        historial = HistorialMedico(
            vacunasAplicadas = "Triple felina al día",
            desparasitacion = "Interna y externa hace 1 mes",
            esterilizacion = "Programada con el veterinario del refugio",
            recomendaciones = "Esterilizar y aplicar el refuerzo anual de vacunas."
        )
    ),

    // ---------------- PERROS ----------------
    Mascota(
        nombre = "Negrita", tipo = TipoMascota.PERRO, imagen = "animal11",
        sexo = "Hembra", edad = "7 meses", tamano = "Pequeño", peso = "8 Kg", raza = "Mestizo",
        salud = "Excelente", vacunas = "Al día", esterilizado = "Pendiente (programada)",
        personalidad = "Cariñosa, sociable y muy juguetona con las personas.",
        necesidadesEspeciales = "Paseos diarios y socialización.",
        historiaRescate = "Fue encontrada en la vía pública y entregada al refugio por una vecina.",
        historial = HistorialMedico(
            vacunasAplicadas = "Esquema de cachorro completo",
            desparasitacion = "Hace 1 mes",
            esterilizacion = "Programada con el veterinario del refugio",
            recomendaciones = "Esterilizar y aplicar el refuerzo anual de vacunas."
        )
    ),
    Mascota(
        nombre = "Tony", tipo = TipoMascota.PERRO, imagen = "animal12",
        sexo = "Macho", edad = "1 año", tamano = "Pequeño", peso = "7 Kg", raza = "Mestizo",
        salud = "Excelente", vacunas = "Al día", esterilizado = "Sí",
        personalidad = "Tranquilo, obediente y muy apegado a las personas. Ideal para apartamento.",
        historiaRescate = "Fue abandonado en un parque. Ya se acostumbró al collar y a la correa.",
        historial = HistorialMedico(
            vacunasAplicadas = "Múltiple canina y antirrábica al día",
            desparasitacion = "Hace 2 meses",
            esterilizacion = "Realizada"
        )
    ),
    Mascota(
        nombre = "Bruno", tipo = TipoMascota.PERRO, imagen = "animal13",
        sexo = "Macho", edad = "2 años", tamano = "Mediano", peso = "10 Kg", raza = "Mestizo",
        salud = "Buena", vacunas = "Al día", esterilizado = "Sí",
        personalidad = "Leal y protector, pero muy dócil con la familia.",
        necesidadesEspeciales = "Ejercicio diario (caminatas largas).",
        historiaRescate = "Vivió varios meses en la calle. Llegó delgado y hoy ya recuperó su peso.",
        historial = HistorialMedico(
            vacunasAplicadas = "Múltiple canina y antirrábica al día",
            desparasitacion = "Hace 1 mes",
            esterilizacion = "Realizada",
            tratamientos = "Antiparasitario tópico mensual",
            recomendaciones = "Controlar el peso y mantener paseos diarios."
        )
    ),
    Mascota(
        nombre = "Niña", tipo = TipoMascota.PERRO, imagen = "animal14",
        sexo = "Hembra", edad = "1 año", tamano = "Pequeño", peso = "9 Kg", raza = "Mestizo",
        salud = "Excelente", vacunas = "En proceso", esterilizado = "Sí",
        personalidad = "Dulce. Al principio es tímida, pero después se vuelve muy cariñosa.",
        necesidadesEspeciales = "Paciencia para ganar confianza durante los primeros días.",
        historiaRescate = "Fue rescatada de un lote baldío donde vivía sola.",
        historial = HistorialMedico(
            vacunasAplicadas = "Esquema en proceso: faltan refuerzos",
            desparasitacion = "Hace 1 mes",
            esterilizacion = "Realizada",
            recomendaciones = "Completar el esquema de vacunación antes de llevarla a parques."
        )
    ),
    Mascota(
        nombre = "Toby", tipo = TipoMascota.PERRO, imagen = "animal15",
        sexo = "Macho", edad = "4 meses", tamano = "Pequeño (cachorro en crecimiento)", peso = "5 Kg", raza = "Huskee",
        salud = "Excelente", vacunas = "En proceso", esterilizado = "Pendiente (por su edad)",
        personalidad = "Energético, curioso y muy juguetón.",
        necesidadesEspeciales = "Mucho ejercicio y espacio. Evitar el calor excesivo.",
        historiaRescate = "Fue entregado por una familia que no pudo atender su energía.",
        historial = HistorialMedico(
            vacunasAplicadas = "Esquema de cachorro en proceso",
            desparasitacion = "Cada mes según su edad",
            esterilizacion = "Pendiente: se recomienda después de los 6 meses",
            recomendaciones = "Completar vacunas y esterilizar después de los 6 meses."
        )
    ),
    Mascota(
        nombre = "Nala", tipo = TipoMascota.PERRO, imagen = "animal16",
        sexo = "Hembra", edad = "2 años", tamano = "Pequeño", peso = "8 Kg", raza = "Mestizo",
        salud = "Excelente", vacunas = "Al día", esterilizado = "Sí",
        personalidad = "Cariñosa y tranquila.",
        historiaRescate = "Fue rescatada al borde de una vía y se recuperó de una desnutrición leve.",
        historial = HistorialMedico(
            vacunasAplicadas = "Múltiple canina y antirrábica al día",
            desparasitacion = "Hace 2 meses",
            esterilizacion = "Realizada"
        )
    )
)

/**
 * Muestra la foto buscando el archivo por nombre en res/drawable.
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
