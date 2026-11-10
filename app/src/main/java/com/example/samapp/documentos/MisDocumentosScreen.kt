package com.example.samapp.documentos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.samapp.inicio.BottomBarSAM
import com.example.samapp.inicio.TopBarSAM
import com.example.samapp.navigation.NavActions
import com.google.firebase.auth.FirebaseAuth

/** Documentos que el adoptante carga para su proceso de adopción. */
@Composable
fun MisDocumentosScreen(nav: NavActions) {
    val uid = remember { FirebaseAuth.getInstance().currentUser?.uid }

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
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "MIS DOCUMENTOS",
                fontFamily = FontFamily.Monospace,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "Sube los documentos que necesitas presentar en tu proceso de adopción " +
                        "(identidad, comprobante de domicilio, carta de compromiso). " +
                        "Solo tú y los administradores de SAM pueden verlos.",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(20.dp))

                if (uid == null) {
                    Text(
                        text = "Inicia sesión para gestionar tus documentos.",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                } else {
                    DocumentosPanel(
                        titulo = "Documentos cargados",
                        propietarioTipo = TIPO_PROPIETARIO_USUARIO,
                        propietarioId = uid,
                        categorias = CATEGORIAS_DOCUMENTOS_USUARIO,
                        puedeSubir = true,
                        puedeEliminar = true
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
