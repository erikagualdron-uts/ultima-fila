# Pruebas de extremo a extremo sobre MySQL

Fecha: 27 de noviembre de 2026. Resultado: **sin fallos de la aplicación**. Todos los criterios probados se cumplen con el perfil `mysql`.

## Entorno

| Elemento | Valor |
|---|---|
| Servidor | MySQL **8.0.46** en Docker (`mysql:8.0`), `utf8mb4`, InnoDB, zona horaria UTC |
| Aplicación | Spring Boot 4.2.0, Java 21, perfil `mysql`, puerto 8095 (el 8093 estaba ocupado por otra instancia) |
| Primer arranque | `DB_USER`, `DB_PASSWORD`, `DDL_AUTO=create`, `DB_INIT=always` (crea las 8 tablas y carga `data.sql`) |
| Credenciales | Solo en variables de entorno; contraseña de prueba generada para un contenedor desechable |
| Cómo se probó | Peticiones HTTP con sesiones independientes (una por usuario) y consultas SQL directas para comprobar el estado real |

Datos semilla cargados: 5 géneros, 10 películas, 3 salas, 124 sillas, 84 funciones y 5 usuarios.

## Resultados por flujo

| Flujo | Criterios | Resultado |
|---|---|---|
| Registro | AC-004.1, 004.2, 004.3 | Cuenta creada con rol CLIENTE y clave `sal:SHA-256` de 81 caracteres sin la clave en claro (NFR-CIN-004); error «Mínimo 8 caracteres»; error «Correo ya registrado» sin cuenta duplicada. Tildes y ñ se guardan bien en utf8mb4 |
| Compra en línea | AC-014.1, 014.2 | Comprobante con 2 boletas y códigos únicos (índice UNIQUE); el otro usuario ve las sillas vendidas |
| Reglas de compra | AC-015.2, 015.3, BR-CIN-001 | «Máximo 6 boletas por compra», «La función ya inició» y «La silla A1 ya fue vendida»; ninguno crea compra ni boletas sueltas |
| Mis boletas | AC-016.1, 016.2 | Cada cliente ve solo sus compras; el comprobante ajeno lleva a acceso denegado |
| Cancelación | AC-017.1, 017.2 | Con la función a más de 2 h: compra y boletas CANCELADAS y la silla se puede volver a vender. A menos de 2 h: «Plazo de cancelación vencido» y sigue PAGADA |
| Taquilla | AC-018.1, 018.2 | Venta en efectivo a nombre del taquillero (sin cliente) y comprobante con botón imprimir; «La silla A1 ya fue vendida»; también rechaza más de 6 sillas y funciones de otro día |
| Validación | AC-019.1, 019.2, AC-020.1 a 020.3 | USADA con hora de ingreso; «Boleta ya usada a las HH:mm»; «Boleta no encontrada»; «Boleta cancelada»; «Aún no se permite el ingreso». Los rechazos no cambian el estado |
| Ocupación | AC-021.1, 021.2 | «15 libres, 15 por ingresar, 10 ingresadas» con 25 boletas y 10 validadas; el mapa pinta 15, 15 y 10 sillas; una función de mañana redirige con aviso |
| Dashboard | AC-022.1, 022.2 | 8 funciones, 32 boletas y $ 328.000, idéntico al cálculo directo en MySQL; la ocupación (25/40) da 63 % |
| Personal | AC-023.1, 023.2 | Acomodador nuevo entra a control de ingreso; taquillero inactivado ve «Cuenta inactiva» y sus ventas siguen en el dashboard |
| Acceso por rol | NFR-CIN-002 | Cliente, taquillero y acomodador reciben acceso denegado en rutas ajenas; el visitante va a login |
| Administración | AC-007.1, 007.2, 008.3, 009.1, 009.2, 010.1, 011.1, 012.1 a 012.3 | Sala 5×8 genera 40 sillas A1 a E8; el cruce de funciones se rechaza y los límites exactos se aceptan; fin = inicio + 118 + 15; géneros y películas con dependencias no se eliminan |

## AC-015.1 y NFR-CIN-005 (compras simultáneas)

No se usaron dos ventanas de navegador: una sola ventana de Chrome comparte las cookies y no permite dos usuarios a la vez. Se usaron **sesiones independientes disparadas en el mismo instante** con `curl --parallel --parallel-immediate`, que es la misma situación que dos navegadores.

| Escenario | Rondas | Resultado |
|---|---|---|
| 2 compradores (Camila y Carlos) por la misma silla | 10 | 10 ganadores y 10 perdedores; una sola boleta por silla; ganó cada uno en rondas distintas |
| 4 sesiones por la misma silla | 5 | 5 ganadores y 15 perdedores; una sola boleta por silla |
| Compras que se cruzan: {s1, s2} contra {s2, s3} | 1 | Gana una sola; la perdedora no deja la silla s3 vendida (todo o nada) |
| Taquilla contra compra en línea por la misma silla | 1 | Gana una sola |
| 3 acomodadores validan la misma boleta | 5 | Un solo «Ingreso permitido» por ronda |

El perdedor ve «La silla B1 ya fue vendida». Al final no existe ningún par (función, silla) con más de una boleta no cancelada. El log de la aplicación no tiene deadlocks ni «Lock wait timeout».

## Integridad y portabilidad (NFR-CIN-006)

- Ninguna compra sin boletas; el total de cada compra es igual a la suma de sus boletas; las compras CANCELADAS no tienen boletas vigentes; ninguna función se solapa en su sala.
- Esquema: 8 tablas InnoDB `utf8mb4`, 9 claves foráneas y únicos en códigos de boleta y compra, correo, nombre de género y de sala, y (sala, fila, número). Los enums se guardan como texto.
- Fechas: se guardan en hora de Colombia (la aplicación fija `America/Bogota` y el conector usa `serverTimezone=America/Bogota`), aunque el contenedor MySQL esté en UTC. «Hoy» y las ventanas de 2 h y 30 min dependen de esto.
- Reinicio con los valores por defecto (`update` y `never`): los conteos son idénticos, sin avisos ni errores, y los inicios de sesión y el dashboard siguen igual.
- Base vacía con los valores por defecto: se crean las 8 tablas pero **sin datos**, porque `DB_INIT=never`. El primer despliegue necesita `DDL_AUTO=create` y `DB_INIT=always`, y después volver a los valores por defecto.
- `DB_SSL_MODE=REQUIRED`: la aplicación conecta cifrada (`TLS_AES_256_GCM_SHA384`). Con el valor por defecto `DISABLED` no hay conexiones cifradas.

## Observaciones (no son fallos de los criterios)

1. En el primer arranque con `DDL_AUTO=create` salen 9 avisos WARN de `alter table … drop foreign key` sobre tablas que aún no existen. Son normales de Hibernate en una base vacía.
2. Un formulario enviado con una codificación que no es UTF-8 produce un 500 («Character decoding failed»). Un navegador siempre envía UTF-8, así que solo afecta a clientes anormales.
3. `DB_INIT=always` carga `data.sql` en cada arranque: debe usarse solo en el primer arranque. No se probó qué ocurre con una base que ya tiene datos.

## Lo que no se probó

- Dos ventanas de navegador reales (ver arriba).
- Aiven. Falta confirmar el certificado y el valor de `DB_SSL_MODE` allí; se hace el domingo con el despliegue.
- Carga con muchos usuarios. NFR-CIN-001 (menos de 2 s) no se midió en esta tanda.
