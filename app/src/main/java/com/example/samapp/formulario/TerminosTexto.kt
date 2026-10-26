package com.example.samapp.formulario

/** Texto de los Términos y Condiciones de SAM APP (versión 1.0, vigente desde 01/11/2025). */
const val TERMINOS_VERSION = "1.0"

data class Articulo(
    val numero: Int,
    val parrafos: List<String> = emptyList(),
    val items: List<String> = emptyList()
)

val TERMINOS_ENCABEZADO = listOf(
    "SAM APP",
    "Sistema de Adopción de Mascotas",
    "NIT: 121.323.080-9 | Dirección: Calle 45B #70A Sur 51 Bogotá D.C",
    "Tel: (601) 32434343 | Correo: administracion@samapp.com | DPO: sam@appsuan.edu.co"
)

val TERMINOS_DATOS = listOf(
    "SAM APP - Sistema de Adopción de Mascotas",
    "Versión: 1.0",
    "Fecha de entrada en vigencia: 01 de Noviembre de 2025",
    "Titular del producto (Responsable del tratamiento): SAM APP S.A.S., NIT 121.323.080-9, " +
            "Calle 45B #70A Sur - 51, Bogotá D.C., Colombia, correo de contacto: sam@appsuan.edu.co"
)

val TERMINOS_PIE = listOf(
    "SAM APP S.A.S - Todos los derechos reservados | Bogotá D.C., Colombia",
    "Este documento cumple con la Ley 1581 de 2012 y la Ley 1266 de 2008."
)

val TERMINOS_ARTICULOS = listOf(
    Articulo(1, parrafos = listOf(
        "Al acceder y utilizar la plataforma Sistema de Adopción de Mascotas (SAM), el usuario acepta " +
                "cumplir con los presentes Términos y Condiciones de Uso, así como con las leyes y normativas " +
                "vigentes aplicables. Si el usuario no está de acuerdo con alguna de estas condiciones, deberá " +
                "abstenerse de utilizar el sistema."
    )),
    Articulo(2, parrafos = listOf(
        "Sistema de Adopción de Mascotas (SAM) es una aplicación web diseñada para facilitar el proceso de " +
                "adopción de mascotas rescatadas, brindando información sobre el cuidado responsable de los " +
                "animales y promoviendo la adopción ética y consciente."
    )),
    Articulo(3, items = listOf(
        "Para acceder a ciertas funcionalidades, el usuario deberá registrarse proporcionando datos verídicos y actualizados.",
        "Cualquier usuario puede retractarse de usar la aplicación y cerrar su cuenta.",
        "El usuario es responsable de mantener la confidencialidad de sus credenciales de acceso y de todas las actividades que se realicen bajo su cuenta.",
        "La administración del sistema podrá suspender o eliminar cuentas que hagan uso indebido o fraudulento de la plataforma."
    )),
    Articulo(4, parrafos = listOf(
        "El sistema cuenta con tres tipos de usuarios: usuario común, persona interesada en adoptar una " +
                "mascota, sin importar su estrato socioeconómico, su nivel de estudio siempre y cuando sea mayor " +
                "de edad (18 años). Administrador, responsable del control general del sistema supervisando las " +
                "publicaciones de los refugios, aprueba o rechaza solicitudes de adopción, administra los " +
                "contenidos informativos sobre cuidado animal, y mantiene la seguridad e integridad de los datos " +
                "registrados en la plataforma. Refugio, entidad o persona encargada de brindar un hogar de paso " +
                "(temporal) para las mascotas rescatadas, esta persona debe ser mayor de 20 años y contar con un " +
                "trabajo estable. Cada uno tiene permisos específicos dentro de la plataforma. El uso indebido de " +
                "privilegios o la suplantación de identidad serán causales de suspensión o eliminación de la cuenta."
    )),
    Articulo(5,
        parrafos = listOf("El usuario se compromete a:"),
        items = listOf(
            "Utilizar la plataforma únicamente con fines legítimos y de buena fe.",
            "No publicar, transmitir o almacenar información falsa, ofensiva o inapropiada.",
            "Mantener la veracidad de los datos personales y de contacto proporcionados.",
            "Tratar con respeto y empatía a los centros de adopción, adoptantes y demás usuarios del sistema."
        )
    ),
    Articulo(6, parrafos = listOf(
        "SAM APP (Sistema de Adopción de Mascotas) En posesión del ARTÍCULO 2° de la ley 1266 de 2008, asume " +
                "la responsabilidad y la transparencia de recolectar y tratar los datos suministrados por los " +
                "usuarios sin ningún fin más allá de manejar estadísticas, realizar recomendaciones y movilizar " +
                "información internamente."
    )),
    Articulo(7, parrafos = listOf(
        "La aplicación funciona con el permiso del usuario a notificaciones, almacenamiento, etc."
    )),
    Articulo(8, parrafos = listOf(
        "La aplicación solicita información personal como nombre completo, apellidos, número de documento, " +
                "fecha de nacimiento, correo electrónico, número de teléfono, ciudad y dirección de residencia. " +
                "Además, se recopilan datos sobre la vivienda (tipo de vivienda, número de personas en el hogar, " +
                "presencia de niños, existencia de patio o zona verde, tipo y cantidad de mascotas, y estado de " +
                "vacunación), así como información relacionada con la responsabilidad del adoptante (motivos para " +
                "adoptar, responsable del cuidado del animal, historial previo de mascotas y tipo de participación " +
                "deseada). Toda esta información será utilizada únicamente para fines relacionados con el proceso " +
                "de adopción, seguimiento y contacto con los interesados, garantizando la confidencialidad de los " +
                "datos conforme a la Ley de Protección de Datos Personales. En caso de que el usuario cierre sesión " +
                "o deje de utilizar la aplicación, los datos recopilados serán conservados de manera segura por un " +
                "periodo máximo de seis (6) meses, tras el cual serán eliminados definitivamente de nuestra base de datos."
    )),
    Articulo(9, parrafos = listOf(
        "Esta información será recopilada únicamente para brindar un servicio integral al usuario, desde " +
                "conectar una mascota en adopción con el perfil que mejor se acopla, hasta recomendaciones de " +
                "alimentos, especialistas y medicamentos."
    )),
    Articulo(10, parrafos = listOf(
        "Antes de que el usuario cree un perfil en la aplicación elige entre aceptar o rechazar los términos y condiciones (T&C)."
    )),
    Articulo(11, parrafos = listOf(
        "Realizar recomendaciones a los usuarios de productos o servicios dentro de la aplicación y " +
                "establecer la conexión entre el usuario y una mascota."
    )),
    Articulo(12, parrafos = listOf(
        "El Sistema de Adopción de Mascotas (SAM APP) está protegida bajo la cámara de comercio de Colombia. " +
                "Donde los derechos se reservan a la empresa SAM APP S.A.S, NIT: 121.323.080-9 | Dirección: " +
                "Calle 45B #70A Sur 51 Bogotá D.C"
    )),
    Articulo(13, parrafos = listOf(
        "SAM APP no recopila información sin el consentimiento de los usuarios; no compartirá la información " +
                "recolectada con terceros y se mantendrá al margen de ciertos datos si el usuario lo exige."
    )),
    Articulo(14, parrafos = listOf(
        "SAM APP no defiende ningún acto de violencia o maltrato hacia las mascotas."
    )),
    Articulo(15, parrafos = listOf(
        "SAM APP denunciará ante las autoridades competentes cualquier irregularidad o maltrato efectuado hacia las mascotas adoptadas."
    )),
    Articulo(16,
        parrafos = listOf("SAM APP respalda y asegura el cumplimiento de las siguientes leyes:"),
        items = listOf(
            "Ley 84 de 1989,",
            "Ley 1774 de 2016,",
            "Ley 2455 de 2025. También llamada \u201cLey Ángel\u201d,",
            "Ley 2054 de 2020."
        )
    ),
    Articulo(17, parrafos = listOf(
        "SAM APP respalda y asegura el cumplimiento del Código Penal (modificado por la Ley 1774 de 2016)."
    )),
    Articulo(18, parrafos = listOf(
        "Al incumplir la cláusula que se encuentra en el contrato realizaremos un proceso legal para " +
                "recuperar la mascota adoptada. Adicional a una denuncia en caso de maltrato."
    ))
)

/**
 * Compromisos que tenía el diálogo anterior del registro. No son parte de los T&C del PDF;
 * se conservan aquí para usarlos en la pantalla de adopción.
 */
val COMPROMISOS_ADOPCION = listOf(
    "La persona adoptante debe ser mayor de edad y responsable del sustento económico del hogar.",
    "Se compromete a educar y dedicar tiempo a la mascota.",
    "Se compromete a esterilizar a la mascota después de terminar el esquema de vacunas.",
    "Se compromete a no maltratar de ninguna manera a la mascota.",
    "Se compromete a mantener al día su atención veterinaria, alimentación de calidad, placa y chip de identificación.",
    "Se compromete a vacunar a la mascota (esquema de cachorro) y luego anualmente.",
    "Se compromete a enviar fotos y videos del carnet de vacunas y de la mascota cuando lo requiera el rescatista.",
    "Si incumple lo pactado, se recogerá la mascota en el domicilio con acompañamiento policial."
)