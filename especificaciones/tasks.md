# Tareas por sprint (Puerta 3)

Marca `[x]` al terminar. Cada requisito va en su rama `feature/REQ-CIN-XXX` con PR revisado por el compañero. Tablero: Por hacer · En curso (máx. 2 por persona) · En revisión · Bloqueado · Hecho. Cada desarrollador: unas 20 h por sprint.

## Preparación (13 al 15 de noviembre)

- [x] Proyecto Spring Boot en Spring Tools for Eclipse (Maven, Java 21, 7 dependencias)
- [x] `feature/config-base`: properties (puerto 8093, perfiles h2 y mysql), paquetes, `.gitignore`
- [x] Bootstrap 5 y fuentes en `static/`, layout Thymeleaf, CSS base de `DISENO.md`
- [ ] Repositorio en GitHub con ambos como colaboradores, `main` protegida, tablero Scrum

## Sprint 1 (16 al 20 de noviembre) · Facilita Erika

**Meta:** especificación v1.0 y MER aprobados, H2 con datos semilla, cartelera pública con búsqueda, filtros y detalle, los cuatro roles inician sesión viendo solo sus opciones, y el Administrador gestiona géneros, películas, salas, sillas y funciones sin cruces.

### Erika (EVGP)
- [ ] Constitución SDD (`constitucion.md`)
- [x] Layout Thymeleaf con Bootstrap: navbar según rol, footer, alertas
- [x] REQ-CIN-001 Cartelera paginada (P-01)
- [x] REQ-CIN-002 Búsqueda y filtros por género y fecha (P-01)
- [x] REQ-CIN-003 Detalle de película con funciones futuras (P-02)
- [ ] REQ-CIN-004 Registro de cliente con Bean Validation y hash con sal (P-03)
- [ ] REQ-CIN-005 Inicio y cierre de sesión con HttpSession (P-04)
- [ ] REQ-CIN-006 Interceptor por rol y acceso denegado (P-21)
- [ ] Revisión cruzada de los PR de Yeziel

### Yeziel (YARC)
- [x] MER y 8 entidades JPA con enums y repositorios
- [x] Configuración H2 y `data.sql` (5 géneros, 10 películas, 3 salas, funciones de 2 semanas, 5 usuarios)
- [ ] REQ-CIN-007 CRUD de géneros (P-10)
- [ ] REQ-CIN-008 CRUD de películas (P-11, P-12)
- [ ] REQ-CIN-009 Salas con generación de sillas (P-13)
- [ ] REQ-CIN-010 Sillas fuera de servicio (P-14)
- [ ] REQ-CIN-011 Programar funciones con cálculo de fin (P-15, P-16)
- [ ] REQ-CIN-012 Regla de no solapamiento (P-16)
- [ ] Revisión cruzada de los PR de Erika

### Cierre
- [ ] Sprint Review con el PO y Sprint Retrospective

## Sprint 2 (23 al 27 de noviembre) · Facilita Yeziel

**Meta:** la app funciona sobre MySQL, el Cliente compra y cancela en línea, el Taquillero vende con las mismas reglas, el Acomodador valida el ingreso, el Administrador consulta el dashboard y gestiona al personal, y la interfaz es responsiva y maneja errores.

### Erika (EVGP), primero 013 a 015 porque REQ-CIN-018 depende de ellos
- [ ] REQ-CIN-013 Mapa de sillas con JavaScript: contador, límite y total (P-05)
- [ ] REQ-CIN-014 Compra en línea en una transacción con pago simulado (P-05, P-06, P-08)
- [ ] REQ-CIN-015 Reglas de compra con bloqueo pesimista (P-06)
- [ ] REQ-CIN-016 Mis boletas y comprobante (P-07, P-08)
- [ ] REQ-CIN-017 Cancelación dentro del plazo (P-07)
- [ ] REQ-CIN-023 Usuarios del personal (P-17)
- [ ] Revisión cruzada de los PR de Yeziel

### Yeziel (YARC)
- [ ] Migración de H2 a MySQL con perfiles
- [ ] REQ-CIN-018 Venta en taquilla (P-18, P-05, P-06, P-08)
- [ ] REQ-CIN-019 Validación de boleta (P-19)
- [ ] REQ-CIN-020 Rechazo de boletas inválidas (P-19)
- [ ] REQ-CIN-021 Ocupación de función (P-20)
- [ ] REQ-CIN-022 Dashboard (P-09)
- [ ] REQ-CIN-024 Páginas de error con @ControllerAdvice (P-21)
- [ ] Revisión cruzada de los PR de Erika

### Ambos
- [ ] Pruebas de extremo a extremo sobre MySQL (incluida AC-015.1 con dos navegadores)
- [ ] Revisión responsiva desde 375 px en todas las pantallas
- [ ] README con pasos para H2 y MySQL (NFR-CIN-007)
- [ ] Matriz de trazabilidad (REQ → responsable → tarea → código → prueba → pantalla → entidad)
- [ ] Sprint Review, Retrospective y release v1.0.0

**Demostración y entrega final:** lunes 30 de noviembre de 2026.

## Horas estimadas

| Actividad | Erika S1 | Erika S2 | Yeziel S1 | Yeziel S2 | Total |
|---|---|---|---|---|---|
| Especificación SDD y tablero | 4 | 1 | 2 | 1 | 8 |
| MER y entidades JPA | 2 | 1 | 4 | 1 | 8 |
| Base de datos (H2 y MySQL) | 0 | 1 | 1 | 3 | 5 |
| Controladores y repositorios | 5 | 6 | 6 | 6 | 23 |
| Pantallas | 7 | 8 | 5 | 6 | 26 |
| Revisión cruzada, pruebas y documentación | 2 | 3 | 2 | 3 | 10 |
| **Total** | **20** | **20** | **20** | **20** | **80** |
