package com.example.samapp.formulario

import android.util.Patterns
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.samapp.model.RegistroState
import java.util.Calendar

val TextNavy = Color(0xFF1B263B)
val ButtonBlue = Color(0xFF6CA0DC)
val AsteriskRed = Color(0xFFE63946)

private val TERMINOS = listOf(
    "La persona adoptante debe ser mayor de edad y responsable del sustento económico del hogar.",
    "Se compromete a educar y dedicar tiempo a la mascota.",
    "Se compromete a esterilizar a la mascota después de terminar el esquema de vacunas.",
    "Se compromete a no maltratar de ninguna manera a la mascota.",
    "Se compromete a mantener al día su atención veterinaria, alimentación de calidad, placa y chip de identificación.",
    "Se compromete a vacunar a la mascota (esquema de cachorro) y luego anualmente.",
    "Se compromete a enviar fotos y videos del carnet de vacunas y de la mascota cuando lo requiera el rescatista.",
    "Si incumple lo pactado, se recogerá la mascota en el domicilio con acompañamiento policial."
)

// ---------------------------------------------------------------- Flujo

private val RegistroSaver = listSaver<RegistroState, String>(
    save = { r ->
        listOf(
            r.nombreCompleto, r.tipoDocumento, r.numeroDocumento, r.correo, r.fechaNacimiento,
            r.ciudad, r.direccion, r.contrasena, r.confirmarContrasena, r.tipoVivienda,
            r.personasEnCasa, r.hayNinos, r.tieneMascotas, r.tieneZonaVerde, r.edadesNinos,
            r.cantidadMascotas, r.tipoMascotas, r.mascotasVacunadasEsterilizadas,
            r.motivoAdopcion, r.planesMudanzaViaje, r.responsableCuidado, r.participacion
        )
    },
    restore = { l ->
        RegistroState(
            nombreCompleto = l[0], tipoDocumento = l[1], numeroDocumento = l[2], correo = l[3],
            fechaNacimiento = l[4], ciudad = l[5], direccion = l[6], contrasena = l[7],
            confirmarContrasena = l[8], tipoVivienda = l[9], personasEnCasa = l[10],
            hayNinos = l[11], tieneMascotas = l[12], tieneZonaVerde = l[13], edadesNinos = l[14],
            cantidadMascotas = l[15], tipoMascotas = l[16], mascotasVacunadasEsterilizadas = l[17],
            motivoAdopcion = l[18], planesMudanzaViaje = l[19], responsableCuidado = l[20],
            participacion = l[21]
        )
    }
)

@Composable
fun FormularioApp(
    onCancelForm: () -> Unit,
    onFinishForm: (RegistroState) -> Unit
) {
    var currentStep by rememberSaveable { mutableIntStateOf(1) }
    var registro by rememberSaveable(stateSaver = RegistroSaver) { mutableStateOf(RegistroState()) }
    var termsAccepted by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }
    var errorEvent by remember { mutableIntStateOf(0) }
    val scrollState = rememberScrollState()

    var primeraVez by remember { mutableStateOf(true) }
    LaunchedEffect(currentStep, errorEvent) {
        if (primeraVez) primeraVez = false else scrollState.animateScrollTo(0)
    }

    fun irA(paso: Int) { errorMessage = null; currentStep = paso }

    // Devuelve true si no hay error; si lo hay, lo muestra.
    fun validar(mensaje: String?): Boolean {
        errorMessage = mensaje
        if (mensaje != null) errorEvent++
        return mensaje == null
    }

    // Botón atrás del celular: vuelve al paso anterior en vez de salir del registro.
    BackHandler { if (currentStep > 1) irA(currentStep - 1) else onCancelForm() }

    // Sube al inicio al cambiar de paso o cuando aparece un error.
    LaunchedEffect(currentStep, errorEvent) { scrollState.animateScrollTo(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .imePadding()
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.widthIn(max = 600.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                errorMessage?.let { message ->
                    Text(
                        text = message,
                        color = AsteriskRed,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(AsteriskRed.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    )
                    Spacer(Modifier.height(12.dp))
                }

                when (currentStep) {
                    1 -> InfoBasicaScreen(
                        registro = registro,
                        onChange = { registro = it },
                        onNext = { if (validar(validarPaso1(registro))) irA(2) },
                        onCancel = onCancelForm
                    )

                    2 -> InfoViviendaScreen(
                        registro = registro,
                        onChange = { registro = it },
                        onNext = { if (validar(validarPaso2(registro))) irA(3) },
                        onBack = { irA(1) }
                    )

                    3 -> InfoMascotasNinosScreen(
                        registro = registro,
                        onChange = { registro = it },
                        onNext = { if (validar(validarPaso3(registro))) irA(4) },
                        onBack = { irA(2) }
                    )

                    4 -> InfoResponsabilidadScreen(
                        registro = registro,
                        termsAccepted = termsAccepted,
                        onTermsChanged = { termsAccepted = it },
                        onChange = { registro = it },
                        onFinish = {
                            if (validar(validarPaso4(registro, termsAccepted))) onFinishForm(registro)
                        },
                        onBack = { irA(3) }
                    )
                }
            }
        }
    }
}

// ---------------------------------------------------------- Validaciones

private fun otraSinEspecificar(valor: String) =
    valor.startsWith("Otra") && valor.substringAfter(":", "").isBlank()

private fun validarPaso1(r: RegistroState): String? = when {
    r.nombreCompleto.isBlank() -> "Ingresa tu nombre y apellidos."
    r.tipoDocumento.isBlank() -> "Ingresa tu tipo de documento."
    r.numeroDocumento.isBlank() -> "Ingresa tu número de documento."
    r.correo.isBlank() -> "Ingresa tu correo electrónico."
    !Patterns.EMAIL_ADDRESS.matcher(r.correo.trim()).matches() -> "El correo electrónico no es válido."
    r.fechaNacimiento.isBlank() -> "Ingresa tu fecha de nacimiento."
    edadDe(r.fechaNacimiento) == null -> "La fecha debe tener el formato DD/MM/AAAA y ser válida."
    edadDe(r.fechaNacimiento)!! < 18 -> "Debes ser mayor de edad para registrarte."
    r.ciudad.isBlank() -> "Ingresa tu ciudad."
    r.direccion.isBlank() -> "Ingresa tu dirección."
    r.contrasena.length < 8 -> "La contraseña debe tener al menos 8 caracteres."
    r.contrasena != r.confirmarContrasena -> "Las contraseñas no coinciden."
    else -> null
}

private fun validarPaso2(r: RegistroState): String? = when {
    r.tipoVivienda.isBlank() -> "Selecciona el tipo de vivienda."
    otraSinEspecificar(r.tipoVivienda) -> "Especifica el tipo de vivienda en \"Otra\"."
    r.personasEnCasa.isBlank() -> "Indica cuántas personas viven contigo."
    r.hayNinos.isBlank() -> "Indica si hay niños en casa."
    r.tieneMascotas.isBlank() -> "Indica si tienes mascotas."
    r.tieneZonaVerde.isBlank() -> "Indica si tu vivienda tiene patio o zona verde."
    else -> null
}

private fun validarPaso3(r: RegistroState): String? = when {
    otraSinEspecificar(r.tipoMascotas) -> "Especifica el tipo de mascota en \"Otra\"."
    else -> null
}

private fun validarPaso4(r: RegistroState, termsAccepted: Boolean): String? = when {
    r.motivoAdopcion.isBlank() -> "Explica por qué deseas adoptar."
    r.planesMudanzaViaje.isBlank() -> "Cuéntanos si planeas mudarte o viajar."
    r.responsableCuidado.isBlank() -> "Indica quién será el responsable principal del cuidado."
    r.participacion.isBlank() -> "Selecciona el tipo de participación."
    !termsAccepted -> "Debes aceptar los Términos y Condiciones."
    else -> null
}

/** Devuelve la edad en años, o null si la fecha no es válida (formato DD/MM/AAAA). */
private fun edadDe(fecha: String): Int? {
    val p = fecha.split("/")
    if (p.size != 3 || p[2].length != 4) return null
    val d = p[0].toIntOrNull() ?: return null
    val m = p[1].toIntOrNull() ?: return null
    val y = p[2].toIntOrNull() ?: return null
    val nac = Calendar.getInstance()
    nac.isLenient = false
    nac.clear()
    try {
        nac.set(y, m - 1, d)
        nac.timeInMillis // fuerza la validación
    } catch (e: Exception) {
        return null
    }
    val hoy = Calendar.getInstance()
    var edad = hoy.get(Calendar.YEAR) - y
    if (hoy.get(Calendar.DAY_OF_YEAR) < nac.get(Calendar.DAY_OF_YEAR)) edad--
    return if (edad < 0) null else edad
}

/** Convierte lo que escribe el usuario en DD/MM/AAAA. */
private fun formatearFecha(entrada: String): String {
    val d = entrada.filter { it.isDigit() }.take(8)
    return buildString {
        append(d.take(2))
        if (d.length > 2) append("/").append(d.substring(2, minOf(4, d.length)))
        if (d.length > 4) append("/").append(d.substring(4))
    }
}

// ------------------------------------------------------------- Pantallas

@Composable
fun InfoBasicaScreen(
    registro: RegistroState,
    onChange: (RegistroState) -> Unit,
    onNext: () -> Unit,
    onCancel: () -> Unit
) {
    FormTitle("INFORMACIÓN BASICA")
    FormTextField("Nombre/s y Apellidos", registro.nombreCompleto) { onChange(registro.copy(nombreCompleto = it)) }
    FormTextField("Tipo de documento", registro.tipoDocumento) { onChange(registro.copy(tipoDocumento = it)) }
    FormTextField("Número de documento", registro.numeroDocumento) { onChange(registro.copy(numeroDocumento = it)) }
    FormTextField("Correo electrónico", registro.correo, keyboardType = KeyboardType.Email) {
        onChange(registro.copy(correo = it.trim()))
    }
    FormTextField(
        "Fecha de Nacimiento", registro.fechaNacimiento,
        placeholder = "DD/MM/AAAA", keyboardType = KeyboardType.Number
    ) { onChange(registro.copy(fechaNacimiento = formatearFecha(it))) }
    FormTextField("Ciudad", registro.ciudad) { onChange(registro.copy(ciudad = it)) }
    FormTextField("Dirección", registro.direccion) { onChange(registro.copy(direccion = it)) }
    FormTextField("Contraseña", registro.contrasena, password = true) { onChange(registro.copy(contrasena = it)) }
    FormTextField("Confirmar Contraseña", registro.confirmarContrasena, password = true) {
        onChange(registro.copy(confirmarContrasena = it))
    }
    Text(
        text = "Mínimo 8 caracteres. Combina mayúsculas, minúsculas, números y símbolos, y evita datos fáciles de adivinar.",
        fontSize = 12.sp,
        color = Color.Gray,
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(Modifier.height(24.dp))
    NavigationButtons(onBack = onCancel, onNext = onNext, backText = "CANCELAR", nextText = "SIGUIENTE")
}

@Composable
fun InfoViviendaScreen(
    registro: RegistroState,
    onChange: (RegistroState) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    FormTitle("INFORMACIÓN DE VIVIENDA")
    FormRadioGroup("Tipo de vivienda", listOf("Propia", "Familiar", "Arrendada", "Otra"), registro.tipoVivienda) {
        onChange(registro.copy(tipoVivienda = it))
    }
    FormTextField(
        "¿Cuántas personas viven contigo?", registro.personasEnCasa,
        widthFraction = 0.5f, keyboardType = KeyboardType.Number
    ) { onChange(registro.copy(personasEnCasa = it.filter(Char::isDigit).take(2))) }
    FormRadioGroup("¿Hay niños en casa?", listOf("Si", "No"), registro.hayNinos) {
        onChange(registro.copy(hayNinos = it))
    }
    FormRadioGroup("¿Tienes mascotas?", listOf("Si", "No"), registro.tieneMascotas) {
        onChange(registro.copy(tieneMascotas = it))
    }
    FormRadioGroup("¿Tu vivienda tiene patio o zona verde?", listOf("Si", "No"), registro.tieneZonaVerde) {
        onChange(registro.copy(tieneZonaVerde = it))
    }

    Spacer(Modifier.height(24.dp))
    NavigationButtons(onBack = onBack, onNext = onNext)
}

@Composable
fun InfoMascotasNinosScreen(
    registro: RegistroState,
    onChange: (RegistroState) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    FormTextField(
        "¿Qué edades tienen los niños? (en caso de que aplique)", registro.edadesNinos,
        required = false, multiline = true
    ) { onChange(registro.copy(edadesNinos = it)) }
    FormTextField(
        "¿Cuántas mascotas tienes actualmente? (en caso de que aplique)", registro.cantidadMascotas,
        required = false, keyboardType = KeyboardType.Number
    ) { onChange(registro.copy(cantidadMascotas = it.filter(Char::isDigit).take(2))) }
    FormRadioGroup(
        "¿Qué tipo de mascotas tienes actualmente?", listOf("Perro", "Gato", "Otra"),
        registro.tipoMascotas, required = false
    ) { onChange(registro.copy(tipoMascotas = it)) }
    FormRadioGroup(
        "¿Tus mascotas están vacunadas y esterilizadas?", listOf("Si", "No", "En proceso"),
        registro.mascotasVacunadasEsterilizadas, required = false
    ) { onChange(registro.copy(mascotasVacunadasEsterilizadas = it)) }

    Spacer(Modifier.height(24.dp))
    NavigationButtons(onBack = onBack, onNext = onNext)
}

@Composable
fun InfoResponsabilidadScreen(
    registro: RegistroState,
    termsAccepted: Boolean,
    onTermsChanged: (Boolean) -> Unit,
    onChange: (RegistroState) -> Unit,
    onFinish: () -> Unit,
    onBack: () -> Unit
) {
    var showTerms by rememberSaveable { mutableStateOf(false) }

    FormTitle("INFORMACIÓN DE RESPONSABILIDAD")
    FormTextField("¿Por qué deseas adoptar?", registro.motivoAdopcion, multiline = true) {
        onChange(registro.copy(motivoAdopcion = it))
    }
    FormTextField("¿Planeas mudarte o viajar?", registro.planesMudanzaViaje, multiline = true) {
        onChange(registro.copy(planesMudanzaViaje = it))
    }
    FormTextField(
        "¿Quién será el responsable principal del cuidado del animal?",
        registro.responsableCuidado, multiline = true
    ) { onChange(registro.copy(responsableCuidado = it)) }
    FormRadioGroup(
        "¿Qué tipo de participación te gustaría tener con nosotros?",
        listOf("Adoptante", "Hogar de Paso", "Ambas"), registro.participacion
    ) { onChange(registro.copy(participacion = it)) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Checkbox(checked = termsAccepted, onCheckedChange = onTermsChanged)
        Column {
            Text("He leído y aceptado los", fontWeight = FontWeight.Bold, color = TextNavy)
            Text(
                text = "Términos y Condiciones",
                color = ButtonBlue,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { showTerms = true } // solo abre, no acepta
            )
        }
    }

    Spacer(Modifier.height(16.dp))
    NavigationButtons(onBack = onBack, onNext = onFinish, nextText = "FINALIZAR")

    if (showTerms) {
        AlertDialog(
            onDismissRequest = { showTerms = false },
            title = { Text("Términos y Condiciones", fontWeight = FontWeight.Bold) },
            text = {
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    TERMINOS.forEach { Text("•  $it", fontSize = 14.sp, modifier = Modifier.padding(vertical = 3.dp)) }
                }
            },
            confirmButton = {
                TextButton(onClick = { onTermsChanged(true); showTerms = false }) { Text("ACEPTAR") }
            },
            dismissButton = {
                TextButton(onClick = { showTerms = false }) { Text("CERRAR") }
            }
        )
    }
}

// ------------------------------------------------------------ Componentes

@Composable
fun FormTitle(title: String) {
    Text(
        text = title,
        fontFamily = FontFamily.Monospace,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp, top = 8.dp)
    )
}

@Composable
fun FormLabel(text: String, required: Boolean = true) {
    Text(
        text = buildAnnotatedString {
            append(text)
            if (required) {
                withStyle(SpanStyle(color = AsteriskRed, fontSize = 18.sp)) { append(" *") }
            }
        },
        color = TextNavy,
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun FormTextField(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    required: Boolean = true,
    placeholder: String = "",
    multiline: Boolean = false,
    password: Boolean = false,
    widthFraction: Float = 1f,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    var visible by rememberSaveable { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth().padding(bottom = 16.dp)) {
        FormLabel(label, required)
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(widthFraction),
            singleLine = !multiline,
            minLines = if (multiline) 4 else 1,
            maxLines = if (multiline) 8 else 1,
            textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
            cursorBrush = SolidColor(TextNavy),
            visualTransformation = if (password && !visible) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType = if (password) KeyboardType.Password else keyboardType,
                imeAction = if (multiline) ImeAction.Default else ImeAction.Next
            ),
            decorationBox = { inner ->
                Row(
                    verticalAlignment = if (multiline) Alignment.Top else Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(if (multiline) Modifier else Modifier.height(44.dp))
                        .border(1.dp, Color.Black)
                        .background(Color.White)
                        .padding(horizontal = 12.dp, vertical = if (multiline) 10.dp else 0.dp)
                ) {
                    Box(Modifier.weight(1f)) {
                        if (value.isEmpty() && placeholder.isNotEmpty()) {
                            Text(placeholder, color = Color.Gray, fontSize = 16.sp)
                        }
                        inner()
                    }
                    if (password) {
                        Text(
                            text = if (visible) "OCULTAR" else "VER",
                            color = ButtonBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .clickable { visible = !visible }
                        )
                    }
                }
            }
        )
    }
}

/** Si eliges "Otra", aparece una línea para escribir; se guarda como "Otra: texto". */
@Composable
fun FormRadioGroup(
    label: String,
    options: List<String>,
    selectedOption: String,
    required: Boolean = true,
    onSelected: (String) -> Unit
) {
    val isOtra = selectedOption == "Otra" || selectedOption.startsWith("Otra:")
    val otroTexto = if (selectedOption.startsWith("Otra:")) selectedOption.removePrefix("Otra:").trimStart() else ""

    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
        FormLabel(label, required)
        Column(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .border(1.dp, Color.Black)
                .padding(8.dp)
        ) {
            options.forEach { option ->
                val esOtra = option == "Otra"
                val selected = if (esOtra) isOtra else selectedOption == option
                val seleccionar = { onSelected(if (esOtra) (if (isOtra) selectedOption else "Otra:") else option) }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { seleccionar() }
                        .padding(vertical = 2.dp)
                ) {
                    RadioButton(
                        selected = selected,
                        onClick = seleccionar,
                        colors = RadioButtonDefaults.colors(selectedColor = TextNavy)
                    )
                    Text(
                        text = if (esOtra) "Otra:" else option,
                        color = TextNavy,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                    if (esOtra) {
                        BasicTextField(
                            value = otroTexto,
                            onValueChange = { onSelected("Otra: $it") },
                            enabled = isOtra,
                            singleLine = true,
                            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
                            cursorBrush = SolidColor(TextNavy),
                            modifier = Modifier.weight(1f).padding(start = 8.dp, end = 8.dp),
                            decorationBox = { inner ->
                                Column {
                                    Box(Modifier.padding(vertical = 8.dp)) { inner() }
                                    Box(Modifier.fillMaxWidth().height(1.dp).background(Color.Black))
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NavigationButtons(
    onBack: (() -> Unit)?,
    onNext: () -> Unit,
    backText: String = "ATRAS",
    nextText: String = "SIGUIENTE"
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        if (onBack != null) {
            PillButton(backText, onBack, Modifier.weight(1f))
            Spacer(Modifier.width(16.dp))
        }
        PillButton(nextText, onNext, Modifier.weight(1f))
    }
}

@Composable
private fun PillButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue, contentColor = Color.White),
        shape = RoundedCornerShape(50),
        modifier = modifier.height(48.dp)
    ) {
        Text(text, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 1)
    }
}