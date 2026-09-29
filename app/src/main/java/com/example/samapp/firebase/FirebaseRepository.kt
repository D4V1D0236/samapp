package com.example.samapp.firebase

import com.example.samapp.model.RegistroState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException


class FirebaseRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun registrarUsuario(
        registro: RegistroState,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val email = registro.correo.trim()
        val password = registro.contrasena

        if (email.isBlank() || password.isBlank()) {
            onError("El correo y la contraseña son obligatorios.")
            return
        }

        if (password.length < 8) {
            onError("La contraseña debe tener al menos 8 caracteres.")
            return
        }

        if (password != registro.confirmarContrasena) {
            onError("Las contraseñas no coinciden.")
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val firebaseUser = authResult.user
                if (firebaseUser == null) {
                    onError("No fue posible crear la cuenta.")
                    return@addOnSuccessListener
                }

                val uid = firebaseUser.uid
                val datosUsuario = hashMapOf(
                    "uid" to uid,
                    "nombreCompleto" to registro.nombreCompleto.trim(),
                    "tipoDocumento" to registro.tipoDocumento.trim(),
                    "numeroDocumento" to registro.numeroDocumento.trim(),
                    "correo" to email,
                    "fechaNacimiento" to registro.fechaNacimiento.trim(),
                    "ciudad" to registro.ciudad.trim(),
                    "direccion" to registro.direccion.trim(),
                    "rolId" to "ADOPTANTE",
                    "rolSolicitado" to registro.rolSolicitado,
                    "estadoCuenta" to "ACTIVA",
                    "terminosAceptados" to true,
                    "tipoVivienda" to registro.tipoVivienda,
                    "personasEnCasa" to registro.personasEnCasa,
                    "hayNinos" to registro.hayNinos,
                    "tieneMascotas" to registro.tieneMascotas,
                    "tieneZonaVerde" to registro.tieneZonaVerde,
                    "edadesNinos" to registro.edadesNinos,
                    "cantidadMascotas" to registro.cantidadMascotas,
                    "tipoMascotas" to registro.tipoMascotas,
                    "mascotasVacunadasEsterilizadas" to registro.mascotasVacunadasEsterilizadas,
                    "motivoAdopcion" to registro.motivoAdopcion,
                    "planesMudanzaViaje" to registro.planesMudanzaViaje,
                    "responsableCuidado" to registro.responsableCuidado,
                    "participacion" to registro.participacion,
                    "creadoEn" to FieldValue.serverTimestamp(),
                    "actualizadoEn" to FieldValue.serverTimestamp()
                )

                db.collection("usuarios")
                    .document(uid)
                    .set(datosUsuario)
                    .addOnSuccessListener {
                        firebaseUser.sendEmailVerification()
                            .addOnSuccessListener {
                                onSuccess()
                            }
                            .addOnFailureListener { error ->
                                onSuccess()
                            }
                    }
                    .addOnFailureListener { error ->
                        firebaseUser.delete()
                            .addOnCompleteListener {
                                onError("La cuenta se creó, pero no se pudo guardar el perfil: ${error.localizedMessage ?: "error de Firestore"}")
                            }
                    }
            }
            .addOnFailureListener { error ->
                onError(mensajeFirebase(error.message))
            }
    }

    fun iniciarSesion(
        email: String,
        password: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val cleanEmail = email.trim()

        if (cleanEmail.isBlank() || password.isBlank()) {
            onError("Ingresa el correo y la contraseña.")
            return
        }

        auth.signInWithEmailAndPassword(cleanEmail, password)
            .addOnSuccessListener { authResult ->
                val user = authResult.user
                if (user == null) {
                    onError("No fue posible iniciar sesión.")
                    return@addOnSuccessListener
                }

                if (!user.isEmailVerified) {
                    auth.signOut()
                    onError("Debes verificar tu correo antes de iniciar sesión.")
                    return@addOnSuccessListener
                }

                db.collection("usuarios")
                    .document(user.uid)
                    .get()
                    .addOnSuccessListener { document ->
                        if (!document.exists()) {
                            onError("No existe el perfil del usuario en la base de datos.")
                            return@addOnSuccessListener
                        }

                        val rol = document.getString("rolId") ?: "ADOPTANTE"
                        onSuccess(rol)
                    }
                    .addOnFailureListener { error ->
                        onError("No se pudo consultar el perfil: ${error.localizedMessage ?: "error de Firestore"}")
                    }
            }
            .addOnFailureListener { error ->
                onError(mensajeLogin(error))
            }
    }

    fun enviarRestablecimientoPassword(
        email: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val cleanEmail = email.trim()
        if (cleanEmail.isBlank()) {
            onError("Ingresa tu correo electrónico.")
            return
        }

        auth.sendPasswordResetEmail(cleanEmail)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { error -> onError(mensajeFirebase(error.message)) }
    }

    fun reenviarVerificacion(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val user = auth.currentUser
        if (user == null) {
            onError("No hay una cuenta pendiente de verificación.")
            return
        }

        user.sendEmailVerification()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { error -> onError(error.localizedMessage ?: "No se pudo reenviar el correo.") }
    }

    fun comprobarVerificacion(
        onVerified: () -> Unit,
        onNotVerified: () -> Unit,
        onError: (String) -> Unit
    ) {
        val user = auth.currentUser
        if (user == null) {
            onError("No hay una sesión activa.")
            return
        }

        user.reload()
            .addOnSuccessListener {
                if (auth.currentUser?.isEmailVerified == true) {
                    onVerified()
                } else {
                    onNotVerified()
                }
            }
            .addOnFailureListener { error -> onError(error.localizedMessage ?: "No se pudo comprobar la verificación.") }
    }

    fun cerrarSesion() {
        auth.signOut()
    }

    private fun mensajeLogin(error: Exception): String = when {
        error is FirebaseAuthInvalidCredentialsException &&
                error.errorCode == "ERROR_INVALID_EMAIL" ->
            "El correo electrónico no tiene un formato válido."
        // Usuario inexistente y contraseña incorrecta usan el mismo mensaje a propósito
        error is FirebaseAuthInvalidUserException ||
                error is FirebaseAuthInvalidCredentialsException ->
            "El correo o la contraseña son incorrectos."
        error is FirebaseNetworkException ->
            "Sin conexión a internet. Revisa tu red e inténtalo de nuevo."
        error is FirebaseTooManyRequestsException ->
            "Demasiados intentos fallidos. Espera unos minutos e inténtalo de nuevo."
        else -> mensajeFirebase(error.message)
    }

    private fun mensajeFirebase(message: String?): String {
        return when {
            message?.contains("The email address is badly formatted", ignoreCase = true) == true -> "El correo electrónico no tiene un formato válido."
            message?.contains("password is invalid", ignoreCase = true) == true -> "El correo o la contraseña son incorrectos."
            message?.contains("no user record", ignoreCase = true) == true -> "No existe una cuenta con ese correo."
            message?.contains("email address is already in use", ignoreCase = true) == true -> "Ese correo ya está registrado."
            else -> message ?: "Ocurrió un error con Firebase."
        }
    }
}
