# Funcionalidades

Las siguientes es una lista de las funcionalidades disponibles para el usuario de la aplicación:

1. Registro, autenticación y gestión de usuarios: Los usuarios podrán crear una cuenta, iniciar y cerrar sesión, recuperar su contraseña y modificar sus datos personales. En esta se manejarán tres roles que corresponden a: adoptante, refugio u hogar de paso y administrador. Cada uno de estos tiene permisos y funcionalidades específicas.
2. Galería de animales en adopción: Se mostrará un catálogo con fotografías e información detallada de cada animal, incluyendo nombre, especie, raza, edad aproximada, sexo, tamaño, estado de salud, vacunas, esterilización, personalidad, necesidades especiales, ubicación e historia del rescate.
3. Buscador y filtros por especie: El usuario podrá encontrar animales aplicando filtros por especie (gatos o perros).
4. Perfil detallado del animal: Cada mascota tendrá una página individual desde la cual se podrá consultar toda su información, conocer el refugio responsable y acceder directamente al formulario de solicitud de adopción.
5. Historial médico básico del animal: Podrá registrarse información sobre vacunas, desparasitación, esterilización, alergias, enfermedades conocidas, tratamientos y recomendaciones veterinarias.
6. Carga y gestión de documentos: Los administradores podrán adjuntar certificados de vacunación, esterilización u otros documentos relevantes, mientras que los adoptantes podrán cargar documentación requerida durante el proceso de adopción. Se admiten archivos PDF, JPG y PNG de hasta 700 KB (las fotos se reducen automáticamente).
7. Historias felices: Los usuarios podrán consultar casos de animales que fueron rescatados y posteriormente adoptados, con su nombre, especie, fecha de adopción y una breve historia.
8. Donaciones a SAM APP: Los usuarios podrán consultar campañas o necesidades publicadas por los administradores, mediante algún apoyo económico. Cada campaña muestra su descripción, su meta y el valor recaudado; además se indican la llave Bre-B y el código QR para donar.
9. Campañas y eventos de adopción: Los administradores publicarán jornadas de adopción, vacunación, esterilización, recolección de alimentos y otras actividades relacionadas con el bienestar animal.
10. Gestión de términos, consentimiento y privacidad: Durante el registro, el usuario deberá aceptar expresamente los términos y condiciones, y las políticas relacionadas con el tratamiento de sus datos personales. El texto debe leerse hasta el final antes de poder marcar la casilla de aceptación.

## Aspectos y restricciones técnicas

### Plataforma y herramientas

- Aplicación nativa para Android, escrita en Kotlin con Jetpack Compose y Material 3. No existe versión para iOS ni web.
- Versión mínima: Android 7.0 (API 24). La aplicación se compila y apunta a la API 37.
- Versiones de referencia: Android Gradle Plugin 9.4.1, Kotlin 2.4.20, Compose BOM 2026.09.00 y Firebase BoM 34.19.0.
- La interfaz está únicamente en español; los textos están escritos en el código (sin soporte multi-idioma).

### Conectividad

- Registro, inicio de sesión, verificación de correo, recuperación de contraseña, campañas de donación y documentos requieren conexión a internet.
- Las pantallas con contenido incluido en la propia aplicación (mascotas, historias felices, eventos, información y términos y condiciones) se muestran sin consultar el servidor.
- La aplicación no conserva la sesión: cada vez que se abre se debe iniciar sesión de nuevo.

### Servicios de Firebase y plan de costos

- Se usan Firebase Authentication (correo y contraseña) y Cloud Firestore, ambos dentro del plan gratuito (Spark), que no requiere tarjeta.
- Spark tiene cuotas diarias de uso de Firestore (a modo de referencia: 1 GiB almacenado, 50.000 lecturas y 20.000 escrituras al día). Los valores vigentes deben consultarse en la página oficial de precios de Firebase.
- **Cloud Storage para Firebase no se utiliza** porque exige el plan de pago Blaze (con tarjeta, aunque conserva un cupo sin costo). Por eso los documentos se guardan dentro de Firestore.

### Carga de documentos

- Formatos permitidos: PDF, JPG y PNG. Tamaño máximo: 700 KB por archivo.
- Las imágenes se corrigen de rotación y se comprimen a JPG hasta cumplir el límite. Un PDF que supere los 700 KB se rechaza y debe comprimirse o subirse como foto.
- Cada archivo se guarda en Base64 en un documento de Firestore, cuyo tamaño máximo es 1 MiB. Esto sirve para el volumen de esta versión, pero no escala: para producción se recomienda migrar a Cloud Storage (plan Blaze).
- El archivo se descarga solo cuando el usuario lo abre, y se muestra con la aplicación del teléfono (visor de PDF o galería) mediante un FileProvider. Se usa el selector de archivos del sistema, por lo que no se piden permisos de almacenamiento.
- Los documentos de un adoptante solo los ven su dueño y los administradores; los certificados de una mascota los ve cualquier usuario con sesión iniciada.

### Seguridad y privacidad

- El acceso a los datos se controla con reglas de Firestore (`firebase/firestore.rules`) según el rol del usuario.
- El registro público siempre crea cuentas con rol `ADOPTANTE`; un usuario no puede cambiar su rol ni el estado de su cuenta. El primer administrador se asigna manualmente desde la consola de Firebase.
- Contraseña de mínimo 8 caracteres, correo verificado obligatorio para iniciar sesión y mayoría de edad (18 años) validada en el registro.
- Los datos personales se tratan conforme a la Ley 1581 de 2012 y la Ley 1266 de 2008. Los términos y condiciones (artículo 8) indican que los datos se conservan máximo seis meses tras cerrar sesión o dejar de usar la aplicación; **esa eliminación automática aún no está implementada** y requiere un proceso programado (por ejemplo Cloud Functions, que exige el plan Blaze) o una depuración manual.
- La llave Bre-B y el QR de donación están escritos en el código de la aplicación; para producción deberían pertenecer a una cuenta de SAM APP S.A.S. y poder cambiarse sin publicar una nueva versión.

### Contenido y administración

- Las mascotas, las historias felices, los flyers de eventos, los términos y condiciones y la información de la aplicación están en el código y los recursos de la aplicación. Cambiarlos exige publicar una nueva versión, y las imágenes aumentan el tamaño de la instalación.
- Los administradores sí gestionan dentro de la aplicación las campañas de donación (publicar, actualizar el valor recaudado y eliminar) y los documentos de las mascotas.
- Aún no existe un panel administrativo ni una pantalla para gestionar usuarios o roles.
- El filtro del catálogo es solo por especie. Los roles de refugio y hogar de paso están definidos en la base de datos, pero todavía no tienen funcionalidades propias.
- Los datos de vivienda, mascotas y responsabilidad se pedirán en el formulario de adopción, que aún no está implementado; ya no hacen parte del registro.

### Notificaciones y mensajería

- No hay notificaciones push. Los únicos correos son los de verificación de cuenta y restablecimiento de contraseña de Firebase Authentication.
- La pantalla de chats es un prototipo con datos de ejemplo y no está conectada al menú.
- El contacto para adoptar se hace por WhatsApp (aplicación externa) desde el botón ADOPTAR; el número está configurado en el código.

### Pruebas

- Las pruebas realizadas hasta ahora son manuales. Quedan pendientes las pruebas automatizadas (unitarias, de interfaz y de reglas de Firestore con el emulador).
