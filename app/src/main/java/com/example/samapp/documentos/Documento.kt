package com.example.samapp.documentos

/** A quién pertenece el documento. */
const val TIPO_PROPIETARIO_MASCOTA = "MASCOTA"   // certificados del animal (los sube un administrador)
const val TIPO_PROPIETARIO_USUARIO = "USUARIO"   // documentos del adoptante (los sube el propio adoptante)

/**
 * Tamaño máximo por archivo. Los archivos se guardan dentro de Firestore (un documento admite
 * 1 MiB en total), porque Cloud Storage de Firebase exige el plan de pago Blaze.
 */
const val MAX_BYTES_DOCUMENTO = 700 * 1024

val CATEGORIAS_DOCUMENTOS_MASCOTA = listOf(
    "Certificado de vacunación",
    "Certificado de esterilización",
    "Historia clínica veterinaria",
    "Otro documento"
)

val CATEGORIAS_DOCUMENTOS_USUARIO = listOf(
    "Documento de identidad",
    "Comprobante de domicilio",
    "Carta de compromiso de adopción",
    "Otro documento"
)

/** Datos del documento (sin el contenido, que se descarga solo al abrirlo). */
data class Documento(
    val id: String,
    val propietarioTipo: String,
    val propietarioId: String,
    val categoria: String,
    val nombreArchivo: String,
    val mimeType: String,
    val tamanoBytes: Long,
    val creadoEn: Long
)
