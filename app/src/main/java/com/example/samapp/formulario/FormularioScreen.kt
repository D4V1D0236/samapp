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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.samapp.model.RegistroState
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

val TextNavy = Color(0xFF1B263B)
val ButtonBlue = Color(0xFF6CA0DC)
val AsteriskRed = Color(0xFFE63946)
private val VerdeLeido = Color(0xFF2E7D32)

// Valor que se guarda (abreviación) -> texto que se ve en el menú
private val TIPOS_DOCUMENTO = listOf("CC", "CE", "PA")
private fun nombreTipoDocumento(valor: String) = when (valor) {
    "CC" -> "CC - Cédula de Ciudadanía"
    "CE" -> "CE - Cédula de Extranjería"
    "PA" -> "PA - Pasaporte"
    else -> valor
}

// ---------------------------------------------------------------- Flujo

private val RegistroSaver = listSaver<RegistroState, String>(
    save = { r ->
        listOf(
            r.nombreCompleto, r.tipoDocumento, r.numeroDocumento, r.correo, r.fechaNacimiento,
            r.ciudad, r.direccion, r.contrasena, r.confirmarContrasena
        )
    },
    restore = { l ->
        RegistroState(
            nombreCompleto = l[0], tipoDocumento = l[1], numeroDocumento = l[2], correo = l[3],
            fechaNacimiento = l[4], ciudad = l[5], direccion = l[6], contrasena = l[7],
            confirmarContrasena = l[8]
        )
    }
)

/** Registro en una sola pantalla con scroll. Los T&C se leen hasta el final y luego se aceptan. */
@Composable
fun FormularioApp(
    onCancelForm: () -> Unit,
    onFinishForm: (RegistroState) -> Unit
) {
    var registro by rememberSaveable(stateSaver = RegistroSaver) { mutableStateOf(RegistroState()) }
    var termsRead by rememberSaveable { mutableStateOf(false) }      // llegó al final del texto
    var termsAccepted by rememberSaveable { mutableStateOf(false) }  // marcó la casilla
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }
    var errorEvent by remember { mutableIntStateOf(0) }
    val scrollState = rememberScrollState()

    // Sube al inicio cuando aparece un error para que se vea el mensaje.
    LaunchedEffect(errorEvent) { if (errorEvent > 0) scrollState.animateScrollTo(0) }

    BackHandler { onCancelForm() }

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
            ) {
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

                InfoBasicaSection(registro = registro, onChange = { registro = it })

                Spacer(Modifier.height(8.dp))

                TerminosSection(
                    leidos = termsRead,
                    aceptados = termsAccepted,
                    onLeidos = { termsRead = true },
                    onAceptadosChange = { termsAccepted = it }
                )

                Spacer(Modifier.height(16.dp))
                NavigationButtons(
                    onBack = onCancelForm,
                    onNext = {
                        val error = validarRegistro(registro, termsRead, termsAccepted)
                        errorMessage = error
                        if (error != null) errorEvent++ else onFinishForm(registro)
                    },
                    backText = "CANCELAR",
                    nextText = "FINALIZAR"
                )
            }
        }
    }
}

// ---------------------------------------------------------- Validaciones

private fun validarRegistro(r: RegistroState, termsRead: Boolean, termsAccepted: Boolean): String? = when {
    r.nombreCompleto.isBlank() -> "Ingresa tu nombre y apellidos."
    r.tipoDocumento.isBlank() -> "Selecciona tu tipo de documento."
    r.numeroDocumento.isBlank() -> "Ingresa tu número de documento."
    r.correo.isBlank() -> "Ingresa tu correo electrónico."
    !Patterns.EMAIL_ADDRESS.matcher(r.correo.trim()).matches() -> "El correo electrónico no es válido."
    r.fechaNacimiento.isBlank() -> "Selecciona tu fecha de nacimiento."
    edadDe(r.fechaNacimiento) == null -> "La fecha debe tener el formato DD/MM/AAAA y ser válida."
    edadDe(r.fechaNacimiento)!! < 18 -> "Debes ser mayor de edad para registrarte."
    r.ciudad.isBlank() -> "Ingresa tu ciudad."
    r.direccion.isBlank() -> "Ingresa tu dirección."
    r.contrasena.length < 8 -> "La contraseña debe tener al menos 8 caracteres."
    r.contrasena != r.confirmarContrasena -> "Las contraseñas no coinciden."
    !termsRead -> "Debes leer los Términos y Condiciones completos (hasta el final) antes de aceptarlos."
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

/** DD/MM/AAAA -> milisegundos UTC (formato que usa el calendario), o null si no es válida. */
private fun millisDe(fecha: String): Long? {
    if (edadDe(fecha) == null) return null
    val p = fecha.split("/")
    val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    cal.clear()
    cal.set(p[2].toInt(), p[1].toInt() - 1, p[0].toInt())
    return cal.timeInMillis
}

/** Milisegundos UTC del calendario -> DD/MM/AAAA. */
private fun fechaDe(millis: Long): String {
    val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    cal.timeInMillis = millis
    return String.format(
        Locale.US, "%02d/%02d/%04d",
        cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR)
    )
}

// ------------------------------------------------------------- Pantallas

@Composable
private fun InfoBasicaSection(
    registro: RegistroState,
    onChange: (RegistroState) -> Unit
) {
    FormTitle("INFORMACIÓN BASICA")
    FormTextField("Nombre/s y Apellidos", registro.nombreCompleto) { onChange(registro.copy(nombreCompleto = it)) }
    FormDropdown(
        label = "Tipo de documento",
        options = TIPOS_DOCUMENTO,
        selected = registro.tipoDocumento,
        optionLabel = ::nombreTipoDocumento
    ) { onChange(registro.copy(tipoDocumento = it)) }
    FormTextField("Número de documento", registro.numeroDocumento) { onChange(registro.copy(numeroDocumento = it)) }
    FormTextField("Correo electrónico", registro.correo, keyboardType = KeyboardType.Email) {
        onChange(registro.copy(correo = it.trim()))
    }
    FormDateField("Fecha de Nacimiento", registro.fechaNacimiento) {
        onChange(registro.copy(fechaNacimiento = it))
    }
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
}

/**
 * Sección de Términos y Condiciones:
 * 1) el usuario abre y lee el texto hasta el final  ->  [leidos] pasa a true
 * 2) solo entonces se habilita la casilla de aceptar ->  [aceptados]
 */
@Composable
private fun TerminosSection(
    leidos: Boolean,
    aceptados: Boolean,
    onLeidos: () -> Unit,
    onAceptadosChange: (Boolean) -> Unit
) {
    var showTerms by rememberSaveable { mutableStateOf(false) }

    FormTitle("TÉRMINOS Y CONDICIONES")
    Text(
        text = "Para crear tu cuenta debes leer los Términos y Condiciones hasta el final y luego aceptarlos.",
        color = TextNavy,
        fontSize = 14.sp,
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(Modifier.height(12.dp))
    PillButton("LEER TÉRMINOS Y CONDICIONES", { showTerms = true }, Modifier.fillMaxWidth())
    Spacer(Modifier.height(8.dp))
    Text(
        text = if (leidos) "\u2713 Has leído los Términos y Condiciones."
        else "Aún no has leído los Términos y Condiciones completos.",
        color = if (leidos) VerdeLeido else Color.Gray,
        fontSize = 13.sp,
        modifier = Modifier.fillMaxWidth()
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // Sin leer, tocar la casilla abre el texto en vez de marcarla
                if (!leidos) showTerms = true else onAceptadosChange(!aceptados)
            }
            .padding(vertical = 8.dp)
    ) {
        Checkbox(checked = aceptados, onCheckedChange = onAceptadosChange, enabled = leidos)
        Text(
            text = "He leído y acepto los Términos y Condiciones",
            fontWeight = FontWeight.Bold,
            color = if (leidos) TextNavy else Color.Gray
        )
    }

    if (showTerms) {
        TerminosDialog(
            yaLeido = leidos,
            onLeido = onLeidos,
            onClose = { showTerms = false }
        )
    }
}

/** Pantalla completa con el texto. Marca "leído" cuando el usuario llega al final del scroll. */
@Composable
private fun TerminosDialog(
    yaLeido: Boolean,
    onLeido: () -> Unit,
    onClose: () -> Unit
) {
    val scroll = rememberScrollState()
    val llegoAlFinal by remember {
        derivedStateOf { scroll.maxValue != Int.MAX_VALUE && scroll.value >= scroll.maxValue - 16 }
    }
    LaunchedEffect(llegoAlFinal) { if (llegoAlFinal) onLeido() }

    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
            Column(Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing)) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(scroll)
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    TerminosContenido()
                }

                HorizontalDivider()
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (yaLeido || llegoAlFinal) "Ya puedes cerrar y marcar la casilla de aceptación."
                        else "Desplázate hasta el final para poder aceptar.",
                        color = if (yaLeido || llegoAlFinal) VerdeLeido else Color.Gray,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(8.dp))
                    PillButton("CERRAR", onClose, Modifier.fillMaxWidth())
                }
            }
        }
    }
}

@Composable
private fun TerminosContenido() {
    TERMINOS_ENCABEZADO.forEachIndexed { i, linea ->
        Text(
            text = linea,
            fontFamily = FontFamily.Serif,
            fontWeight = if (i < 2) FontWeight.Bold else FontWeight.Normal,
            fontSize = if (i < 2) 15.sp else 11.sp,
            color = TextNavy
        )
    }

    Spacer(Modifier.height(20.dp))
    Text(
        text = "TÉRMINOS Y CONDICIONES",
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        color = Color.Black
    )
    Spacer(Modifier.height(12.dp))
    TERMINOS_DATOS.forEach {
        Text(
            text = it,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
            fontSize = 14.sp,
            color = Color.Black,
            modifier = Modifier.padding(vertical = 3.dp)
        )
    }

    TERMINOS_ARTICULOS.forEach { articulo ->
        Spacer(Modifier.height(18.dp))
        Text(
            text = "Artículo ${articulo.numero}.",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = Color.Black
        )
        Spacer(Modifier.height(4.dp))
        articulo.parrafos.forEach {
            Text(
                text = it,
                fontFamily = FontFamily.Serif,
                fontSize = 14.sp,
                lineHeight = 21.sp,
                textAlign = TextAlign.Justify,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 2.dp)
            )
        }
        articulo.items.forEach {
            Row(Modifier.padding(start = 8.dp, top = 2.dp, bottom = 2.dp)) {
                Text("\u2022", fontSize = 14.sp, color = Color.Black)
                Spacer(Modifier.width(8.dp))
                Text(
                    text = it,
                    fontFamily = FontFamily.Serif,
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    color = Color.Black
                )
            }
        }
    }

    Spacer(Modifier.height(24.dp))
    TERMINOS_PIE.forEach {
        Text(
            text = it,
            fontFamily = FontFamily.Serif,
            fontSize = 11.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
    Spacer(Modifier.height(8.dp))
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

/** Caja de solo lectura que se toca para abrir un menú o un calendario. */
@Composable
private fun SelectorBox(
    text: String,
    placeholder: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .border(1.dp, Color.Black)
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp)
    ) {
        Text(
            text = text.ifEmpty { placeholder },
            color = if (text.isEmpty()) Color.Gray else Color.Black,
            fontSize = 16.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        Icon(icon, contentDescription = null, tint = TextNavy)
    }
}

/** Desplegable: se guarda el valor de [options]; en el menú se muestra [optionLabel]. */
@Composable
fun FormDropdown(
    label: String,
    options: List<String>,
    selected: String,
    placeholder: String = "Selecciona una opción",
    required: Boolean = true,
    optionLabel: (String) -> String = { it },
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
        FormLabel(label, required)
        Box {
            SelectorBox(selected, placeholder, Icons.Default.ArrowDropDown) { expanded = true }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color.White)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(optionLabel(option), color = TextNavy, fontWeight = FontWeight.Bold) },
                        onClick = { onSelected(option); expanded = false }
                    )
                }
            }
        }
    }
}

/** Campo de fecha con calendario (con selector de mes y año). Guarda el valor como DD/MM/AAAA. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormDateField(
    label: String,
    value: String,
    required: Boolean = true,
    onDateSelected: (String) -> Unit
) {
    var show by rememberSaveable { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
        FormLabel(label, required)
        SelectorBox(value, "Selecciona tu fecha", Icons.Default.DateRange) { show = true }
    }

    if (show) {
        val ahora = System.currentTimeMillis()
        val anioActual = Calendar.getInstance().get(Calendar.YEAR)
        val hace18Anios = Calendar.getInstance().apply { add(Calendar.YEAR, -18) }.timeInMillis
        val actual = millisDe(value)

        val state = rememberDatePickerState(
            initialSelectedDateMillis = actual,
            initialDisplayedMonthMillis = actual ?: hace18Anios, // abre cerca de una edad adulta
            yearRange = 1920..anioActual,
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long) = utcTimeMillis <= ahora
                override fun isSelectableYear(year: Int) = year <= anioActual
            }
        )

        DatePickerDialog(
            onDismissRequest = { show = false },
            confirmButton = {
                TextButton(
                    enabled = state.selectedDateMillis != null,
                    onClick = {
                        state.selectedDateMillis?.let { onDateSelected(fechaDe(it)) }
                        show = false
                    }
                ) { Text("ACEPTAR") }
            },
            dismissButton = { TextButton(onClick = { show = false }) { Text("CANCELAR") } }
        ) {
            DatePicker(state = state)
        }
    }
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