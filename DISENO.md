# Diseño de The Back Row

Estilo **sala de cine en modo oscuro**: fondo borgoña casi negro, tarjetas borgoña oscuro con bordes dorados finos y dorado solo como detalle (aros, líneas, precios, botones secundarios). La marca visible es **The Back Row** (el proyecto y el código siguen llamándose Última Fila).

## Logo

Sale de `Logo arreglado.png` (emblema circular + «THE BACK ROW»), sin redibujarlo. Archivos en `static/img/`:

| Archivo | Uso |
|---|---|
| `emblema-backrow-128.png` | Navbar (44 px), footer, cabecera del boleto |
| `emblema-backrow-256.png` | Inicio y tarjeta de login/registro |
| `favicon-32.png` | Favicon |
| `apple-touch-icon.png` | Icono al guardar en el celular (180 px) |
| `logo-backrow.png` | Logo completo con texto (uso sobre fondo claro, p. ej. documentos) |

Sobre fondo oscuro el texto de la marca se escribe en HTML con Bebas Neue al lado del emblema, porque el texto del PNG es borgoña oscuro y no contrasta.

## Paleta (variables en `:root` de `static/css/ultimafila.css`)

| Variable | Valor | Uso |
|---|---|---|
| `--uf-fondo` | `#1a0a0d` | Fondo de la página |
| `--uf-tarjeta` | `#2a1015` | Tarjetas, modales, filas de función |
| `--uf-tarjeta-alta` | `#36161d` | Degradado de boletos, filas en hover |
| `--uf-borde` | `#c9a45c` al 38 % | Bordes finos de tarjetas, campos y tablas |
| `--uf-borgona` | `#651823` | Navbar, cabecera de tablas y modales |
| `--uf-borgona-boton` | `#8e2433` | Botón principal (texto blanco) |
| `--uf-borgona-hover` | `#a52d3f` | Hover del botón principal |
| `--uf-dorado` | `#c9a45c` | Detalles: línea bajo la navbar, aros, foco, pantalla, sillas elegidas, botones secundarios |
| `--uf-dorado-claro` | `#e3c58a` | Texto dorado (botón secundario, número de silla, fechas) |
| `--uf-precio` | `#e0c07d` | Precios |
| `--uf-texto` | `#f3e9dc` | Texto principal |
| `--uf-texto-suave` | `#c4b2a3` | Texto secundario y metadatos |
| `--uf-enlace` | `#e8c987` | Enlaces |

Bootstrap usa su modo oscuro (`data-bs-theme="dark"` en `<html>`), y la hoja propia redefine sus variables con esta paleta, así formularios, tablas, modales y paginación quedan iguales en todas las pantallas.

Estados de función: pocas sillas `#e8a33d` con texto `#2b1a00` y borde punteado; agotada `#3d2a2d` tachada. Footer `#12070a`.

## Tipografía

- **Bebas Neue**: marca, `h1`–`h3`, títulos de película, horas de función, pestañas, títulos de modal y número de silla del boleto.
- **Inter** (variable 400–700): todo el texto, formularios, tablas y botones.
- Respaldo: `Oswald, "Arial Narrow", Impact` para la marca y `"Segoe UI", system-ui, Arial` para el texto. Como la app debe funcionar sin internet, las fuentes se guardan en `static/fonts/` (`bebas-neue-400.woff2`, `inter-variable.woff2`) con `@font-face`.
- Título principal con `clamp(2.4rem, 6vw, 3.6rem)`. Sinopsis máximo 46 caracteres por línea.

## Componentes Bootstrap 5

| Componente | Uso en The Back Row |
|---|---|
| Navbar (`navbar-dark`, `sticky-top`) | Fondo `#651823`, borde inferior dorado 2 px, emblema con aro dorado y «The Back Row» en Bebas Neue, opciones según rol |
| Tarjeta de película (`.peli`, fragmento `tarjetaPeli`) | Póster 2:3 grande con esquinas de 0,9 rem; cinta inferior con clasificación y duración; título en Bebas Neue; en hover sube 6 px, se marca el aro dorado y aparece «Comprar boletas». Dos por fila en celular |
| Botones `.btn-uf`, `.btn-outline-uf` y `.btn-hora` | Principal borgoña relleno; secundario y horario con borde y texto dorados (punteado y ámbar si quedan pocas sillas, tachado si está agotada) |
| Nav tabs | Bebas Neue, activa con línea dorada de 3 px |
| Form, form-select | Fondo `#1f0c10`, borde dorado fino; errores con `th:errors` e `.invalid-feedback` |
| Table | Cabecera borgoña con línea dorada, cuerpo `--uf-tarjeta`, contorno redondeado |
| Modal | Fondo de tarjeta, borde dorado, cabecera borgoña |
| Badge | Clasificación, estado de compra y boleta |
| Alert / avisos | Avisos oscuros con borde dorado abajo a la derecha |
| Pagination | Página activa dorada con texto oscuro |

**Mapa de sillas:** sala en penumbra (degradado casi negro con un halo dorado arriba), pantalla dorada curva con brillo y la palabra «PANTALLA», filas con letra y pasillo central. Libre: clara `#efe4d2`. Elegida: dorada con ✓ y brillo. Vendida: apagada `#4a2c31` con ×. Fuera de servicio: rayada. Siempre con leyenda. Sillas de 26×24 px (22×21 px en celular).

**Boleto de cine:** el resumen de sillas, la confirmación (`.boleto`) y el comprobante (`.boleta` con talón) se ven como boletas: borde dorado, líneas punteadas y muescas laterales perforadas. Sin código QR, porque generarlo exige una librería y el stack no permite agregar ninguna; el código de cada boleta va en grande en el talón.

**Afiches:** imagen por enlace (`poster_url`) con `object-fit: cover`; si falla, degradado borgoña con el título en Bebas Neue y marco dorado.

## Accesibilidad

- Contraste mínimo AA (4,5:1 en texto normal). Texto principal `#f3e9dc` sobre `#1a0a0d` ≈ 16:1; secundario `#c4b2a3` ≈ 9:1; dorado claro `#e3c58a` ≈ 11:1. El dorado `#c9a45c` no se usa para texto pequeño sobre borgoña.
- Foco visible en todo elemento interactivo: `outline: 3px solid var(--uf-dorado); outline-offset: 2px`.
- El color nunca es la única señal: horarios agotados van tachados, pocas sillas con borde punteado y las sillas tienen `aria-label` («Silla C4, libre»).
- Toda imagen con `alt`; afiche de respaldo con `role="img"` y `aria-label`.
- Navbar con `aria-label`, botón hamburguesa con `aria-controls` y `aria-expanded`, modales con `aria-labelledby`.
- Zonas que cambian (cartelera filtrada, avisos, resultado de validación de boleta) con `aria-live="polite"`.
- Todos los campos con `<label>` asociado; errores junto al campo.
- Áreas táctiles de al menos 24×24 px. Sin desplazamiento horizontal desde 375 px (NFR-CIN-003).
- Se puede usar todo con teclado, incluido el mapa de sillas.

## Movimiento y animaciones

El movimiento es sutil y moderado. Sirve para dar contexto (qué cambió, qué se puede tocar, qué está cargando) y nunca para decorar. Solo CSS y JavaScript básico, sin librerías de animación.

### Principios

1. **Corto:** ninguna transición dura más de **0,4 s**.
2. **Suave:** las curvas entran rápido y frenan despacio. Sin rebotes, giros ni oscilaciones.
3. **Ligero:** solo se anima `opacity` y `transform` (translate y scale).
4. **Respetuoso:** con «reducir movimiento» activado, los cambios son instantáneos.

### Variables (en `:root`)

| Variable | Valor | Uso |
|---|---|---|
| `--mov-rapida` | 0,15 s | Botones, presión al hacer clic, cambios de color, salida de la cartelera |
| `--mov-media` | 0,25 s | Hover de tarjetas, navbar compacta, sillas, avisos |
| `--mov-lenta` | 0,4 s (máximo) | Entrada de tarjetas, zoom del afiche, modales |
| `--curva-salida` | `cubic-bezier(.2, .7, .2, 1)` | Elementos que **aparecen** |
| `--curva-suave` | `cubic-bezier(.4, 0, .2, 1)` | **Cambios de estado** (hover, color, selección) |

### Qué se anima

| Elemento | Animación | Propiedades | Duración |
|---|---|---|---|
| Tarjetas de película | Entran con desvanecido hacia arriba (12 px), retraso escalonado de 60 ms por tarjeta (máximo 8 pasos) | opacity, transform | 0,4 s |
| Hover de tarjeta | Sube 6 px, se marca la sombra con aro dorado y aparece «Comprar boletas» | transform, opacity | 0,25 s |
| Afiche en hover | Se acerca un poco (escala 1,04) | transform | 0,4 s |
| Botones | Color suave y presión leve al hacer clic (escala 0,97) | color, fondo, transform | 0,15 s |
| Sillas | Cambian de color al elegirlas y hacen un «pop» corto (escala 1,15 y vuelve) | fondo, transform | 0,25 s |
| Navbar | Se compacta al bajar más de 40 px: logo a 0,86 y aparece sombra | padding, transform, opacity | 0,25 s |
| Modales | Desvanecido con deslizamiento corto de 16 px | opacity, transform | 0,4 s |
| Avisos | Aparecen subiendo 10 px y se cierran solos a los 3,5 s | opacity, transform | 0,25 s |
| Pestañas y filtros | La cartelera se desvanece, aparece el esqueleto y entran las tarjetas nuevas | opacity | 0,15 s |
| Esqueleto de carga | Brillo suave de izquierda a derecha | transform | 1,4 s en bucle |

### Excepciones permitidas

- **Color:** en botones y sillas se puede animar `color`, `background-color` y `border-color`.
- **Navbar:** su `padding` cambia, porque es un único elemento fijo arriba.
- **Esqueleto de carga:** único bucle permitido; desaparece cuando llegan los datos.
- **Sombras:** nunca se anima `box-shadow`. La sombra fuerte va en un `::after` y solo cambia su opacidad.

### Qué no se anima

- `width`, `height`, `top`, `left` ni `margin`.
- Textos largos, precios ni datos importantes.
- Nada se mueve solo sin acción del usuario, salvo la entrada inicial y el esqueleto.
- Sin rebotes, giros, parpadeos, parallax ni efectos de escritura.

### Movimiento reducido

```css
@media (prefers-reduced-motion: reduce) {
  *, *::before, *::after {
    animation-duration: .01ms !important;
    animation-iteration-count: 1 !important;
    animation-delay: 0s !important;
    transition-duration: .01ms !important;
    scroll-behavior: auto !important;
  }
  .peli:hover, .peli:focus-within, .peli:hover .afiche img, .peli-cta span, .btn:active, .navbar-uf.compacta .navbar-brand { transform: none; }
  .esqueleto::after { display: none; }
}
```

Así el contenido siempre termina visible, porque las animaciones de entrada acaban de inmediato en su estado final.

### Cómo añadir una animación nueva

1. Usa una variable de duración y una de curva. No escribas tiempos sueltos.
2. Anima solo `opacity` y `transform`, salvo las excepciones.
3. Comprueba que se ve bien con «reducir movimiento» activado y en un celular de 375 px.
