# SAM APP

Autor(es): Alejandro Guerrero, David Lozano, Shayra Melo.

Desarrollar una aplicación móvil que permita a los usuarios registrarse, conocer información sobre el cuidado de mascotas y acceder al proceso de adopción de animales rescatados mediante un sistema organizado y seguro.
La app móvil de adopción de mascotas está compuesta por cuatro módulos principales que garantizan una experiencia completa y funcional para los usuarios. El módulo de autenticación permite el registro de usuarios mediante un formulario con datos básicos como nombre, edad,  correo, contraseña, dirección y teléfono, además de ofrecer opciones de inicio de sesión, recuperación de contraseña y asignación de roles (usuarios, administrador y refugio). El módulo de adopción brinda una galería de animales disponibles con información detallada (fotos, edad, raza e historia), así como filtros de búsqueda y un formulario de solicitud que permite hacer seguimiento al estado del proceso (en revisión, aprobado o adoptado). El módulo educativo ofrece artículos, vídeos y consejos sobre el cuidado, alimentación, vacunación y bienestar animal, con la opción de comentar o marcar contenido como favorito. Finalmente, el módulo administrativo proporciona un panel para que el administrador pueda gestionar fotos de los animales, revisar y aprobar solicitudes de adopción, y publicar artículos o noticias relacionadas con la adopción y el cuidado de mascotas.
Algunas de las funciones adicionales de este proyecto serán, chat o formulario de contacto con refugios, envío de notificaciones por correo al aprobar solicitudes, sección de “Historias felices” (historias de animales rescatados y dados en adopción), localización de refugios asociados.


## Referencias

- [Ideas iniciales de proyecto](docs/ideas.md)
- [Funcionalidades de la aplicación](docs/funcionalidades.md)
- [Diseño de la interfaz de usuario](docs/ui.md)

p1.0.1

## Firebase

La aplicación usa Firebase Authentication para las cuentas con correo/contraseña y Cloud Firestore para almacenar el perfil del usuario.

Colecciones principales:

- `roles`: catálogo de roles y permisos.
- `usuarios`: un documento por usuario, con ID igual al `uid` de Firebase Authentication.

Roles contemplados: `ADOPTANTE`, `REFUGIO`, `HOGAR_DE_PASO` y `ADMINISTRADOR`.

Por seguridad, cualquier registro nuevo entra como `ADOPTANTE`. Si el usuario solicita actuar como hogar de paso, ese valor se guarda en `rolSolicitado` para que posteriormente un administrador pueda realizar el cambio de rol.

### Configuración

1. Crear un proyecto en Firebase.
2. Registrar una aplicación Android con el paquete `com.example.samapp`.
3. Descargar `google-services.json` y copiarlo dentro de `app/`.
4. En Firebase Authentication activar el proveedor `Email/Password`.
5. Crear Cloud Firestore en producción y publicar `firebase/firestore.rules`.
6. Crear los documentos de `roles` siguiendo `firebase/firestore_seed.json`.
7. Para crear el primer administrador, registrar primero una cuenta desde la aplicación y después cambiar manualmente su campo `rolId` a `ADMINISTRADOR` desde la consola de Firestore.

El archivo `app/google-services.json.example` solamente sirve como referencia. No reemplaza el archivo real descargado desde Firebase.
