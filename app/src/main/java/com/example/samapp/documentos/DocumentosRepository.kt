package com.example.samapp.documentos

import android.util.Base64
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException

/**
 * Estructura en Firestore:
 *   documentos/{id}                      -> datos del documento (nombre, categoría, tamaño...)
 *   documentos/{id}/contenido/archivo    -> { datos: "<archivo en Base64>" }
 * El contenido va aparte para que listar los documentos sea rápido.
 */
class DocumentosRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun listar(
        propietarioTipo: String,
        propietarioId: String,
        onResult: (List<Documento>) -> Unit,
        onError: (String) -> Unit
    ) {
        db.collection("documentos")
            .whereEqualTo("propietarioTipo", propietarioTipo)
            .whereEqualTo("propietarioId", propietarioId)
            .get()
            .addOnSuccessListener { resultado ->
                val lista = resultado.documents.map { d ->
                    Documento(
                        id = d.id,
                        propietarioTipo = d.getString("propietarioTipo") ?: "",
                        propietarioId = d.getString("propietarioId") ?: "",
                        categoria = d.getString("categoria") ?: "Documento",
                        nombreArchivo = d.getString("nombreArchivo") ?: "documento",
                        mimeType = d.getString("mimeType") ?: "application/octet-stream",
                        tamanoBytes = d.getLong("tamanoBytes") ?: 0L,
                        creadoEn = d.getTimestamp("creadoEn")?.toDate()?.time ?: 0L
                    )
                }.sortedByDescending { it.creadoEn }
                onResult(lista)
            }
            .addOnFailureListener { onError(mensaje(it, "No se pudieron cargar los documentos.")) }
    }

    fun subir(
        propietarioTipo: String,
        propietarioId: String,
        categoria: String,
        archivo: ArchivoListo,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            onError("Debes iniciar sesión para subir documentos.")
            return
        }
        if (archivo.bytes.size > MAX_BYTES_DOCUMENTO) {
            onError("El archivo supera el máximo de ${MAX_BYTES_DOCUMENTO / 1024} KB.")
            return
        }

        val referencia = db.collection("documentos").document()
        val datos = hashMapOf(
            "propietarioTipo" to propietarioTipo,
            "propietarioId" to propietarioId,
            "categoria" to categoria,
            "nombreArchivo" to archivo.nombre,
            "mimeType" to archivo.mimeType,
            "tamanoBytes" to archivo.bytes.size.toLong(),
            "subidoPor" to uid,
            "creadoEn" to FieldValue.serverTimestamp()
        )
        val contenido = hashMapOf(
            "datos" to Base64.encodeToString(archivo.bytes, Base64.NO_WRAP)
        )

        // Los dos documentos se escriben juntos: o se guardan ambos o ninguno
        db.batch()
            .set(referencia, datos)
            .set(referencia.collection("contenido").document("archivo"), contenido)
            .commit()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(mensaje(it, "No se pudo subir el documento.")) }
    }

    fun obtenerContenido(
        documentoId: String,
        onResult: (ByteArray) -> Unit,
        onError: (String) -> Unit
    ) {
        db.collection("documentos").document(documentoId)
            .collection("contenido").document("archivo")
            .get()
            .addOnSuccessListener { d ->
                val datos = d.getString("datos")
                if (datos == null) {
                    onError("El documento no tiene contenido.")
                } else {
                    try {
                        onResult(Base64.decode(datos, Base64.NO_WRAP))
                    } catch (e: IllegalArgumentException) {
                        onError("El documento está dañado.")
                    }
                }
            }
            .addOnFailureListener { onError(mensaje(it, "No se pudo abrir el documento.")) }
    }

    fun eliminar(
        documentoId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val referencia = db.collection("documentos").document(documentoId)
        db.batch()
            .delete(referencia.collection("contenido").document("archivo"))
            .delete(referencia)
            .commit()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(mensaje(it, "No se pudo eliminar el documento.")) }
    }

    private fun mensaje(error: Exception, porDefecto: String): String =
        if (error is FirebaseFirestoreException &&
            error.code == FirebaseFirestoreException.Code.PERMISSION_DENIED
        ) {
            "No tienes permiso para esta acción. Si debería funcionar, revisa que las reglas de Firestore estén publicadas."
        } else {
            error.localizedMessage ?: porDefecto
        }
}
