# Matriz de trazabilidad

Une cada requisito con su responsable, su tarea, el código que lo implementa, la evidencia de prueba, la pantalla y la entidad. Estado al **27 de noviembre de 2026**, sobre `main` más los PR indicados como abiertos.

## Cómo leerla

- **Responsable:** el de `requisitos.md` (**EVGP** = Erika, **YARC** = Yeziel). Por decisión del equipo, Erika implementó también los requisitos de Yeziel.
- **Tarea:** la línea de [`tasks.md`](tasks.md); todas están marcadas.
- **Código:** controlador, repositorio (`Repositorio.método`), plantilla y script. Las rutas están en [`pantallas.md`](pantallas.md).
- **Prueba:**
  - **PR #n**: criterios probados según la descripción del PR (H2, curl o navegador).
  - **PR #n (pasos)**: el PR solo describe cómo probar; no registra resultados.
  - **MySQL**: [`pruebas-mysql.md`](pruebas-mysql.md) (MySQL 8.0.46, con consultas SQL de contraste).
  - **JUnit**: prueba automática de `./mvnw test`.
  - **375 px**: [`revision-375px.md`](revision-375px.md), que llega con el PR #41 (abierto).
- **Entidad:** tablas del [MER](mer.md) que el requisito lee o escribe.
- **PR abiertos a esta fecha:** #41 (revisión a 375 px), #42 (mensajes de validación y detalles menores) y #43 (README). Mientras no se mezclen, los enlaces a `revision-375px.md` y a `README.md` no abren.

## 1. Requisitos funcionales

| REQ | Responsable | Tarea | Código | Prueba | Pantalla | Entidad |
|---|---|---|---|---|---|---|
| 001 Ver cartelera | EVGP | Cartelera paginada | `CarteleraController` · `PeliculaRepository.buscarEnCartelera` · `cartelera.html` (fragmento `tarjetaPeli`) · `cartelera.js` | PR #2 (pasos); 375 px | P-01 | Pelicula, Genero |
| 002 Buscar y filtrar | EVGP | Búsqueda y filtros por género y fecha | `CarteleraController` · `PeliculaRepository.buscarEnCartelera` · `FuncionRepository.inicioDeFuncionesDesde` · `cartelera.js` · `calendario.js` | PR #4 (pasos); 375 px | P-01 | Pelicula, Genero, Funcion |
| 003 Detalle de película | EVGP | Detalle de película con funciones futuras | `PeliculaController` · `PeliculaRepository.buscarConGenero` · `FuncionRepository.buscarFuturasDePelicula` · `BoletaRepository.countByFuncionIdAndEstadoNotAndSillaActivaTrue` · `SillaRepository.countBySalaIdAndActivaTrue` · `pelicula.html` | PR #5 (pasos); PR #39 (sillas libres con una silla vendida y fuera de servicio); 375 px | P-02 | Pelicula, Funcion, Sala, Silla, Boleta |
| 004 Registro de cliente | EVGP | Registro de cliente con Bean Validation y hash con sal | `RegistroController` · `RegistroForm` · `Claves` · `UsuarioRepository.existsByCorreo` · `registro.html` | MySQL (AC-004.1, 004.2, 004.3); 375 px (con errores). PR #7 no registra criterios | P-03 | Usuario |
| 005 Iniciar y cerrar sesión | EVGP | Inicio y cierre de sesión con HttpSession | `LoginController` · `Claves` · `UsuarioRepository.findByCorreo` · `login.html` | PR #39 (cliente va a `/cartelera` al entrar y al salir); MySQL (inicios de sesión de AC-023.1 y 023.2); 375 px. Sin registro: AC-005.2 (clave incorrecta) | P-04 | Usuario |
| 006 Opciones según rol | EVGP | Interceptor por rol y acceso denegado | `InterceptorSesion` · `ConfiguracionWeb` · `AccesoDenegadoController` · `error.html` · `layout.html` (navbar por rol) | PR #10 (AC-006.1, 006.2); MySQL (acceso por rol, NFR-CIN-002) | P-21 | Usuario (enum Rol) |
| 007 Administrar géneros | YARC | CRUD de géneros | `AdminGeneroController` · `GeneroRepository.existeNombre`, `contarPeliculas` · `generos.html` | PR #14 (AC-007.1, 007.2); MySQL (AC-007.1, 007.2) | P-10 | Genero, Pelicula |
| 008 Administrar películas | YARC | CRUD de películas | `AdminPeliculaController` · `PeliculaRepository.buscarParaAdmin` · `FuncionRepository.contarFuturasDePelicula` · `peliculas.html` · `pelicula-form.html` · `admin.js` | PR #15 (AC-008.1, 008.2, 008.3); MySQL (AC-008.3); PR #42 (mensaje de duración vacía) | P-11, P-12 | Pelicula, Genero, Funcion |
| 009 Crear salas con sillas | YARC | Salas con generación de sillas | `AdminSalaController` (`@Transactional`) · `SalaRepository.existsByNombreIgnoreCase` · `SillaRepository.saveAll` · `salas.html` | PR #16 (AC-009.1, 009.2); MySQL (AC-009.1, 009.2); PR #42 (mensajes de filas y sillas) | P-13 | Sala, Silla |
| 010 Sillas fuera de servicio | YARC | Sillas fuera de servicio | `AdminSillaController` · `SillaRepository.findBySalaIdOrderByFilaAscNumeroAsc` · `admin/sillas.html` | PR #17 (AC-010.1, 010.2); MySQL (AC-010.1) | P-14 | Silla, Sala |
| 011 Programar funciones | YARC | Programar funciones con cálculo de fin | `AdminFuncionController` · `FuncionForm` · `FuncionRepository.buscarParaAdmin` · `funciones.html` · `funcion-form.html` | PR #18 (AC-011.1 con 110 min, AC-011.2); MySQL (AC-011.1) | P-15, P-16 | Funcion, Pelicula, Sala |
| 012 Impedir solapes | YARC | Regla de no solapamiento | `AdminFuncionController` · `FuncionRepository.buscarCruzadas` · `funcion-form.html` | PR #19 (AC-012.1, 012.2, 012.3); MySQL (AC-012.1, 012.2, 012.3) | P-16 | Funcion, Sala |
| 013 Mapa de sillas | EVGP | Mapa de sillas con JavaScript: contador, límite y total | `SillasController` · `BoletaRepository.buscarIdsSillasOcupadas` · `SillaRepository.findBySalaIdOrderByFilaAscNumeroAsc` · `sillas.html` · `sillas.js` | PR #11 (AC-013.1, 013.2); MySQL (sillas vendidas en AC-014.2); 375 px (sillas elegidas y límite de 6) | P-05 | Funcion, Sala, Silla, Boleta |
| 014 Compra en línea | EVGP | Compra en línea en una transacción con pago simulado | `CompraController` (`@Transactional`) · `CompraRepository.save`, `existsByCodigo` · `BoletaRepository.saveAll`, `existsByCodigo` · `confirmar.html` · `comprobante.html` | PR #12 (AC-014.1, 014.2); MySQL (AC-014.1, 014.2) | P-05, P-06, P-08 | Compra, Boleta, Funcion, Silla, Usuario |
| 015 Reglas de compra | EVGP | Reglas de compra con bloqueo pesimista | `CompraController.motivoRechazo` · `FuncionRepository.buscarConBloqueo` (`PESSIMISTIC_WRITE`) · `BoletaRepository.buscarIdsSillasOcupadas` | JUnit `CompraControllerTest` (Scenario Outline, 5 casos); PR #13 (AC-015.1, 015.2 en H2); MySQL (AC-015.1 con 2 y 4 sesiones simultáneas, AC-015.2, AC-015.3) | P-06 | Funcion, Boleta, Silla |
| 016 Mis boletas | EVGP | Mis boletas y comprobante | `MisBoletasController` · `BoletaRepository.buscarDeCliente` · `CompraController` (`GET /compras/{codigo}`) · `mis-boletas.html` · `comprobante.html` | PR #25 (AC-016.1, 016.2); MySQL (AC-016.1, 016.2) | P-07, P-08 | Compra, Boleta, Funcion, Silla |
| 017 Cancelar compra | EVGP | Cancelación dentro del plazo | `MisBoletasController.cancelar`, `dentroDelPlazo` (`@Transactional`) · `CompraRepository.buscarPorCodigo` · `BoletaRepository.buscarDeCompra` | JUnit `MisBoletasControllerTest` (5 casos); PR #27 (AC-017.1, 017.2); MySQL (AC-017.1, 017.2) | P-07 | Compra, Boleta, Funcion |
| 018 Venta en taquilla | YARC | Venta en taquilla | `TaquillaController` · `CompraController` (canal `TAQUILLA`) · `BoletaRepository.buscarVentasDelDia` · `taquilla.html` | PR #30 (AC-018.1, 018.2); MySQL (AC-018.1, 018.2 y carrera taquilla contra compra en línea) | P-18, P-05, P-06, P-08 | Compra, Boleta, Funcion, Silla, Usuario |
| 019 Validar ingreso | YARC | Validación de boleta | `AcomodadorController.validar` (`@Transactional`) · `BoletaRepository.buscarParaValidar` (`PESSIMISTIC_WRITE`) · `acomodador.html` | JUnit `AcomodadorControllerTest`; PR #35; MySQL (AC-019.1, 019.2 y 5 rondas con 3 acomodadores simultáneos) | P-19 | Boleta, Funcion, Silla, Sala |
| 020 Rechazar boletas inválidas | YARC | Rechazo de boletas inválidas | `AcomodadorController.motivoRechazoIngreso` · `acomodador.html` | JUnit `AcomodadorControllerTest` (ventana de ingreso, cancelada, usada); PR #36; MySQL (AC-020.1, 020.2, 020.3) | P-19 | Boleta, Funcion |
| 021 Ocupación de función | YARC | Ocupación de función | `AcomodadorController.ocupacion` · `BoletaRepository.buscarVigentesDeFuncion` · `ocupacion.html` · `ultimafila.css` (`.silla.ingresada`) | PR #38; MySQL (AC-021.1, 021.2); 375 px | P-20 | Funcion, Sala, Silla, Boleta |
| 022 Dashboard | YARC | Dashboard | `AdminDashboardController` · `BoletaRepository.contarVendidasEntre`, `sumarIngresoEntre` · `FuncionRepository.buscarPorDia` · `dashboard.html` | PR #37; MySQL (AC-022.2 y las cifras del día contra SQL) | P-09 | Funcion, Boleta, Compra, Sala, Silla |
| 023 Cuentas del personal | EVGP | Usuarios del personal | `AdminUsuarioController` · `PersonalForm` · `Claves` · `UsuarioRepository.findAllByOrderByRolAscNombreCompletoAsc` · `usuarios.html` | PR #28; MySQL (AC-023.1, 023.2) | P-17 | Usuario |
| 024 Errores amigables | YARC | Páginas de error con `@ControllerAdvice` | `ManejadorErrores` · `error.html` · `application.properties` (`server.error.*`) | PR #39 (404 con «Volver al inicio»); PR #42 (aviso duplicado). El error 500 se forzó a mano de forma temporal y no quedó registrado en el repositorio | P-21 | — |
| 025 Página de inicio | EVGP | Landing en `/` con destacadas, funciones de hoy, 3 pasos, salas y footer | `InicioController` · `PeliculaRepository.buscarDestacadas` · `FuncionRepository.buscarPorDia` · `SalaRepository.findByActivaTrueOrderByNombre` · `inicio.html` | PR #3 (pasos de AC-025.1 a 025.6); 375 px | P-22 | Pelicula, Funcion, Sala |

## 2. Reglas de negocio

| BR | Código | Prueba |
|---|---|---|
| BR-CIN-001 Una silla no se vende dos veces | `CompraController.motivoRechazo`, `FuncionRepository.buscarConBloqueo` | MySQL: AC-015.1 y AC-018.2 con carreras de 2 y 4 sesiones; ningún par (función, silla) con más de una boleta vigente. JUnit `CompraControllerTest` |
| BR-CIN-002 Máximo 6 boletas | `CompraController.motivoRechazo` | MySQL: AC-015.2. JUnit `CompraControllerTest` |
| BR-CIN-003 Sin solapes y 15 min de limpieza | `AdminFuncionController`, `FuncionRepository.buscarCruzadas` | MySQL: AC-011.1, AC-012.1 a 012.3 |
| BR-CIN-004 Cancelar hasta 2 h antes | `MisBoletasController.dentroDelPlazo` | MySQL: AC-017.1, 017.2. JUnit `MisBoletasControllerTest` |
| BR-CIN-005 No vender funciones iniciadas o canceladas | `CompraController.motivoRechazo` | MySQL: AC-015.3 (iniciada). JUnit `CompraControllerTest`. Sin prueba registrada para una función cancelada |
| BR-CIN-006 Una validación, desde 30 min antes hasta el fin | `AcomodadorController.motivoRechazoIngreso`, `BoletaRepository.buscarParaValidar` | MySQL: AC-019.2, AC-020.3 y la carrera de 3 acomodadores. JUnit `AcomodadorControllerTest` (límites de −30 y 135 min) |
| BR-CIN-007 No eliminar géneros, películas ni salas con dependencias | `AdminGeneroController`, `AdminPeliculaController` | MySQL: AC-007.2, AC-008.3. **No existe ninguna operación para eliminar salas**, así que la parte de salas no está implementada ni probada |
| BR-CIN-008 Silla fuera de servicio no se vende | `AdminSillaController`, `CompraController` | PR #17 y MySQL: AC-010.1 |

## 3. Requisitos no funcionales

| NFR | Cómo se cubre | Estado |
|---|---|---|
| NFR-CIN-001 Rendimiento (menos de 2 s con 50 películas y 200 funciones) | — | **No medido** |
| NFR-CIN-002 Acceso por rol | `InterceptorSesion`; el comprobante verifica propietario | Probado: PR #10, PR #25 y MySQL (acceso por rol y AC-016.2) |
| NFR-CIN-003 Uso desde 375 px sin desplazamiento horizontal | CSS responsivo (Bootstrap y `ultimafila.css`) | Revisado en 46 vistas (PR #41): ninguna página desborda. Las cinco tablas de administración se desplazan dentro de su contenedor; se acepta como cumplimiento (decisión en el [README](../README.md), PR #43) |
| NFR-CIN-004 Claves con hash y sal | `Claves` (SHA-256, `SecureRandom`) | Probado en MySQL: formato `sal:hash` de 81 caracteres, sin la clave en claro, en el registro y en las cuentas del personal |
| NFR-CIN-005 Dos ventas simultáneas nunca terminan ambas aceptadas | Bloqueo `PESSIMISTIC_WRITE` sobre la función | Probado en MySQL con sesiones independientes en paralelo (10 rondas de 2, 5 de 4, cruce de sillas y taquilla contra compra en línea). No se usaron dos ventanas de navegador reales |
| NFR-CIN-006 Igual con H2 y MySQL | Perfiles `h2` y `mysql` | Probado: pruebas sobre ambos perfiles y arranque de ambos en un clon limpio (PR #43) |
| NFR-CIN-007 Arranca desde un clon limpio siguiendo solo el README | `README.md` | Probado en Windows con H2 y MySQL (PR #43, abierto). No probado en macOS ni Linux |

## 4. Huecos y observaciones

1. **PR que solo describen cómo probar.** Los PR #2, #3, #4 y #5 (REQ-CIN-001, 025, 002 y 003) traen pasos de prueba, no resultados. Los PR #7 y #8 (REQ-CIN-004 y 005) no traen criterios. Estos requisitos se apoyan en la prueba general de MySQL solo en parte (004 y los inicios de sesión).
2. **Criterios sin prueba registrada:** AC-005.2 (clave incorrecta), AC-022.1 con 50 boletas y $ 500.000 (se probó la misma cuenta con otras cifras) y la función cancelada de BR-CIN-005. «La función ya terminó» (AC-020) solo está cubierta por JUnit.
3. **Criterios probados solo en H2:** AC-008.1, 008.2, 010.2, 011.2 y 013.1/013.2 (según la descripción de sus PR); en MySQL se probaron los demás de cada requisito.
4. **Sin probar:** NFR-CIN-001, Aiven, `mvnw` en macOS o Linux, dos ventanas de navegador reales y un teléfono físico.
5. **Cambios transversales sin requisito propio:** zona horaria `America/Bogota`, `DB_SSL_MODE` y el conteo de sillas libres en sillas activas (PR #39); mensajes de validación en español (PR #42, abierto); y los PR de diseño (#20 a #24, #31 y #32), que no cambian el comportamiento de ningún requisito. El calendario de la cartelera (`calendario.js`, `inicioDeFuncionesDesde`) llegó con el rediseño (#31 y #32) y sostiene el filtro por fecha de REQ-CIN-002.
