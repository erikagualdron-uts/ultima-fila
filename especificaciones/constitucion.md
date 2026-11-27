# Constitución SDD de Última Fila (Puerta 0)

Fuente: *Plan de Trabajo Última Fila* (13 de noviembre de 2026). Especificación v1.0, autores Erika Viviana Gualdron Parra (EVGP) y Yeziel Armando Ramos Calderón (YARC). Product Owner: docente Julián Barney Jaimes Rincón.

## Contexto

Prototipo web para el **Cine Comunitario Luz de Barrio** (ficticio, 3 salas). Hoy la cartelera está en un tablero, las boletas se venden en talonarios y las sillas se anotan a mano: hay sillas vendidas dos veces, filas largas, funciones cruzadas y ningún control de ingreso.

**Product Goal:** que toda función programada y toda boleta vendida queden registradas mediante JPA, de modo que el cliente consulte la cartelera y compre sus sillas en línea, el taquillero venda sin sillas duplicadas, el acomodador valide el ingreso y el administrador gestione películas, salas y funciones desde pantallas responsivas en entorno local.

## Objetivos medibles

1. El 100 % de las boletas (en línea y taquilla) se registran y ninguna silla se vende dos veces para la misma función.
2. Cualquier visitante conoce cartelera, horarios y sillas disponibles sin ir al cine.
3. El 100 % de los ingresos a sala se validan con el código de la boleta.

## No-objetivos (Won't)

Pasarela de pagos real (el pago es simulado), descuentos, promociones y membresías, confitería, correo electrónico y recuperación de clave, lectura de QR con cámara, exportación de reportes, **API REST o JSON**, **MongoDB** y aplicación móvil.

## Roles del sistema

| Rol | Qué puede hacer |
|---|---|
| Visitante (sin sesión) | Ver la landing, cartelera, detalle, mapa de sillas y registrarse como cliente |
| Administrador | Dashboard; CRUD de géneros y películas; crear salas y marcar sillas fuera de servicio; programar funciones sin solapamiento; crear Taquilleros y Acomodadores; activar o inactivar usuarios |
| Taquillero | Funciones del día; vender en taquilla con el mapa de sillas; imprimir comprobante; ver sus ventas del día |
| Acomodador | Validar el código de cada boleta al ingreso; ver la ocupación de las funciones del día |
| Cliente | Registrarse e iniciar sesión; filtrar cartelera; comprar en línea con pago simulado; ver sus boletas; cancelar dentro del plazo |

## Reglas arquitectónicas

1. Java 21 + Spring Boot (Spring Web MVC) con Maven en Spring Tools for Eclipse, puerto **8093**.
2. Solo 7 dependencias: Spring Web, Thymeleaf, Spring Data JPA, H2, MySQL Driver, DevTools, Validation. Bootstrap como archivos estáticos.
   *Nota (Puerta del cambio):* Spring Boot 4 separa la consola web de H2 en el módulo `spring-boot-h2console`. Es un complemento de la dependencia H2 (la consola es la herramienta de consulta del perfil `h2`), no una función nueva; no cuenta como octava dependencia.
3. `@Controller` con vistas Thymeleaf y redirecciones PRG. **Sin API REST.**
4. JPA con H2 (Sprint 1) y MySQL (Sprint 2) cambiando solo el perfil (`application-h2.properties`, `application-mysql.properties`).
5. Todas las entidades con `id` Long autoincremental (`GenerationType.IDENTITY`).
6. Paquetes `controller`, `repository`, `model` y `config`. **Sin capa service.** Reglas de negocio en controladores con `@Transactional`.
7. Compra con `@Transactional` y bloqueo pesimista (`@Lock`) sobre la función.
8. Sesión con `HttpSession` + `HandlerInterceptor` por rol. Claves SHA-256 con sal por usuario (`MessageDigest`, `SecureRandom`).
9. Errores 404 y 500 centralizados con `@ControllerAdvice`.
10. Código en GitHub: `main` protegida, una rama por requisito (`feature/REQ-CIN-014`) y pull request revisado por el otro desarrollador.
11. **Sin MongoDB.**

## Principios SDD

- La especificación aprobada es la fuente de verdad. Nada se codifica sin su requisito EARS y criterios Given/When/Then.
- Nivel *Spec-anchored*: especificación y código evolucionan juntos. Si el comportamiento cambia, primero cambia la especificación (Puerta del cambio).
- Dudas marcadas como `[NECESITA ACLARACIÓN]` con un supuesto de trabajo; nunca se adivinan.

| Fase | Artefacto | Punto de control |
|---|---|---|
| 0. Constitución | Este archivo | Puerta 0 |
| 1-2. Especificar y clarificar | Roles, `mer.md`, `pantallas.md`, preguntas abiertas | — |
| 3. Requisitos | `requisitos.md` (25 REQ, NFR, BR) | Puerta 1 (PO) |
| 4. Plan técnico | Mapeo JPA, perfiles, repositorios, rutas y plantillas | Puerta 2 |
| 5. Tareas | `tasks.md` | Puerta 3 |
| 6-7. Implementar y validar | PR con revisión cruzada, pruebas por pantalla, matriz de trazabilidad | Revisión de código |
| 8. Evolucionar | Ajustes primero en la especificación | Puerta del cambio |

## Datos semilla (`data.sql`, compatible H2 y MySQL)

5 géneros, 10 películas, 3 salas con sus sillas, funciones de dos semanas, 1 administrador, 1 taquillero, 1 acomodador y 2 clientes. Todo ficticio.

## Definiciones

- **Definition of Ready:** patrón EARS, al menos dos criterios Given/When/Then binarios, pantalla, prioridad MoSCoW, responsable y ninguna duda `[NECESITA ACLARACIÓN]` pendiente.
- **Definition of Done:** criterios verificados en local, PR aprobado por el compañero, criterios aprobados por el PO, pantalla correcta desde 375 px, código unido a `main` y matriz de trazabilidad actualizada.
