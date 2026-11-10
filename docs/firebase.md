# Configuración de Firebase para SAM APP

## 1. Arquitectura

SAM APP utiliza dos servicios de Firebase:

- **Firebase Authentication**: gestiona las cuentas y contraseñas mediante correo electrónico y contraseña.
- **Cloud Firestore**: guarda el perfil del usuario y su rol, las campañas de donación y los documentos cargados.

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
  "fechaNacimiento": "10/05/2002",
  "ciudad": "Bogotá",
  "direccion": "Dirección registrada",
  "rolId": "ADOPTANTE",
  "estadoCuenta": "ACTIVA",
  "terminosAceptados": true
}
```

`tipoDocumento` toma uno de estos valores: `CC` (cédula de ciudadanía), `CE` (cédula de extranjería) o `PA` (pasaporte). La fecha de nacimiento se guarda como `DD/MM/AAAA`.

Los datos de vivienda, mascotas y responsabilidad del adoptante ya no se piden en el registro: se recogerán en el formulario de adopción y se agregarán a este mismo documento.

Los campos `creadoEn` y `actualizadoEn` son generados por Firestore.

## 7. Cómo funciona el rol

Por seguridad, el registro público **siempre crea `rolId = ADOPTANTE`**.

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

Con rol `ADMINISTRADOR` la aplicación habilita:

- publicar, actualizar el valor recaudado y eliminar **campañas de donación**;
- adjuntar y eliminar **documentos de las mascotas** (certificados de vacunación, esterilización, etc.).

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

## 11. Colección `campanas`

Campañas o necesidades de donación publicadas por los administradores:

```text
campanas/{id}
```

```json
{
  "titulo": "Alimento para el mes",
  "descripcion": "Necesitamos concentrado para 20 perros y 15 gatos.",
  "categoria": "Alimento",
  "meta": 1500000,
  "recaudado": 350000,
  "creadoPor": "uid_del_administrador",
  "creadoEn": "(fecha asignada por Firestore)"
}
```

Los montos están en pesos colombianos. Cualquier usuario con sesión iniciada puede leerlas; solo un administrador puede crearlas, modificarlas o eliminarlas. La pantalla de Donaciones se actualiza en tiempo real.

## 12. Colección `documentos`

Documentos de mascotas (cargados por administradores) y de adoptantes (cargados por cada adoptante):

```text
documentos/{id}                      -> datos del documento
documentos/{id}/contenido/archivo    -> el archivo en Base64
```

```json
{
  "propietarioTipo": "MASCOTA",
  "propietarioId": "gato_tigre",
  "categoria": "Certificado de vacunación",
  "nombreArchivo": "vacunas_tigre.pdf",
  "mimeType": "application/pdf",
  "tamanoBytes": 215040,
  "subidoPor": "uid_del_administrador",
  "creadoEn": "(fecha asignada por Firestore)"
}
```

- `propietarioTipo` es `MASCOTA` o `USUARIO`. En una mascota, `propietarioId` es el identificador de la mascota (tipo y nombre, por ejemplo `perro_nala`); en un usuario es su `uid`.
- El archivo va en un documento aparte (`contenido/archivo`, campo `datos`) para que listar los documentos no descargue los archivos.
- Tamaño máximo: 700 KB por archivo. Formatos: PDF, JPG y PNG.
- Los certificados de mascotas los puede ver cualquier usuario con sesión; los documentos de un adoptante, solo su dueño y los administradores.

### ¿Por qué no se usa Cloud Storage?

Cloud Storage para Firebase exige el plan de pago **Blaze** (se vincula una cuenta de facturación, aunque mantiene un cupo sin costo). Para que el proyecto funcione en el plan gratuito, los archivos se guardan en Firestore. Si más adelante se activa Blaze, conviene mover los archivos a Cloud Storage y dejar en Firestore solo los datos del documento; el cambio está concentrado en la clase `DocumentosRepository`.

## 13. Publicar las reglas

Cada vez que cambie el archivo `firebase/firestore.rules`, cópialo en Firestore → **Rules** y pulsa **Publicar**. Las reglas actuales incluyen `usuarios`, `roles`, `campanas` y `documentos`.
