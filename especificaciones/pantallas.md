# Inventario de pantallas (22)

Plantillas Thymeleaf con HTML, CSS, JavaScript y Bootstrap, sobre un layout común (navbar según rol, footer y alertas). Los requisitos las citan por código.

| Cód. | Pantalla | Ruta | Rol | Elementos clave | Sprint |
|---|---|---|---|---|---|
| P-01 | Cartelera | `/cartelera` | Público | Buscador, filtros de género y fecha, tarjetas, paginador | 1 |
| P-02 | Detalle de película | `/peliculas/{id}` | Público | Póster, sinopsis, clasificación, funciones futuras con sillas disponibles | 1 |
| P-03 | Registro | `/registro` | Público | Formulario con errores por campo | 1 |
| P-04 | Inicio de sesión | `/login` | Público | Formulario y alerta de credenciales inválidas | 1 |
| P-05 | Selección de sillas | `/funciones/{id}/sillas` | Público, Cliente, Taquillero | Mapa por colores, pantalla al frente, contador y total | 2 |
| P-06 | Confirmar compra | `/compras/confirmar` | Cliente, Taquillero | Resumen de función y sillas, pago simulado o efectivo | 2 |
| P-07 | Mis boletas | `/mis-boletas` | Cliente | Compras con insignia de estado y botón Cancelar | 2 |
| P-08 | Comprobante de compra | `/compras/{codigo}` | Cliente, Taquillero | Datos de función, sillas, códigos de boleta, botón Imprimir | 2 |
| P-09 | Dashboard | `/admin` | Administrador | Métricas del día y ocupación por función | 2 |
| P-10 | Géneros | `/admin/generos` | Administrador | Listado y formulario en la misma pantalla | 1 |
| P-11 | Películas | `/admin/peliculas` | Administrador | Tabla con búsqueda, paginación y acciones con confirmación | 1 |
| P-12 | Formulario de película | `/admin/peliculas/nueva`, `/admin/peliculas/{id}/editar` | Administrador | Campos, selector de género y clasificación | 1 |
| P-13 | Salas | `/admin/salas` | Administrador | Listado y formulario (filas y sillas por fila) | 1 |
| P-14 | Sillas de la sala | `/admin/salas/{id}/sillas` | Administrador | Mapa para activar o marcar sillas fuera de servicio | 1 |
| P-15 | Funciones | `/admin/funciones` | Administrador | Tabla filtrable por fecha y sala | 1 |
| P-16 | Formulario de función | `/admin/funciones/nueva` | Administrador | Película, sala, fecha, hora y precio; alerta de cruce | 1 |
| P-17 | Usuarios | `/admin/usuarios` | Administrador | Filtro por rol, alta de personal, activar/inactivar | 2 |
| P-18 | Taquilla | `/taquilla` | Taquillero | Funciones del día con sillas libres y ventas del día | 2 |
| P-19 | Control de ingreso | `/acomodador` | Acomodador | Campo de código y resultado en verde o rojo | 2 |
| P-20 | Ocupación de función | `/acomodador/funciones/{id}` | Acomodador | Mapa con libres, vendidas e ingresadas y totales | 2 |
| P-21 | Error y acceso denegado | `/error`, `/acceso-denegado` | Todos | Mensaje amigable y botón para volver al inicio | 1 |
| P-22 | Página de inicio (landing) | `/` | Público | Hero con la marca y «Ver cartelera», películas destacadas, funciones de hoy, cómo comprar en 3 pasos, salas y horarios, footer con contacto, botones Iniciar sesión y Registrarme | 1 |

## Rutas protegidas por el interceptor

| Prefijo | Rol permitido |
|---|---|
| `/admin/**` | Administrador |
| `/taquilla/**` | Taquillero |
| `/acomodador/**` | Acomodador |
| `/mis-boletas/**` | Cliente |
| `/compras/**` | Cliente y Taquillero (el cliente solo ve las suyas) |

Sin sesión en una ruta protegida: redirige a `/login`. Con sesión de otro rol: `/acceso-denegado`.

## Destino tras iniciar sesión

Administrador → `/admin` · Taquillero → `/taquilla` · Acomodador → `/acomodador` · Cliente → `/cartelera`. El visitante sin sesión entra por la landing (`/`).

> P-05 en el plan figura como «Público»: el visitante puede ver el mapa, pero para confirmar la compra debe iniciar sesión como Cliente.
