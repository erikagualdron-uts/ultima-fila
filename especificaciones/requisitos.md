# Requisitos de Última Fila (Especificación v1.0)

25 requisitos funcionales (REQ-CIN-001 a 025): 13 en el Sprint 1 (Erika 7, Yeziel 6) y 12 en el Sprint 2 (6 cada uno). REQ-CIN-025 (landing) se agregó después del plan original. Cada uno tiene historia, requisito **EARS** (Evento *Cuando*, Estado *Mientras*, No deseado *Si… entonces*, Opcional *Donde* o Ubicuo) y criterios **Given/When/Then** (AC). Prioridad MoSCoW: **M** = Must, **S** = Should. **EVGP** = Erika, **YARC** = Yeziel. Quien no implementa, revisa el PR.

---

## Sprint 1 (16 al 20 de noviembre)

### REQ-CIN-001 · Ver cartelera · EVGP · P-01 · M
**Historia:** Como visitante quiero ver la cartelera para saber qué películas se proyectan.
**EARS (Evento):** Cuando un visitante abra la cartelera (`/cartelera`), el sistema deberá mostrar las películas en cartelera paginadas (8 por página) con póster, título, género, clasificación y duración.
- **AC-001.1** Given 10 películas en cartelera When el visitante abre la cartelera Then ve 8 películas And un enlace a la página 2.
- **AC-001.2** Given una película marcada fuera de cartelera When abre la cartelera Then no la ve.

### REQ-CIN-002 · Buscar y filtrar cartelera · EVGP · P-01 · S
**Historia:** Como visitante quiero buscar y filtrar la cartelera por género y fecha para encontrar una función que me sirva.
**EARS (Evento):** Cuando el visitante ingrese un texto o elija un género o una fecha, el sistema deberá listar solo las películas que coincidan y que tengan funciones en esa fecha.
- **AC-002.1** Given el género «Animación» elegido When filtra por el sábado 28 de noviembre Then solo ve películas animadas con funciones ese día.
- **AC-002.2** Given un texto sin coincidencias When busca Then ve el mensaje «No se encontraron películas».

### REQ-CIN-003 · Detalle de película · EVGP · P-02 · M
**Historia:** Como visitante quiero ver el detalle de una película con sus funciones para elegir horario y sala.
**EARS (Evento):** Cuando el visitante seleccione una película, el sistema deberá mostrar su sinopsis y sus funciones futuras con fecha, hora, sala, precio y sillas disponibles.
- **AC-003.1** Given una película con 2 funciones futuras y 1 pasada When abre su ficha Then ve solo las 2 funciones futuras And las sillas disponibles de cada una.
- **AC-003.2** Given una película que no existe When intenta abrir su ficha Then ve la pantalla de error 404.

### REQ-CIN-004 · Registro de cliente · EVGP · P-03 · M
**Historia:** Como visitante quiero registrarme como cliente para comprar boletas en línea.
**EARS (Evento):** Cuando el visitante envíe nombre, documento, correo no registrado y clave de mínimo 8 caracteres, el sistema deberá crear su cuenta con rol Cliente y la clave cifrada (NFR-CIN-004).
- **AC-004.1** Given datos válidos When envía el registro Then la cuenta queda creada con rol Cliente And pasa a la pantalla de inicio de sesión.
- **AC-004.2** Given una clave de 7 caracteres When envía el registro Then ve el error «Mínimo 8 caracteres» junto al campo.
- **AC-004.3** Given un correo ya registrado When envía el registro Then ve «Correo ya registrado» And no se crea otra cuenta.

### REQ-CIN-005 · Iniciar y cerrar sesión · EVGP · P-04 · M
**Historia:** Como usuario quiero iniciar y cerrar sesión para acceder a las opciones de mi rol.
**EARS (Evento):** Cuando el usuario envíe credenciales válidas de una cuenta activa, el sistema deberá iniciar su sesión y llevarlo al panel de su rol: Administrador al dashboard, Taquillero a taquilla, Acomodador a control de ingreso y Cliente a la cartelera.
- **AC-005.1** Given un taquillero activo When inicia sesión Then llega a la pantalla de taquilla.
- **AC-005.2** Given una clave incorrecta When inicia sesión Then permanece en inicio de sesión And ve «Credenciales inválidas».
- **AC-005.3** Given una sesión iniciada When pulsa Salir Then la sesión se cierra And vuelve a la cartelera.

### REQ-CIN-006 · Opciones según rol · EVGP · P-21 · M
**Historia:** Como usuario quiero ver solo las opciones de mi rol para no entrar a funciones que no me corresponden.
**EARS (Estado):** Mientras exista una sesión activa, el sistema deberá mostrar y permitir solo las opciones y pantallas del rol del usuario (NFR-CIN-002).
- **AC-006.1** Given un acomodador autenticado When intenta abrir la gestión de funciones Then ve la pantalla de acceso denegado.
- **AC-006.2** Given un visitante sin sesión When intenta abrir «Mis boletas» Then es llevado a inicio de sesión.

### REQ-CIN-007 · Administrar géneros · YARC · P-10 · S
**Historia:** Como administrador quiero administrar los géneros para clasificar la cartelera.
**EARS (Evento):** Cuando el administrador guarde o elimine un género, el sistema deberá aplicar el cambio, salvo que el nombre esté vacío o repetido o el género tenga películas (BR-CIN-007).
- **AC-007.1** Given el género «Documental» sin películas When lo elimina Then desaparece del listado.
- **AC-007.2** Given un género con 1 película When intenta eliminarlo Then ve una alerta And el género permanece.

### REQ-CIN-008 · Administrar películas · YARC · P-11, P-12 · M
**Historia:** Como administrador quiero administrar las películas para mantener la cartelera actualizada.
**EARS (Evento):** Cuando el administrador guarde o elimine una película, el sistema deberá aplicar el cambio, salvo que falten datos, la duración sea menor de 1 minuto o la película tenga funciones futuras (BR-CIN-007).
- **AC-008.1** Given una película con datos completos y «En cartelera» marcado When la guarda Then aparece en el listado And en la cartelera pública.
- **AC-008.2** Given una duración de 0 minutos When guarda la película Then ve el error junto al campo And no se crea.
- **AC-008.3** Given una película con una función futura When intenta eliminarla Then ve una alerta And la película permanece.

### REQ-CIN-009 · Crear salas con sillas · YARC · P-13 · M
**Historia:** Como administrador quiero crear salas indicando filas y sillas por fila para que el sistema genere el mapa de sillas.
**EARS (Evento):** Cuando el administrador guarde una sala nueva con nombre único, número de filas y sillas por fila, el sistema deberá crear automáticamente sus sillas identificadas por letra de fila y número.
- **AC-009.1** Given una sala «Sala 2» con 5 filas y 8 sillas por fila When la guarda Then se crean 40 sillas de A1 a E8.
- **AC-009.2** Given el nombre «Sala 1» ya registrado When guarda otra sala con ese nombre Then ve «Nombre de sala repetido» And no se crea.

### REQ-CIN-010 · Sillas fuera de servicio · YARC · P-14 · S
**Historia:** Como administrador quiero marcar sillas fuera de servicio para que no se vendan sillas dañadas.
**EARS (Evento):** Cuando el administrador marque una silla como fuera de servicio, el sistema deberá excluirla de la venta en todas las funciones futuras de esa sala (BR-CIN-008).
- **AC-010.1** Given la silla C4 activa When la marca fuera de servicio Then aparece gris en el mapa And no se puede seleccionar al comprar.
- **AC-010.2** Given la silla C4 fuera de servicio When la reactiva Then vuelve a estar disponible en las funciones futuras donde no esté vendida.

### REQ-CIN-011 · Programar funciones · YARC · P-15, P-16 · M
**Historia:** Como administrador quiero programar funciones para ofrecer horarios de cada película.
**EARS (Evento):** Cuando el administrador guarde una función con película, sala, fecha y hora futuras y precio mayor que cero, el sistema deberá crearla PROGRAMADA y calcular su fin como inicio más duración más 15 minutos (BR-CIN-003).
- **AC-011.1** Given una película de 120 minutos When se programa a las 18:00 Then la función termina a las 20:15.
- **AC-011.2** Given una fecha pasada When guarda la función Then ve «La función debe ser futura» And no se crea.

### REQ-CIN-012 · Impedir funciones solapadas · YARC · P-16 · M
**Historia:** Como administrador quiero que el sistema impida funciones solapadas para no ocupar la misma sala dos veces.
**EARS (No deseado):** Si la nueva función se cruza con otra función PROGRAMADA de la misma sala, entonces el sistema deberá rechazarla e indicar la función con la que choca (BR-CIN-003).
- **AC-012.1** Given la Sala 1 ocupada de 18:00 a 20:15 When se programa otra a las 19:30 en la Sala 1 Then se rechaza And ve «Se cruza con la función de las 18:00».
- **AC-012.2** Given la misma situación When se programa a las 19:30 en la Sala 2 Then la función se crea.
- **AC-012.3** Given la misma situación When se programa a las 20:15 en la Sala 1 Then la función se crea.

### REQ-CIN-025 · Página de inicio (landing) · EVGP · P-22 · M
**Historia:** Como visitante quiero una página de inicio que presente el cine para conocer qué ofrece y llegar rápido a la cartelera o a mi cuenta.
**EARS (Evento):** Cuando un visitante abra la raíz del sitio (`/`), el sistema deberá mostrar la landing con: hero con la marca y el botón «Ver cartelera»; hasta 4 películas destacadas (en cartelera y con funciones futuras); las funciones de hoy con hora, película y sala; «Cómo comprar» en 3 pasos (elige película, elige sillas, paga y recibe tus códigos); las salas activas con su capacidad y el horario de las funciones de hoy; los botones «Iniciar sesión» y «Registrarme»; y un footer con datos de contacto.
- **AC-025.1** Given un visitante sin sesión When abre `/` Then ve la landing And el botón «Ver cartelera» lo lleva a `/cartelera`.
- **AC-025.2** Given 6 películas en cartelera con funciones futuras When abre `/` Then ve 4 películas destacadas And cada una enlaza a su detalle (P-02).
- **AC-025.3** Given 3 funciones programadas para hoy y 1 para mañana When abre `/` Then ve solo las 3 funciones de hoy.
- **AC-025.4** Given ninguna función para hoy When abre `/` Then ve «Hoy no hay funciones programadas» And un enlace a la cartelera.
- **AC-025.5** Given un visitante sin sesión When pulsa «Registrarme» o «Iniciar sesión» Then llega a P-03 o P-04 según el botón.
- **AC-025.6** Given un cliente con sesión iniciada When abre `/` Then no ve los botones «Registrarme» ni «Iniciar sesión».

---

## Sprint 2 (23 al 27 de noviembre)

### REQ-CIN-013 · Mapa de sillas · EVGP · P-05 · M
**Historia:** Como cliente quiero ver el mapa de sillas de una función para escoger dónde sentarme.
**EARS (Evento):** Cuando un usuario abra una función, el sistema deberá mostrar el mapa de la sala con las sillas disponibles, vendidas y fuera de servicio diferenciadas por color, y la pantalla al frente.
- **AC-013.1** Given una función con A1 y A2 vendidas When el cliente abre el mapa Then A1 y A2 aparecen como vendidas And no se pueden seleccionar.
- **AC-013.2** Given una función de $ 12.000 When el cliente selecciona 3 sillas Then ve las 3 sillas en el resumen And un total de $ 36.000.

### REQ-CIN-014 · Compra en línea · EVGP · P-05, P-06, P-08 · M
**Historia:** Como cliente quiero comprar boletas en línea para asegurar mis sillas sin hacer fila.
**EARS (Evento):** Cuando el cliente confirme la compra de sillas disponibles con el pago simulado, el sistema deberá crear en una sola transacción la compra PAGADA y una boleta VENDIDA por silla, cada una con un código único.
- **AC-014.1** Given 2 sillas disponibles seleccionadas When confirma el pago simulado Then ve el comprobante con 2 boletas And cada una con su código.
- **AC-014.2** Given la compra confirmada When otro usuario abre el mapa Then esas 2 sillas aparecen vendidas.

### REQ-CIN-015 · Reglas de compra · EVGP · P-06 · M
**Historia:** Como administrador quiero que se apliquen las reglas de compra para evitar sillas duplicadas y excesos.
**EARS (No deseado):** Si alguna silla ya está vendida para la función (BR-CIN-001), se piden más de 6 boletas (BR-CIN-002) o la función ya empezó o fue cancelada (BR-CIN-005), entonces el sistema deberá rechazar toda la compra e indicar el motivo.
- **AC-015.1** Given dos usuarios con la silla B5 seleccionada When ambos confirman Then solo una compra se acepta And el otro ve «La silla B5 ya fue vendida».
- **AC-015.2** Given 7 sillas seleccionadas When confirma Then ve «Máximo 6 boletas por compra» And no se crea la compra.
- **AC-015.3** Given una función que empezó hace 5 minutos When intenta comprar Then ve «La función ya inició».

**Scenario Outline:** Given una función que empieza en `<horas>` horas y `<boletas>` sillas seleccionadas, de las cuales `<vendidas>` ya están vendidas, When el usuario confirma la compra, Then el resultado es `<resultado>`.

| Horas para el inicio | Boletas | Ya vendidas | Resultado |
|---|---|---|---|
| 3 | 2 | 0 | Aceptada |
| 3 | 6 | 0 | Aceptada |
| 3 | 7 | 0 | Rechazada: máximo 6 boletas |
| 3 | 2 | 1 | Rechazada: silla ya vendida |
| -0,1 (ya inició) | 2 | 0 | Rechazada: la función ya inició |

### REQ-CIN-016 · Mis boletas · EVGP · P-07, P-08 · M
**Historia:** Como cliente quiero ver mis boletas para saber mi función, sala, sillas y código de ingreso.
**EARS (Estado):** Mientras el cliente esté autenticado, el sistema deberá mostrarle únicamente sus propias compras con función, sala, sillas, códigos y estado (NFR-CIN-002).
- **AC-016.1** Given un cliente con 2 compras When abre «Mis boletas» Then ve esas 2 compras.
- **AC-016.2** Given el comprobante de otro cliente When intenta abrirlo desde la dirección Then ve la pantalla de acceso denegado.

### REQ-CIN-017 · Cancelar compra · EVGP · P-07 · S
**Historia:** Como cliente quiero cancelar una compra con anticipación para liberar las sillas si no puedo asistir.
**EARS (Evento):** Cuando el cliente cancele una compra PAGADA al menos 2 horas antes del inicio de la función, el sistema deberá marcar la compra y sus boletas como CANCELADAS y liberar las sillas (BR-CIN-004).
- **AC-017.1** Given una función que empieza en 5 horas When el cliente cancela Then la compra queda CANCELADA And las sillas vuelven a estar disponibles.
- **AC-017.2** Given una función que empieza en 1 hora When intenta cancelar Then ve «Plazo de cancelación vencido» And la compra sigue PAGADA.

**Scenario Outline:** Given una compra PAGADA cuya función empieza en `<horas>` horas, When el cliente la cancela, Then el resultado es `<resultado>`.

| Horas para el inicio | Resultado |
|---|---|
| 5 | Aceptada: sillas liberadas |
| 2 | Aceptada: límite incluido |
| 1,5 | Rechazada: plazo de cancelación vencido |
| 0 (ya inició) | Rechazada: plazo de cancelación vencido |

### REQ-CIN-018 · Venta en taquilla · YARC · P-18, P-05, P-06, P-08 · M
**Historia:** Como taquillero quiero vender boletas en taquilla para atender a quien llega sin compra en línea.
**EARS (Evento):** Cuando el taquillero confirme la venta de sillas disponibles de una función del día con pago en efectivo, el sistema deberá registrar la compra con él como vendedor, aplicar las reglas de REQ-CIN-015 y mostrar el comprobante para imprimir.
- **AC-018.1** Given una función de hoy con sillas libres When el taquillero vende 3 sillas Then ve el comprobante And la venta aparece en sus ventas del día.
- **AC-018.2** Given una silla vendida en línea hace un minuto When el taquillero intenta venderla Then ve «La silla ya fue vendida».

> Depende de REQ-CIN-013 a 015: Erika los entrega en la primera mitad del Sprint 2.

### REQ-CIN-019 · Validar ingreso · YARC · P-19 · M
**Historia:** Como acomodador quiero validar el código de la boleta para controlar el ingreso a la sala.
**EARS (Evento):** Cuando el acomodador ingrese el código de una boleta VENDIDA dentro de la ventana de ingreso de su función (BR-CIN-006), el sistema deberá marcarla USADA con la hora de ingreso y mostrar la sala y la silla.
- **AC-019.1** Given una boleta VENDIDA para la función de las 18:00 en la silla D7 When se valida a las 17:45 Then queda USADA And ve «Sala 1, silla D7».
- **AC-019.2** Given la misma boleta ya validada When se valida de nuevo Then ve «Boleta ya usada a las 17:45».

### REQ-CIN-020 · Rechazar boletas inválidas · YARC · P-19 · M
**Historia:** Como acomodador quiero que se rechacen las boletas inválidas para no dejar entrar a quien no corresponde.
**EARS (No deseado):** Si el código no existe, la boleta está CANCELADA o se valida fuera de la ventana de ingreso (BR-CIN-006), entonces el sistema deberá rechazar el ingreso, mostrar el motivo y no cambiar la boleta.
- **AC-020.1** Given un código inexistente When se valida Then ve «Boleta no encontrada».
- **AC-020.2** Given una boleta CANCELADA When se valida Then ve «Boleta cancelada» And su estado no cambia.
- **AC-020.3** Given una boleta para la función de las 18:00 When se valida a las 16:00 Then ve «Aún no se permite el ingreso».

### REQ-CIN-021 · Ocupación de función · YARC · P-20 · S
**Historia:** Como acomodador quiero ver la ocupación de una función para guiar a los asistentes.
**EARS (Estado):** Mientras el acomodador consulte una función del día, el sistema deberá mostrar el mapa con sillas libres, vendidas por ingresar e ingresadas, con el total de cada estado.
- **AC-021.1** Given una sala de 40 sillas y 25 boletas vendidas, 10 de ellas ya usadas When abre la ocupación Then ve «15 libres, 15 por ingresar, 10 ingresadas».
- **AC-021.2** Given una función de mañana When revisa la lista del día Then esa función no aparece.

### REQ-CIN-022 · Dashboard · YARC · P-09 · S
**Historia:** Como administrador quiero un dashboard para ver el estado del cine de un vistazo.
**EARS (Evento):** Cuando el administrador abra el dashboard, el sistema deberá mostrar las funciones del día, las boletas vendidas hoy y el ingreso del día sin contar cancelaciones, y el porcentaje de ocupación de cada función.
- **AC-022.1** Given 3 funciones hoy y 50 boletas vendidas a $ 10.000 When abre el dashboard Then ve «3 funciones», «50 boletas» y «$ 500.000».
- **AC-022.2** Given ninguna venta hoy When abre el dashboard Then ve 0 en boletas y en ingresos.

### REQ-CIN-023 · Cuentas del personal · EVGP · P-17 · S
**Historia:** Como administrador quiero gestionar las cuentas del personal para controlar quién vende y quién valida.
**EARS (Evento):** Cuando el administrador cree una cuenta de Taquillero o Acomodador o inactive a un usuario, el sistema deberá aplicar el cambio e impedir el inicio de sesión de las cuentas inactivas, conservando sus ventas.
- **AC-023.1** Given datos válidos y rol Acomodador When crea la cuenta Then el acomodador puede iniciar sesión And llega a control de ingreso.
- **AC-023.2** Given un taquillero inactivado When intenta iniciar sesión Then ve «Cuenta inactiva» And sus ventas siguen en el dashboard.

### REQ-CIN-024 · Errores amigables · YARC · P-21 · S
**Historia:** Como usuario quiero mensajes claros ante errores para saber cómo continuar.
**EARS (No deseado):** Si ocurre un error 404 o 500, entonces el sistema deberá mostrar una pantalla de error amigable con un enlace al inicio.
- **AC-024.1** Given una ruta que no existe When el usuario la abre Then ve la pantalla 404 And un botón «Volver al inicio».
- **AC-024.2** Given la pantalla de error When pulsa «Volver al inicio» Then llega a la página de inicio (P-22).

---

## Requisitos no funcionales (NFR)

| ID | Tipo | Descripción verificable | Cómo se verifica |
|---|---|---|---|
| NFR-CIN-001 | Rendimiento | Cartelera y mapa de sillas responden en menos de 2 s con 50 películas y 200 funciones en local | Medición en el navegador |
| NFR-CIN-002 | Seguridad | Cada rol accede solo a sus pantallas y cada cliente solo a sus compras y boletas | Pruebas de acceso por rol |
| NFR-CIN-003 | Usabilidad | Todas las pantallas, incluido el mapa, se usan desde 375 px sin desplazamiento horizontal | Revisión en modo responsivo |
| NFR-CIN-004 | Seguridad | Claves como hash con sal, nunca en texto plano | Revisión de la tabla usuarios |
| NFR-CIN-005 | Integridad | Dos ventas simultáneas de la misma silla y función nunca terminan ambas aceptadas | Prueba con dos navegadores (AC-015.1) |
| NFR-CIN-006 | Portabilidad | Funciona igual con H2 y MySQL cambiando solo el perfil | Pruebas con ambos perfiles |
| NFR-CIN-007 | Despliegue | Arranca en `http://localhost:8093` desde un clon limpio siguiendo solo el README | Clonado y arranque en otro equipo |

## Reglas de negocio (BR)

| ID | Regla | Se prueba en |
|---|---|---|
| BR-CIN-001 | Una silla no se vende dos veces para la misma función (sin contar boletas canceladas) | AC-015.1, AC-018.2 |
| BR-CIN-002 | Una compra tiene como máximo 6 boletas | AC-015.2 |
| BR-CIN-003 | Las funciones de una sala no se solapan; cada función ocupa la sala su duración más 15 min de limpieza | AC-011.1, AC-012.1 |
| BR-CIN-004 | El cliente cancela hasta 2 horas antes del inicio (límite incluido); libera las sillas y no hay reembolso real | AC-017.1, AC-017.2 |
| BR-CIN-005 | No se venden boletas para funciones iniciadas o canceladas | AC-015.3 |
| BR-CIN-006 | Una boleta se valida una sola vez, desde 30 min antes del inicio hasta el fin de su función | AC-019.2, AC-020.3 |
| BR-CIN-007 | No se elimina un género con películas, una película con funciones futuras ni una sala con funciones futuras | AC-007.2, AC-008.3 |
| BR-CIN-008 | Una silla fuera de servicio no se puede vender | AC-010.1 |

## Preguntas abiertas y supuestos de trabajo

Supuestos acordados por el equipo, por confirmar con el Product Owner en la Puerta 1.

| [NECESITA ACLARACIÓN] | Supuesto de trabajo |
|---|---|
| ¿Hasta cuántas horas antes puede el cliente cancelar? | Hasta 2 horas antes, límite incluido (BR-CIN-004) |
| ¿Cuántas boletas como máximo por compra? | 6 (BR-CIN-002) |
| ¿Cuánto tiempo de limpieza entre funciones de una sala? | 15 minutos (BR-CIN-003) |
| ¿La venta en taquilla exige que el comprador tenga cuenta? | No; la compra queda a nombre del taquillero como vendedor |
| ¿Desde cuándo puede el acomodador validar una boleta? | Desde 30 min antes del inicio hasta el fin de la función (BR-CIN-006) |
| ¿Hay precios distintos por tipo de silla o por edad? | No; precio único por función |
| ¿Qué datos de contacto lleva el footer de la landing? | Datos ficticios fijos en la plantilla (dirección, teléfono y correo del cine), sin tabla nueva |
| ¿Cómo se eligen las películas destacadas? | Las 4 primeras en cartelera con la función futura más próxima, sin atributo nuevo en Pelicula |
