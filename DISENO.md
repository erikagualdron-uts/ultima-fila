# Diseño de Última Fila

Estilo **clásico de cine de barrio**: fondo crema, borgoña como color principal y dorado solo como detalle. Referencia visual: `ejemplo_cartelera_v4.html` (cartelera, mapa de sillas, avisos y modal de administración).

## Paleta (variables en `:root` de `static/css/ultimafila.css`)

| Variable | Valor | Uso |
|---|---|---|
| `--uf-fondo` | `#f6efe1` | Fondo de la página (crema) |
| `--uf-tarjeta` | `#fffaf0` | Tarjetas, modales, franjas |
| `--uf-borde` | `#e2d3b3` | Bordes de tarjetas y campos |
| `--uf-borgona` | `#651823` | Navbar, botones principales, títulos |
| `--uf-borgona-hover` | `#4a1019` | Hover de botones principales |
| `--uf-dorado` | `#c9a45c` | Solo detalles: línea bajo la navbar, foco, pantalla de la sala |
| `--uf-precio` | `#6e4f14` | Precios |
| `--uf-texto` | `#2b2320` | Texto principal |
| `--uf-texto-suave` | `#5e514a` | Texto secundario y metadatos |

Estados de función: pocas sillas `#e8a33d` con texto `#2b1a00`; agotada `#3d3530` con texto blanco. Footer `#2b2320` con texto `#cbbca6`.

## Tipografía

- **Playfair Display** (600, 700 e itálica 700): títulos `h1`–`h3`, marca de la navbar, pestañas, títulos de modal.
- **Lora** (400, 600 e itálica 400): todo el texto, formularios y tablas.
- Respaldo: `Georgia, serif`. Como la app debe funcionar sin internet, las fuentes se guardan en `static/fonts/` (woff2) con `@font-face`.
- Título principal con `clamp(2rem, 5vw, 3.2rem)`. Sinopsis máximo 46 caracteres por línea.

## Componentes Bootstrap 5

| Componente | Uso en Última Fila |
|---|---|
| Navbar (`navbar-dark`, `sticky-top`) | Fondo borgoña, borde inferior dorado 2 px, marca en Playfair itálica, opciones según rol |
| Card (`.peli`) | Tarjeta de película con afiche 2:3, insignia de clasificación y estado; en celular pasa a horizontal |
| Botones `.btn-uf` y `.btn-hora` | Principal borgoña relleno; horario con borde borgoña (punteado si quedan pocas sillas, tachado si está agotada) |
| Nav tabs | «En cartelera» / «Próximos estrenos», activa con línea borgoña de 3 px |
| Form, form-select | Bordes `--uf-borde`; errores con `th:errors` e `.invalid-feedback` |
| Table | Listados de administración, con búsqueda y paginación |
| Modal | Confirmar borrado o cancelación y formularios de administración |
| Badge | Clasificación, estado de compra y boleta |
| Alert / avisos | Mensajes flash del patrón PRG; avisos oscuros con borde dorado abajo a la derecha |
| Pagination | Cartelera (8 por página) y tablas |

**Landing (P-22, `/`):** misma paleta y componentes que la cartelera, en este orden: hero (marca en Playfair itálica, frase corta, botón `.btn-uf` «Ver cartelera» y botones secundarios «Iniciar sesión» y «Registrarme» con borde borgoña), película destacada y 4 tarjetas `.peli`, franja «Funciones de hoy» con `.chip-funcion`, «Cómo comprar» en 3 pasos con número en círculo borgoña, salas y horarios en tarjetas `--uf-tarjeta`, y footer oscuro con contacto. Movimiento: el hero y las tarjetas entran con la animación de entrada (`--mov-lenta`, escalonada); las secciones de abajo no se animan al hacer scroll. Un solo `h1` en el hero.

**Mapa de sillas:** pantalla dorada curva al frente, filas con letra, pasillo central. Libre: blanca con borde borgoña. Elegida: borgoña. Vendida o fuera de servicio: gris `#cfc4b6`, deshabilitada. Siempre con leyenda. Sillas de 26×24 px (22×21 px en celular).

**Afiches:** imagen por enlace (`poster_url`) con `object-fit: cover`; si falla, degradado con el título en Playfair.

## Accesibilidad

- Contraste mínimo AA (4,5:1 en texto normal). No usar dorado para texto sobre crema.
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
| Hover de tarjeta | Sube 4 px y la sombra se marca más | transform, opacity de la sombra | 0,25 s |
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
  .peli:hover, .peli:hover .afiche img, .btn:active, .navbar.compacta .navbar-brand { transform: none; }
  .esqueleto::after { display: none; }
}
```

Así el contenido siempre termina visible, porque las animaciones de entrada acaban de inmediato en su estado final.

### Cómo añadir una animación nueva

1. Usa una variable de duración y una de curva. No escribas tiempos sueltos.
2. Anima solo `opacity` y `transform`, salvo las excepciones.
3. Comprueba que se ve bien con «reducir movimiento» activado y en un celular de 375 px.
