package com.example.samapp.formulario

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val TextNavy = Color(0xFF1B263B)
val ButtonBlue = Color(0xFF6CA0DC)
val AsteriskRed = Color(0xFFE63946)
val BorderGray = Color(0xFF9E9E9E)

@Composable
fun FormularioApp(
    onCancelForm: () -> Unit,
    onFinishForm: () -> Unit // <-- 1. Recibe la orden de ir a verificación
) {
    var currentStep by remember { mutableIntStateOf(1) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (currentStep) {
                1 -> InfoBasicaScreen(onNext = { currentStep = 2 }, onCancel = onCancelForm)
                2 -> InfoViviendaScreen(onNext = { currentStep = 3 }, onBack = { currentStep = 1 })
                3 -> InfoMascotasNinosScreen(onNext = { currentStep = 4 }, onBack = { currentStep = 2 })
                4 -> InfoResponsabilidadScreen(
                    onFinish = onFinishForm, // <-- 2. Conecta la orden con la última pantalla
                    onBack = { currentStep = 3 }
                )
            }
        }
    }
}

// --- PANTALLA 1: Información Básica ---
@Composable
fun InfoBasicaScreen(onNext: () -> Unit, onCancel: () -> Unit) {
    FormTitle("INFORMACIÓN BASICA")
    FormTextField("Nombre/s y Apellidos")
    FormTextField("Tipo de documento")
    FormTextField("Número de documento")
    FormTextField("Correo electrónico")
    FormTextField("Fecha de Nacimiento")
    FormTextField("Ciudad")
    FormTextField("Dirección")

    Spacer(modifier = Modifier.height(24.dp))
    NavigationButtons(
        onBack = onCancel,
        onNext = onNext,
        backText = "CANCELAR",
        nextText = "SIGUIENTE"
    )
}

// --- PANTALLA 2: Información de Vivienda ---
@Composable
fun InfoViviendaScreen(onNext: () -> Unit, onBack: () -> Unit) {
    FormTitle("INFORMACIÓN DE VIVIENDA")
    FormRadioGroup("Tipo de vivienda", listOf("Propia", "Familiar", "Arrendada", "Otra: _________"))
    FormTextField("¿Cuántas personas viven contigo?", height = 40.dp, modifier = Modifier.fillMaxWidth(0.5f))
    FormRadioGroup("¿Hay niños en casa?", listOf("Si", "No"))
    FormRadioGroup("¿Tienes mascotas?", listOf("Si", "No"))
    FormRadioGroup("¿Tu vivienda tiene patio o zona verde?", listOf("Si", "No"))

    Spacer(modifier = Modifier.height(24.dp))
    NavigationButtons(onBack = onBack, onNext = onNext)
}

// --- PANTALLA 3: Detalles Niños/Mascotas ---
@Composable
fun InfoMascotasNinosScreen(onNext: () -> Unit, onBack: () -> Unit) {
    FormTextField("¿Qué edades tienen los niños? (en\ncaso de que aplique)")
    FormTextField("¿Cuántas mascotas tienes\nactualmente? (en caso de que aplique)")
    FormRadioGroup("¿Qué tipo de mascotas tienes\nactualmente?", listOf("Perro", "Gato", "Otra: _________"), required = false)
    FormRadioGroup("¿Tus mascotas están vacunadas y\nesterilizadas?", listOf("Si", "No", "En proceso"), required = false)

    Spacer(modifier = Modifier.height(24.dp))
    NavigationButtons(onBack = onBack, onNext = onNext)
}

// --- PANTALLA 4: Información de Responsabilidad ---
@Composable
fun InfoResponsabilidadScreen(onFinish: () -> Unit, onBack: () -> Unit) {
    var termsAccepted by remember { mutableStateOf(false) }

    FormTitle("INFORMACIÓN DE RESPONSABILIDAD")
    FormTextField("¿Por qué deseas adoptar? (campo de texto)", height = 100.dp)
    FormTextField("¿Planeas mudarte o viajar?", height = 100.dp)
    FormTextField("¿Quién será el responsable\nprincipal del cuidado del animal?", height = 100.dp)
    FormRadioGroup("¿Qué tipo de participación te\ngustaría tener con nosotros?", listOf("Adoptante", "Hogar de Paso", "Ambas"))

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Checkbox(
            checked = termsAccepted,
            onCheckedChange = { termsAccepted = it }
        )
        Column {
            Text("He leído y aceptado los", fontWeight = FontWeight.Bold, color = TextNavy)
            Text(
                text = "Términos y Condiciones",
                color = ButtonBlue,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { /* Abrir términos */ }
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))
    NavigationButtons(
        onBack = onBack,
        onNext = {
            if (termsAccepted) {
                onFinish() // <-- 3. Solo navega si el Checkbox está activado
            }
        },
        nextText = "FINALIZAR"
    )
}

// ================= COMPONENTES REUTILIZABLES =================

@Composable
fun FormTitle(title: String) {
    Text(
        text = title,
        fontFamily = FontFamily.Monospace,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        modifier = Modifier.padding(bottom = 24.dp, top = 16.dp)
    )
}

@Composable
fun FormLabel(text: String, required: Boolean = true) {
    Text(
        text = buildAnnotatedString {
            append(text)
            if (required) {
                withStyle(style = SpanStyle(color = AsteriskRed, fontSize = 18.sp)) {
                    append(" *")
                }
            }
        },
        color = TextNavy,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun FormTextField(
    label: String,
    modifier: Modifier = Modifier,
    height: androidx.compose.ui.unit.Dp = 40.dp
) {
    var value by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 16.dp)) {
        FormLabel(label)
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(height)
                .border(1.dp, BorderGray)
                .background(Color.White)
        ) {
            TextField(
                value = value,
                onValueChange = { value = it },
                modifier = Modifier.fillMaxSize(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
fun FormRadioGroup(label: String, options: List<String>, required: Boolean = true) {
    var selectedOption by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 16.dp)) {
        FormLabel(label, required)
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .border(1.dp, BorderGray)
                .padding(8.dp)
        ) {
            options.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedOption = option }
                        .padding(vertical = 4.dp)
                ) {
                    RadioButton(
                        selected = (selectedOption == option),
                        onClick = { selectedOption = option },
                        colors = RadioButtonDefaults.colors(selectedColor = TextNavy)
                    )
                    Text(
                        text = option,
                        color = TextNavy,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun NavigationButtons(onBack: (() -> Unit)?, onNext: () -> Unit, backText: String = "ATRAS", nextText: String = "SIGUIENTE") {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (onBack != null) {
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text(text = backText, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Button(
            onClick = onNext,
            colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
        ) {
            Text(text = nextText, fontWeight = FontWeight.Bold)
        }
    }
}