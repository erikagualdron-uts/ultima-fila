# Revisión responsiva a 375 px

Fecha: 27 de noviembre de 2026. Resultado: **ninguna pantalla desborda horizontalmente a 375 px (NFR-CIN-003)**. Se encontraron un defecto en los mensajes de error de validación y varios detalles menores; no se corrigió nada en esta revisión.

## Cómo se revisó

- App con el perfil h2 (H2 en memoria), una sesión por rol (visitante, cliente, taquillero, administrador y acomodador) y datos reales: una compra, una venta de taquilla y una silla fuera de servicio.
- Cada vista se cargó en un `iframe` de 375 px de ancho (la ventana del navegador no baja de ese tamaño) y se midió: desborde horizontal de la página, elementos que se salen del ancho, áreas táctiles menores de 24 px, campos sin `label`, textos cortados con puntos suspensivos e imágenes sin `alt`.
- Se revisaron **46 vistas**: las 22 pantallas con sus estados (sin resultados, aviso de función iniciada, límite de 6 sillas, modales de cancelar y eliminar, páginas 404, 500 y acceso denegado) y los **estados de error de validación** de registro, inicio de sesión, géneros, películas, salas, funciones, usuarios y control de ingreso.
- Además se inspeccionaron a ojo 24 vistas en hojas de contacto de 4 pantallas (parte pública, flujo del cliente, administración, taquilla y acomodador).

## Resultado por pantalla

| Pantalla | Vistas revisadas | Desborde de página | Observaciones |
|---|---|---|---|
| P-22 Landing | 1 | No | — |
| P-01 Cartelera | 3 (página 1, página 2, sin resultados) | No | — |
| P-02 Detalle de película | 1 | No | — |
| P-03 Registro | 2 (normal y con errores) | No | Errores junto a cada campo |
| P-04 Inicio de sesión | 2 (normal y con error) | No | — |
| P-05 Selección de sillas | 6 (visitante, cliente normal, con 3 sillas y con límite, función iniciada, taquillero) | No | Sillas de 22×21 px (ver nota 3) |
| P-06 Confirmar compra o venta | 2 (cliente y taquillero) | No | — |
| P-07 Mis boletas | 2 (lista y modal de cancelar) | No | — |
| P-08 Comprobante | 2 (cliente y taquilla) | No | — |
| P-09 Dashboard | 1 | No | — |
| P-10 Géneros | 2 (normal y con error) | No | Tabla con desplazamiento interno (nota 2) |
| P-11 Películas | 3 (lista, modal eliminar, sin resultados) | No | Tabla con desplazamiento interno |
| P-12 Formulario de película | 3 (nueva, errores, editar) | No | **Defecto 1** |
| P-13 Salas | 2 (normal y con error) | No | **Defecto 1**; tabla con desplazamiento interno |
| P-14 Sillas de la sala | 1 | No | Sillas de 22×21 px |
| P-15 Funciones | 2 (lista y sin resultados) | No | Tabla con desplazamiento interno |
| P-16 Formulario de función | 2 (normal y con errores) | No | — |
| P-17 Usuarios | 2 (normal y con errores) | No | Tabla con desplazamiento interno; nota 5 |
| P-18 Taquilla | 1 | No | Nota 6 |
| P-19 Control de ingreso | 2 (normal y resultado rechazado) | No | — |
| P-20 Ocupación de función | 1 | No | — |
| P-21 Error y acceso denegado | 3 (404, 500, acceso denegado) | No | Nota 7 |

Sin problemas en todas las vistas: **0 campos sin etiqueta, 0 imágenes sin `alt`, 0 textos cortados y 0 elementos fuera del ancho** (salvo los contenedores que se desplazan por diseño).

## Hallazgos

### Defecto

1. **Mensaje técnico en inglés en P-12 y P-13.** Si se deja vacío un campo numérico (Duración en P-12; Filas o Sillas por fila en P-13) y se pulsa Guardar, la pantalla muestra `Failed to convert property value of type 'java.lang.String' to required type 'int' for property 'duracionMin'; For input string: ""`. Ocurre porque los formularios llevan `novalidate`, los campos son `int` sin `required` y no existe un mensaje propio para el error de conversión. Se reproduce desde la interfaz, sin saltarse nada. Propuesta: un mensaje `typeMismatch` en español en un `messages.properties` (por ejemplo «Ingresa un número entero»).

### Decisión pendiente

2. **Tablas de administración con desplazamiento horizontal interno.** La página no desborda, pero cinco tablas se desplazan dentro de su propio contenedor a 375 px y las últimas columnas quedan fuera de vista hasta desplazar: Géneros (Acciones), Películas (Clasificación, Estado y Acciones), Salas (Acciones), Funciones (Fin, Precio y Estado) y Usuarios (Estado y Acción). NFR-CIN-003 dice «sin desplazamiento horizontal»: hay que decidir si el desplazamiento interno de una tabla cuenta como cumplimiento.

### Notas

3. **Sillas de 22×21 px a 375 px.** `DISENO.md` las define así y también pide áreas táctiles de 24×24 px. Con la separación de 4 a 5 px entre sillas se cumple la excepción de espaciado de WCAG 2.2, pero conviene dejarlo registrado.
4. **Casilla «En cartelera» de 16×16 px.** Su etiqueta asociada mide 91×24 px y el bloque completo 303×24 px, así que el área táctil real cumple.
5. **P-17:** «Mínimo 8 caracteres» aparece dos veces bajo el campo Clave (la ayuda y el error).
6. **P-18:** el precio se parte en dos líneas («$» y «10.000»); basta un espacio que no se parta.
7. **Páginas de error:** el mensaje aparece dos veces (párrafo y aviso flotante) y el texto «Esta boleta no lleva a ninguna función» también sale en la página 500.

## Lo que no se revisó

- Anchos menores de 375 px (solo se midió P-21 a 320 px, sin desborde) ni orientación horizontal.
- La revisión visual cubrió 24 de las 46 vistas; las demás se midieron sin mirarlas.
- Navegadores distintos de Chrome ni un teléfono físico.
