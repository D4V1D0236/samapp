package com.example.samapp.donaciones

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration

val CATEGORIAS_CAMPANA = listOf(
    "Alimento",
    "Medicamentos",
    "Esterilización y vacunación",
    "Otra necesidad"
)

/** Campaña o necesidad publicada por un administrador. Montos en pesos colombianos (COP). */
data class Campana(
    val id: String,
    val titulo: String,
    val descripcion: String,
    val categoria: String,
    val meta: Long,
    val recaudado: Long,
    val creadoEn: Long
)

/**
 * Colección Firestore: campanas/{id}
 * Lectura: cualquier usuario autenticado. Escritura: solo administradores (ver firestore.rules).
 */
class CampanasRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    /** Escucha los cambios en tiempo real. Quien la llame debe hacer remove() al salir. */
    fun escuchar(
        onResult: (List<Campana>) -> Unit,
        onError: (String) -> Unit
    ): ListenerRegistration {
        return db.collection("campanas").addSnapshotListener { resultado, error ->
            if (error != null) {
                onError(mensaje(error, "No se pudieron cargar las campañas."))
                return@addSnapshotListener
            }
            val lista = resultado?.documents?.map { d ->
                Campana(
                    id = d.id,
                    titulo = d.getString("titulo") ?: "",
                    descripcion = d.getString("descripcion") ?: "",
                    categoria = d.getString("categoria") ?: "",
                    meta = d.getLong("meta") ?: 0L,
                    recaudado = d.getLong("recaudado") ?: 0L,
                    // Mientras el servidor asigna la fecha llega null: se muestra de primera
                    creadoEn = d.getTimestamp("creadoEn")?.toDate()?.time ?: Long.MAX_VALUE
                )
            }?.sortedByDescending { it.creadoEn } ?: emptyList()
            onResult(lista)
        }
    }

    fun publicar(
        titulo: String,
        descripcion: String,
        categoria: String,
        meta: Long,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            onError("Debes iniciar sesión.")
            return
        }
        val datos = hashMapOf(
            "titulo" to titulo.trim(),
            "descripcion" to descripcion.trim(),
            "categoria" to categoria,
            "meta" to meta,
            "recaudado" to 0L,
            "creadoPor" to uid,
            "creadoEn" to FieldValue.serverTimestamp()
        )
        db.collection("campanas").add(datos)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(mensaje(it, "No se pudo publicar la campaña.")) }
    }

    fun actualizarRecaudado(
        id: String,
        recaudado: Long,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        db.collection("campanas").document(id).update("recaudado", recaudado)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(mensaje(it, "No se pudo actualizar la campaña.")) }
    }

    fun eliminar(id: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        db.collection("campanas").document(id).delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(mensaje(it, "No se pudo eliminar la campaña.")) }
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
