package com.example.samapp.documentos

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File

/** Archivo ya validado y listo para subir. */
class ArchivoListo(val nombre: String, val mimeType: String, val bytes: ByteArray)

private const val MAX_LECTURA_BYTES = 25L * 1024 * 1024

/**
 * Lee el archivo elegido por el usuario y lo valida:
 *  - PDF: debe pesar como máximo [MAX_BYTES_DOCUMENTO].
 *  - JPG/PNG: se corrige la rotación y se comprime hasta que quepa en [MAX_BYTES_DOCUMENTO].
 * Debe llamarse fuera del hilo principal.
 */
fun leerArchivo(context: Context, uri: Uri): Result<ArchivoListo> = runCatching {
    val resolver = context.contentResolver

    var nombre = "documento"
    var tamano = -1L
    resolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE), null, null, null)
        ?.use { cursor ->
            if (cursor.moveToFirst()) {
                val iNombre = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                val iTamano = cursor.getColumnIndex(OpenableColumns.SIZE)
                if (iNombre >= 0) cursor.getString(iNombre)?.let { nombre = it }
                if (iTamano >= 0 && !cursor.isNull(iTamano)) tamano = cursor.getLong(iTamano)
            }
        }

    val mime = resolver.getType(uri) ?: "application/octet-stream"
    if (tamano > MAX_LECTURA_BYTES) {
        throw IllegalArgumentException("El archivo es demasiado grande (máximo 25 MB).")
    }

    val original = resolver.openInputStream(uri)?.use { it.readBytes() }
        ?: throw IllegalStateException("No se pudo leer el archivo.")

    when (mime) {
        "application/pdf" -> {
            if (original.size > MAX_BYTES_DOCUMENTO) {
                throw IllegalArgumentException(
                    "El PDF pesa ${original.size / 1024} KB y el máximo es ${MAX_BYTES_DOCUMENTO / 1024} KB. " +
                        "Comprímelo o súbelo como foto."
                )
            }
            ArchivoListo(nombre, mime, original)
        }
        "image/jpeg", "image/png" -> {
            val jpg = comprimirImagen(original)
            ArchivoListo(nombre.substringBeforeLast('.') + ".jpg", "image/jpeg", jpg)
        }
        else -> throw IllegalArgumentException("Formato no permitido. Usa PDF, JPG o PNG.")
    }
}

private fun comprimirImagen(original: ByteArray): ByteArray {
    val limites = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    BitmapFactory.decodeByteArray(original, 0, original.size, limites)
    val mayor = maxOf(limites.outWidth, limites.outHeight)
    if (mayor <= 0) throw IllegalArgumentException("No se pudo leer la imagen.")

    // Decodifica en un tamaño razonable para no agotar la memoria con fotos de 12 MP o más
    var muestra = 1
    while (mayor / (muestra * 2) >= 1600) muestra *= 2
    val opciones = BitmapFactory.Options().apply { inSampleSize = muestra }
    val decodificada = BitmapFactory.decodeByteArray(original, 0, original.size, opciones)
        ?: throw IllegalArgumentException("No se pudo leer la imagen.")
    val bitmap = corregirRotacion(decodificada, original)

    for (lado in intArrayOf(1600, 1280, 1024, 800, 640)) {
        val reducida = reducir(bitmap, lado)
        for (calidad in intArrayOf(85, 70, 55)) {
            val salida = ByteArrayOutputStream()
            reducida.compress(Bitmap.CompressFormat.JPEG, calidad, salida)
            if (salida.size() <= MAX_BYTES_DOCUMENTO) return salida.toByteArray()
        }
    }
    throw IllegalArgumentException(
        "No se pudo reducir la imagen por debajo de ${MAX_BYTES_DOCUMENTO / 1024} KB."
    )
}

private fun reducir(bitmap: Bitmap, ladoMax: Int): Bitmap {
    val mayor = maxOf(bitmap.width, bitmap.height)
    if (mayor <= ladoMax) return bitmap
    val factor = ladoMax.toFloat() / mayor
    return Bitmap.createScaledBitmap(
        bitmap,
        (bitmap.width * factor).toInt().coerceAtLeast(1),
        (bitmap.height * factor).toInt().coerceAtLeast(1),
        true
    )
}

private fun corregirRotacion(bitmap: Bitmap, original: ByteArray): Bitmap {
    val orientacion = try {
        ExifInterface(ByteArrayInputStream(original))
            .getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
    } catch (e: Exception) {
        ExifInterface.ORIENTATION_NORMAL
    }

    val matriz = Matrix()
    when (orientacion) {
        ExifInterface.ORIENTATION_ROTATE_90 -> matriz.postRotate(90f)
        ExifInterface.ORIENTATION_ROTATE_180 -> matriz.postRotate(180f)
        ExifInterface.ORIENTATION_ROTATE_270 -> matriz.postRotate(270f)
        else -> return bitmap
    }
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matriz, true)
}

/** Guarda el contenido en caché y lo abre con la app del teléfono (visor de PDF o galería). */
fun abrirDocumento(context: Context, documento: Documento, bytes: ByteArray) {
    val carpeta = File(context.cacheDir, "documentos").apply { mkdirs() }
    val extension = when (documento.mimeType) {
        "application/pdf" -> "pdf"
        "image/png" -> "png"
        else -> "jpg"
    }
    val archivo = File(carpeta, "${documento.id}.$extension")
    archivo.writeBytes(bytes)

    val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", archivo)
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, documento.mimeType)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "No hay una aplicación para abrir este archivo.", Toast.LENGTH_LONG).show()
    }
}
