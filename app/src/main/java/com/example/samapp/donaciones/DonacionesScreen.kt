package com.example.samapp.donaciones

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.samapp.R
import com.example.samapp.inicio.BottomBarSAM
import com.example.samapp.inicio.ButtonBlue
import com.example.samapp.inicio.TopBarSAM
import com.example.samapp.navigation.NavActions
import java.text.NumberFormat
import java.util.Locale

private val AzulOscuro = Color(0xFF1B263B)
private val FondoCampana = Color(0xFFF1F6FC)
private val Rojo = Color(0xFFE63946)

private fun cop(valor: Long): String =
    "$" + NumberFormat.getIntegerInstance(Locale.forLanguageTag("es-CO")).format(valor)

@Composable
fun DonacionesScreen(
    nav: NavActions,
    esAdmin: Boolean
) {
    val context = LocalContext.current
    val repo = remember { CampanasRepository() }

    var campanas by remember { mutableStateOf<List<Campana>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }
    var mostrarFormulario by remember { mutableStateOf(false) }
    var campanaRecaudado by remember { mutableStateOf<Campana?>(null) }
    var campanaEliminar by remember { mutableStateOf<Campana?>(null) }

    fun avisar(mensaje: String) {
        Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show()
    }

    // Se actualiza sola cuando un administrador publica o cambia una campaña
    DisposableEffect(Unit) {
        val registro = repo.escuchar(
            onResult = { campanas = it; cargando = false },
            onError = { cargando = false; avisar(it) }
        )
        onDispose { registro.remove() }
    }

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
            Spacer(Modifier.height(24.dp))

            Text(
                text = "DONACIONES",
                fontFamily = FontFamily.Monospace,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.LightGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Apoya a los animales rescatados. Estas son las campañas y necesidades " +
                        "publicadas por los administradores de SAM.",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    color = Color.Black
                )

                if (esAdmin) {
                    Button(
                        onClick = { mostrarFormulario = true },
                        colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.fillMaxWidth().height(44.dp)
                    ) {
                        Text("PUBLICAR CAMPAÑA", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }

                Text(
                    text = "CAMPAÑAS ACTIVAS",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulOscuro
                )

                when {
                    cargando -> Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                        Spacer(Modifier.width(8.dp))
                        Text("Cargando campañas...", fontFamily = FontFamily.Monospace, fontSize = 13.sp, color = Color.Gray)
                    }
                    campanas.isEmpty() -> Text(
                        text = "Por ahora no hay campañas publicadas. Aun así, puedes donar con los datos de abajo.",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                    else -> campanas.forEach { campana ->
                        CampanaCard(
                            campana = campana,
                            esAdmin = esAdmin,
                            onActualizar = { campanaRecaudado = campana },
                            onEliminar = { campanaEliminar = campana }
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "¿CÓMO DONAR?",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulOscuro
                )

                Text(
                    text = "LLAVE BRE-B\n@BBVA32169449417\nBANCO BBVA\nTITULAR SHAYRA MELO",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    lineHeight = 28.sp
                )

                Text(
                    text = "BBVA",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF004481) // Azul BBVA
                )

                Text(
                    text = "O escanea este código QR para donar:",
                    fontSize = 12.sp,
                    color = Color.Black
                )
            }

            Image(
                painter = painterResource(id = R.drawable.imagen_qr),
                contentDescription = "Código QR Donaciones",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .aspectRatio(1f)
            )

            Spacer(Modifier.height(40.dp))
        }
    }

    if (mostrarFormulario) {
        PublicarCampanaDialog(
            onCancelar = { mostrarFormulario = false },
            onPublicar = { titulo, descripcion, categoria, meta ->
                mostrarFormulario = false
                repo.publicar(
                    titulo = titulo,
                    descripcion = descripcion,
                    categoria = categoria,
                    meta = meta,
                    onSuccess = { avisar("Campaña publicada.") },
                    onError = { avisar(it) }
                )
            }
        )
    }

    campanaRecaudado?.let { campana ->
        ActualizarRecaudadoDialog(
            campana = campana,
            onCancelar = { campanaRecaudado = null },
            onGuardar = { valor ->
                campanaRecaudado = null
                repo.actualizarRecaudado(
                    id = campana.id,
                    recaudado = valor,
                    onSuccess = { avisar("Campaña actualizada.") },
                    onError = { avisar(it) }
                )
            }
        )
    }

    campanaEliminar?.let { campana ->
        AlertDialog(
            onDismissRequest = { campanaEliminar = null },
            title = { Text("Eliminar campaña", fontWeight = FontWeight.Bold) },
            text = { Text("¿Seguro que quieres eliminar \"${campana.titulo}\"?") },
            confirmButton = {
                TextButton(onClick = {
                    campanaEliminar = null
                    repo.eliminar(
                        id = campana.id,
                        onSuccess = { avisar("Campaña eliminada.") },
                        onError = { avisar(it) }
                    )
                }) { Text("ELIMINAR", color = Rojo) }
            },
            dismissButton = { TextButton(onClick = { campanaEliminar = null }) { Text("CANCELAR") } }
        )
    }
}

@Composable
private fun CampanaCard(
    campana: Campana,
    esAdmin: Boolean,
    onActualizar: () -> Unit,
    onEliminar: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = FondoCampana,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (campana.categoria.isNotBlank()) {
                Text(
                    text = campana.categoria.uppercase(),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = ButtonBlue
                )
            }
            Text(
                text = campana.titulo,
                fontFamily = FontFamily.Monospace,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = campana.descripcion,
                fontFamily = FontFamily.Monospace,
                fontSize = 13.sp,
                lineHeight = 20.sp,
                color = Color.Black
            )

            if (campana.meta > 0) {
                val progreso = (campana.recaudado.toFloat() / campana.meta.toFloat()).coerceIn(0f, 1f)
                Spacer(Modifier.height(12.dp))
                LinearProgressIndicator(
                    progress = { progreso },
                    color = ButtonBlue,
                    modifier = Modifier.fillMaxWidth().height(8.dp)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Recaudado: ${cop(campana.recaudado)} de ${cop(campana.meta)} (${(progreso * 100).toInt()}%)",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulOscuro
                )
            }

            Spacer(Modifier.height(8.dp))
            Text(
                text = "Dona con la llave Bre-B o el código QR que aparece más abajo.",
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                color = Color.Gray
            )

            if (esAdmin) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    TextButton(onClick = onActualizar) { Text("ACTUALIZAR RECAUDADO", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                    TextButton(onClick = onEliminar) { Text("ELIMINAR", fontSize = 11.sp, color = Rojo, fontWeight = FontWeight.Bold) }
                }
            }
        }
    }
}

@Composable
private fun PublicarCampanaDialog(
    onCancelar: () -> Unit,
    onPublicar: (titulo: String, descripcion: String, categoria: String, meta: Long) -> Unit
) {
    val context = LocalContext.current
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf(CATEGORIAS_CAMPANA.first()) }
    var meta by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text("Publicar campaña", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it.take(80) },
                    label = { Text("Título") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it.take(500) },
                    label = { Text("Descripción") },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = meta,
                    onValueChange = { meta = it.filter { c -> c.isDigit() }.take(12) },
                    label = { Text("Meta en pesos (COP)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Text("Categoría", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                CATEGORIAS_CAMPANA.forEach { opcion ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { categoria = opcion }
                    ) {
                        RadioButton(selected = categoria == opcion, onClick = { categoria = opcion })
                        Text(opcion, fontSize = 14.sp)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val valorMeta = meta.toLongOrNull() ?: 0L
                when {
                    titulo.isBlank() -> Toast.makeText(context, "Escribe un título.", Toast.LENGTH_SHORT).show()
                    descripcion.isBlank() -> Toast.makeText(context, "Escribe una descripción.", Toast.LENGTH_SHORT).show()
                    valorMeta <= 0L -> Toast.makeText(context, "La meta debe ser mayor a cero.", Toast.LENGTH_SHORT).show()
                    else -> onPublicar(titulo, descripcion, categoria, valorMeta)
                }
            }) { Text("PUBLICAR") }
        },
        dismissButton = { TextButton(onClick = onCancelar) { Text("CANCELAR") } }
    )
}

@Composable
private fun ActualizarRecaudadoDialog(
    campana: Campana,
    onCancelar: () -> Unit,
    onGuardar: (Long) -> Unit
) {
    var valor by remember { mutableStateOf(campana.recaudado.toString()) }

    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text("Monto recaudado", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Text(campana.titulo, fontSize = 13.sp, color = Color.Gray)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = valor,
                    onValueChange = { valor = it.filter { c -> c.isDigit() }.take(12) },
                    label = { Text("Recaudado hasta hoy (COP)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onGuardar(valor.toLongOrNull() ?: 0L) }) { Text("GUARDAR") }
        },
        dismissButton = { TextButton(onClick = onCancelar) { Text("CANCELAR") } }
    )
}
