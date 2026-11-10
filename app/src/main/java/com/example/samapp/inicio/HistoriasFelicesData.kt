package com.example.samapp.inicio

/**
 * Historias felices: animales que ya fueron adoptados.
 * [imagen] es el nombre del archivo en res/drawable, sin extensión (ej. "animal1").
 *
 * IMPORTANTE: los nombres, fechas e historias son de EJEMPLO. Reemplázalos por los reales.
 */
data class HistoriaFeliz(
    val nombre: String,
    val especie: String,
    val fecha: String,
    val historia: String,
    val imagen: String
)

val HISTORIAS_FELICES = listOf(
    HistoriaFeliz(
        nombre = "Mía",
        especie = "Gata",
        fecha = "Octubre de 2026",
        imagen = "animal1",
        historia = "Mía fue rescatada de la calle cuando tenía apenas dos meses. " +
            "Después de su esterilización y sus vacunas, hoy vive en un apartamento " +
            "donde su familia la consiente todos los días."
    ),
    HistoriaFeliz(
        nombre = "Canelo",
        especie = "Perro",
        fecha = "Septiembre de 2026",
        imagen = "animal2",
        historia = "Canelo llegó al refugio siendo un cachorro asustado. Con paciencia " +
            "y cariño aprendió a confiar, y ahora sale a pasear cada tarde con su nueva familia."
    ),
    HistoriaFeliz(
        nombre = "Lola",
        especie = "Perra",
        fecha = "Septiembre de 2026",
        imagen = "animal3",
        historia = "Lola esperó varios meses una oportunidad. Hoy tiene un hogar con " +
            "patio, juguetes y mucho amor."
    ),
    HistoriaFeliz(
        nombre = "Alma",
        especie = "Gata",
        fecha = "Agosto de 2026",
        imagen = "animal4",
        historia = "Alma llegó tímida y desconfiada. Con el tiempo se volvió la más " +
            "cariñosa y hoy duerme en la cama de su nueva familia."
    )
)
