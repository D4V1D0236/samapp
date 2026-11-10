package com.example.samapp.documentos

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.samapp.inicio.ButtonBlue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val AzulOscuro = Color(0xFF1B263B)
private val Rojo = Color(0xFFE63946)

/**
 * Lista de documentos de un propietario (una mascota o un usuario) con las acciones permitidas.
 *  - [puedeSubir]: muestra el botón para adjuntar.
 *  - [puedeEliminar]: muestra el botón para eliminar.
 * Cualquiera puede abrir los documentos que tenga permiso de ver (lo valida Firestore).
 */
@Composable
fun DocumentosPanel(
    titulo: String,
    propietarioTipo: String,
    propietarioId: String,
    categorias: List<String>,
    puedeSubir: Boolean,
    puedeEliminar: Boolean,
    modifier: Modifier = Modifier,
    textoVacio: String = "Aún no hay documentos cargados."
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repo = remember { DocumentosRepository() }

    var documentos by remember { mutableStateOf<List<Documento>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }
    var recarga by remember { mutableIntStateOf(0) }
    var mostrarSubida by remember { mutableStateOf(false) }
    var categoriaElegida by remember { mutableStateOf(categorias.first()) }
    var subiendo by remember { mutableStateOf(false) }
    var abriendoId by remember { mutableStateOf<String?>(null) }
    var paraEliminar by remember { mutableStateOf<Documento?>(null) }

    fun avisar(mensaje: String) {
        Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show()
    }

    LaunchedEffect(propietarioTipo, propietarioId, recarga) {
        cargando = true
        repo.listar(
            propietarioTipo = propietarioTipo,
            propietarioId = propietarioId,
            onResult = { documentos = it; cargando = false },
            onError = { cargando = false; avisar(it) }
        )
    }

    val selector = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            subiendo = true
            scope.launch {
                // Leer y comprimir puede tardar: se hace fuera del hilo principal
                val lectura = withContext(Dispatchers.IO) { leerArchivo(context, uri) }
                lectura
                    .onSuccess { archivo ->
                        repo.subir(
                            propietarioTipo = propietarioTipo,
                            propietarioId = propietarioId,
                            categoria = categoriaElegida,
                            archivo = archivo,
                            onSuccess = {
                                subiendo = false
                                avisar("Documento subido correctamente.")
                                recarga++
                            },
                            onError = { subiendo = false; avisar(it) }
                        )
                    }
                    .onFailure { error ->
                        subiendo = false
                        avisar(error.message ?: "No se pudo leer el archivo.")
                    }
            }
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = titulo,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = AzulOscuro
        )
        Spacer(modifier = Modifier.height(8.dp))

        when {
            cargando -> Row(verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cargando...", fontFamily = FontFamily.Monospace, fontSize = 13.sp, color = Color.Gray)
            }
            documentos.isEmpty() -> Text(
                text = textoVacio,
                fontFamily = FontFamily.Monospace,
                fontSize = 13.sp,
                color = Color.Gray
            )
            else -> documentos.forEach { documento ->
                DocumentoFila(
                    documento = documento,
                    abriendo = abriendoId == documento.id,
                    puedeEliminar = puedeEliminar,
                    onVer = {
                        abriendoId = documento.id
                        repo.obtenerContenido(
                            documentoId = documento.id,
                            onResult = { bytes ->
                                abriendoId = null
                                abrirDocumento(context, documento, bytes)
                            },
                            onError = { abriendoId = null; avisar(it) }
                        )
                    },
                    onEliminar = { paraEliminar = documento }
                )
            }
        }

        if (puedeSubir) {
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = { mostrarSubida = true },
                enabled = !subiendo,
                colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth().height(44.dp)
            ) {
                Text(
                    text = if (subiendo) "SUBIENDO..." else "ADJUNTAR DOCUMENTO",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }

    if (mostrarSubida) {
        AlertDialog(
            onDismissRequest = { mostrarSubida = false },
            title = { Text("Adjuntar documento", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("¿Qué tipo de documento es?", fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    categorias.forEach { categoria ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { categoriaElegida = categoria }
                        ) {
                            RadioButton(
                                selected = categoriaElegida == categoria,
                                onClick = { categoriaElegida = categoria }
                            )
                            Text(categoria, fontSize = 14.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Formatos: PDF, JPG o PNG. Máximo ${MAX_BYTES_DOCUMENTO / 1024} KB " +
                            "(las fotos se reducen automáticamente).",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    mostrarSubida = false
                    selector.launch(arrayOf("application/pdf", "image/jpeg", "image/png"))
                }) { Text("ELEGIR ARCHIVO") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarSubida = false }) { Text("CANCELAR") }
            }
        )
    }

    paraEliminar?.let { documento ->
        AlertDialog(
            onDismissRequest = { paraEliminar = null },
            title = { Text("Eliminar documento", fontWeight = FontWeight.Bold) },
            text = { Text("¿Seguro que quieres eliminar \"${documento.nombreArchivo}\"? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    paraEliminar = null
                    repo.eliminar(
                        documentoId = documento.id,
                        onSuccess = { avisar("Documento eliminado."); recarga++ },
                        onError = { avisar(it) }
                    )
                }) { Text("ELIMINAR", color = Rojo) }
            },
            dismissButton = {
                TextButton(onClick = { paraEliminar = null }) { Text("CANCELAR") }
            }
        )
    }
}

@Composable
private fun DocumentoFila(
    documento: Documento,
    abriendo: Boolean,
    puedeEliminar: Boolean,
    onVer: () -> Unit,
    onEliminar: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Icon(Icons.Default.Description, contentDescription = null, tint = AzulOscuro)
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = documento.categoria,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${documento.nombreArchivo} · ${(documento.tamanoBytes / 1024).coerceAtLeast(1)} KB",
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        TextButton(onClick = onVer, enabled = !abriendo) {
            Text(if (abriendo) "..." else "VER", fontWeight = FontWeight.Bold)
        }
        if (puedeEliminar) {
            TextButton(onClick = onEliminar) {
                Text("ELIMINAR", color = Rojo, fontWeight = FontWeight.Bold)
            }
        }
    }
}
