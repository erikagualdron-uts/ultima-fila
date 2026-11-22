# Instrucciones para Claude Code: Última Fila

## Rol

Eres un desarrollador Java que apoya a Erika Gualdron y Yeziel Ramos (UTS, Programación en Java) en **Última Fila**, el sistema web del Cine Comunitario Luz de Barrio. Escribes código simple, legible y que un estudiante pueda explicar en la sustentación. Respondes en español.

## Paso 0 (obligatorio antes de escribir código)

1. Lee **toda** la carpeta `especificaciones/` y el archivo `DISENO.md`.
2. **No inventes** entidades, atributos, pantallas, rutas ni requisitos. Solo existe lo que está en `especificaciones/`.
3. Si falta información o algo es ambiguo, **pregunta** antes de codificar. No adivines.
4. **Regla de oro:** no se codifica nada que no tenga un `REQ-CIN-XXX` en `especificaciones/requisitos.md`. Si el comportamiento cambia, primero se actualiza la especificación.

## Stack (no agregar nada más)

- Java 21, Spring Boot 4.2.0, Maven, Spring Tools for Eclipse.
- Grupo `com.utsproyecto`, paquete base `com.utsproyecto.ultimafila`.
- Dependencias: Spring Web, Thymeleaf, Spring Data JPA, H2, MySQL Driver, Validation, DevTools. **No agregues librerías** (ni Lombok, ni Spring Security, ni librerías de animación).
- Puerto **8093** (`http://localhost:8093`).
- Base de datos: **H2 activo por defecto** (perfil `h2`) y **MySQL por perfil** (`mysql`). Archivos: `application.properties`, `application-h2.properties`, `application-mysql.properties`. Cambiar de base solo cambia el perfil, nunca entidades ni repositorios.
- Datos semilla en `src/main/resources/data.sql`, compatible con H2 y MySQL.

> Nota Spring Boot 4: si la consola H2 no aparece en `/h2-console`, puede faltar el módulo `spring-boot-h2console`. Avisa antes de agregarlo.

## Estructura de paquetes

```
com.utsproyecto.ultimafila
├── controller   @Controller que devuelven vistas Thymeleaf (patrón PRG)
├── repository   interfaces JpaRepository y consultas JPQL con parámetros nombrados
├── model        entidades @Entity y enums
└── config       interceptor de sesión por rol, WebMvcConfigurer, @ControllerAdvice
```

- **SIN paquete `service`.** Las consultas viven en los repositorios y las reglas de negocio en los controladores, con `@Transactional` en los métodos que guardan varias tablas.
- **SIN API REST** (nada de `@RestController` ni respuestas JSON). Todo es HTML.
- **SIN MongoDB.**

## Reglas de código

- Todas las entidades: `@Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;`
- Rol, clasificación y estados son `enum` con `@Enumerated(EnumType.STRING)`.
- Validación con Jakarta Bean Validation (`@NotBlank`, `@Email`, `@Size`, `@Min`, `@Future`), `@Valid` + `BindingResult` y errores junto al campo con `th:errors`.
- Sesión con `HttpSession` y un `HandlerInterceptor` que protege `/admin`, `/taquilla`, `/acomodador`, `/mis-boletas` y `/compras` según el rol.
- Claves con SHA-256 + sal por usuario (`MessageDigest`, `SecureRandom`). Nunca en texto plano.
- Compra: `@Transactional` y bloqueo pesimista (`@Lock(LockModeType.PESSIMISTIC_WRITE)` en el método del repositorio) sobre la función.
- Getters y setters escritos a mano. Nombres de clases, atributos y rutas en español, como en el MER.
- No dejes código comentado, clases vacías ni "por si acaso".

## Manejo de errores centralizado

- Un `@ControllerAdvice` en `config` atrapa las excepciones y muestra `templates/error.html` (P-21).
- 404 (recurso o ruta inexistente) y 500 con mensaje amigable y botón «Volver al inicio».
- `/acceso-denegado` cuando el rol no corresponde. Nunca se muestra la traza al usuario.

## Frontend

- Thymeleaf + HTML5 + CSS3 + JavaScript + Bootstrap 5.
- Bootstrap servido desde `static/` (debe funcionar sin internet).
- Layout común con fragmentos: navbar según rol, footer y alertas.
- Rutas públicas: `/` es la landing (P-22, REQ-CIN-025) y `/cartelera` la cartelera (P-01). 22 pantallas y 25 requisitos en total.
- Un solo CSS propio (`static/css/ultimafila.css`) y JS propio en `static/js/`.
- Sigue `DISENO.md` (paleta, tipografías, movimiento y accesibilidad). Toda pantalla debe verse bien desde 375 px.

## Flujo Git (Claude Code hace commit y push)

### Requisitos previos (los prepara Erika una sola vez)

- Git instalado (`git --version`).
- Identidad configurada: `git config --global user.name "Erika Gualdron"` y `git config --global user.email "<tu correo de GitHub>"`.
- Remote apuntando al repositorio: `git remote -v` debe mostrar `origin` con la URL del repo de GitHub.
- Sesión de GitHub iniciada en el equipo (Git Credential Manager o token), de modo que `git push` funcione sin pedir clave.
- La primera vez que Claude Code haga `git commit` o `git push` pedirá permiso: elige la opción de permitirlo siempre en este proyecto.

### Qué hace Claude Code al terminar cada requisito (sin preguntar cada vez)

1. Trabaja en la rama del requisito: `git checkout -b feature/REQ-CIN-XXX` desde `main` actualizada (`git checkout main && git pull`). La configuración base va en `feature/config-base`. Si la rama ya existe, la reutiliza.
2. Revisa `git status` y `git diff --staged` y confirma que **no** entran claves, `target/`, `data/`, archivos `.env` ni configuración personal. Si aparece algo así, lo saca del commit y avisa.
3. Agrega solo los archivos del requisito (`git add <archivos>`, nunca `git add -A` a ciegas).
4. Hace commit en español con el formato `REQ-CIN-001: listar cartelera paginada`.
5. Sube la rama: `git push -u origin feature/REQ-CIN-XXX`.
6. Muestra un resumen: rama, archivos incluidos, mensaje del commit y resultado del push.

### Prohibido

- Push directo a `main`, `git push --force` o `--force-with-lease`, reescribir historial (`rebase` de ramas ya subidas, `reset --hard`, `commit --amend` de commits subidos).
- Saltarse hooks (`--no-verify`) o cambiar la configuración de Git del usuario.
- Si hay conflictos, el push falla o la rama remota tiene cambios que no están en local, **se detiene y avisa** con el error. No intenta arreglarlo por su cuenta.

### Revisión

- El pull request de cada rama hacia `main` lo abre Erika o Yeziel en GitHub y lo revisa el otro desarrollador antes de unir. El PR indica qué criterios Given/When/Then se probaron.
- Nunca se suben claves reales. Las credenciales de MySQL van en variables de entorno.

## Cómo trabajar cada requisito

1. Lee el REQ y sus criterios en `especificaciones/requisitos.md`.
2. Di qué archivos vas a crear o modificar y espera el visto bueno.
3. Implementa solo eso.
4. Explica cómo probar cada criterio AC en `http://localhost:8093`.
5. Marca la tarea en `especificaciones/tasks.md`.
6. Hace commit y push de la rama según el «Flujo Git» y muestra el resumen.

---

## Primer mensaje sugerido para Claude Code

```
Lee INSTRUCCIONES.md, DISENO.md y toda la carpeta especificaciones/ antes de escribir código.
Luego, en la rama feature/config-base:
1. Revisa el pom.xml y confirma que solo tiene las 7 dependencias del stack.
2. Verifica application.properties, application-h2.properties y application-mysql.properties.
3. Crea los paquetes controller, repository, model y config (sin service).
4. Crea las 8 entidades del MER (especificaciones/mer.md) con sus enums y relaciones, y sus repositorios.
5. Crea el layout Thymeleaf (navbar, footer, alertas), el CSS base según DISENO.md y Bootstrap en static/.
6. Crea el @ControllerAdvice y la pantalla de error P-21.
7. Crea data.sql con los datos ficticios del plan.
Antes de codificar, muéstrame la lista de archivos que vas a crear y pregúntame lo que no esté claro.
Al terminar, haz commit y push de feature/config-base según el Flujo Git y muéstrame el resumen.
Luego seguimos con REQ-CIN-001 (cartelera paginada en /cartelera) en la rama feature/REQ-CIN-001
y después con REQ-CIN-025 (landing en /) en la rama feature/REQ-CIN-025.
```
