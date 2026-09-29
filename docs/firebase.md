# Configuración de Firebase para SAM APP

## 1. Arquitectura

SAM APP utiliza dos servicios de Firebase:

- **Firebase Authentication**: gestiona las cuentas y contraseñas mediante correo electrónico y contraseña.
- **Cloud Firestore**: guarda el perfil del usuario, sus datos de registro y el rol de la cuenta.

La relación es:

```text
Firebase Authentication
        |
        | uid
        v
Firestore / usuarios / {uid}
        |
        +--> rolId
        +--> datos personales
        +--> datos de vivienda
        +--> datos de responsabilidad
        +--> estadoCuenta
```

## 2. Crear el proyecto Firebase

En Firebase Console:

1. Crea un proyecto nuevo para SAM APP.
2. Agrega una aplicación Android.
3. Usa exactamente este package name:

```text
com.example.samapp
```

4. Descarga `google-services.json`.
5. Copia el archivo en:

```text
app/google-services.json
```

El archivo real no se incluye en el repositorio porque pertenece a tu proyecto Firebase. El proyecto contiene `app/google-services.json.example` como referencia.

## 3. Activar Authentication

En Firebase Console abre Authentication y habilita:

```text
Email/Password
```

La aplicación ya implementa:

- registro con correo y contraseña;
- envío de correo de verificación;
- comprobación de la verificación;
- inicio de sesión;
- restablecimiento de contraseña por correo;
- cierre de sesión.

## 4. Crear Firestore

Crea una base de datos de Cloud Firestore.

Luego publica las reglas del archivo:

```text
firebase/firestore.rules
```

También puedes copiar el contenido de ese archivo directamente en la sección Rules de Firestore.

## 5. Colección `roles`

Crea la colección:

```text
roles
```

y los siguientes documentos:

| ID | Nombre | Uso |
|---|---|---|
| `ADOPTANTE` | Adoptante | Consulta mascotas y crea solicitudes |
| `REFUGIO` | Refugio | Gestiona mascotas y solicitudes de su refugio |
| `HOGAR_DE_PASO` | Hogar de Paso | Gestiona animales asignados temporalmente |
| `ADMINISTRADOR` | Administrador | Gestiona la plataforma y los usuarios |

Los permisos sugeridos están en `firebase/firestore_seed.json`.

## 6. Colección `usuarios`

Cada usuario tiene un documento con este patrón:

```text
usuarios/{uid}
```

Ejemplo:

```json
{
  "uid": "uid_generado_por_firebase",
  "nombreCompleto": "Juan Pérez",
  "tipoDocumento": "CC",
  "numeroDocumento": "123456789",
  "correo": "juan@example.com",
  "fechaNacimiento": "2002-05-10",
  "ciudad": "Bogotá",
  "direccion": "Dirección registrada",
  "rolId": "ADOPTANTE",
  "rolSolicitado": "ADOPTANTE",
  "estadoCuenta": "ACTIVA",
  "terminosAceptados": true,
  "tipoVivienda": "Arrendada",
  "personasEnCasa": "3",
  "hayNinos": "No",
  "tieneMascotas": "Si",
  "tieneZonaVerde": "Si",
  "edadesNinos": "",
  "cantidadMascotas": "1",
  "tipoMascotas": "Gato",
  "mascotasVacunadasEsterilizadas": "Si",
  "motivoAdopcion": "Quiero brindar un hogar responsable.",
  "planesMudanzaViaje": "No",
  "responsableCuidado": "Juan Pérez",
  "participacion": "Adoptante"
}
```

Los campos `creadoEn` y `actualizadoEn` son generados por Firestore.

## 7. Cómo funciona el rol

Por seguridad, el registro público **siempre crea `rolId = ADOPTANTE`**.

Si una persona marca "Hogar de Paso" o "Ambas", la aplicación guarda la intención en:

```text
rolSolicitado = HOGAR_DE_PASO
```

El usuario no puede cambiar su propio `rolId` a `ADMINISTRADOR` ni a `REFUGIO` desde la aplicación. Esto evita que una cuenta normal se otorgue privilegios.

## 8. Crear el primer administrador

Después de crear la base:

1. Registra una cuenta desde SAM APP.
2. Verifica su correo.
3. Abre Firestore.
4. Busca `usuarios/{uid}` de esa cuenta.
5. Cambia manualmente:

```text
rolId = ADMINISTRADOR
```

A partir de ahí las reglas permiten que esa cuenta administre roles y usuarios.

## 9. Ejecutar la aplicación

Desde Android Studio:

1. Coloca `google-services.json` dentro de `app/`.
2. Sincroniza Gradle.
3. Ejecuta la aplicación.
4. Registra un usuario.
5. Verifica el correo recibido.
6. Inicia sesión.
7. Comprueba en Firestore que apareció `usuarios/{uid}`.

## 10. Nota sobre las pantallas existentes

La pantalla de verificación original pedía un código. Firebase Authentication para este flujo utiliza un enlace enviado al correo, por lo que la pantalla fue adaptada para:

- reenviar el correo;
- comprobar si el usuario ya está verificado.

La recuperación de contraseña también utiliza el correo de restablecimiento de Firebase, por lo que ya no es necesario implementar una contraseña local en Firestore.
