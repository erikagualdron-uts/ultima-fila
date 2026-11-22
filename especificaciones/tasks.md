# Tareas por sprint (Puerta 3)

Marca `[x]` al terminar. Cada requisito va en su rama `feature/REQ-CIN-XXX` con PR revisado por el compaÃ±ero. Tablero: Por hacer Â· En curso (mÃ¡x. 2 por persona) Â· En revisiÃ³n Â· Bloqueado Â· Hecho. Cada desarrollador: unas 20 h por sprint.

## PreparaciÃ³n (13 al 15 de noviembre)

- [x] Proyecto Spring Boot en Spring Tools for Eclipse (Maven, Java 21, 7 dependencias)
- [x] `feature/config-base`: properties (puerto 8093, perfiles h2 y mysql), paquetes, `.gitignore`
- [x] Bootstrap 5 y fuentes en `static/`, layout Thymeleaf, CSS base de `DISENO.md`
- [ ] Repositorio en GitHub con ambos como colaboradores, `main` protegida, tablero Scrum

## Sprint 1 (16 al 20 de noviembre) Â· Facilita Erika

**Meta:** especificaciÃ³n v1.0 y MER aprobados, H2 con datos semilla, cartelera pÃºblica con bÃºsqueda, filtros y detalle, los cuatro roles inician sesiÃ³n viendo solo sus opciones, y el Administrador gestiona gÃ©neros, pelÃ­culas, salas, sillas y funciones sin cruces.

### Erika (EVGP)
- [ ] ConstituciÃ³n SDD (`constitucion.md`)
- [x] Layout Thymeleaf con Bootstrap: navbar segÃºn rol, footer, alertas
- [x] REQ-CIN-001 Cartelera paginada (P-01)
- [x] REQ-CIN-002 BÃºsqueda y filtros por gÃ©nero y fecha (P-01)
- [x] REQ-CIN-003 Detalle de pelÃ­cula con funciones futuras (P-02)
- [x] REQ-CIN-004 Registro de cliente con Bean Validation y hash con sal (P-03)
- [x] REQ-CIN-005 Inicio y cierre de sesiÃ³n con HttpSession (P-04)
- [x] REQ-CIN-006 Interceptor por rol y acceso denegado (P-21)
- [ ] RevisiÃ³n cruzada de los PR de Yeziel

### Yeziel (YARC)
- [x] MER y 8 entidades JPA con enums y repositorios
- [x] ConfiguraciÃ³n H2 y `data.sql` (5 gÃ©neros, 10 pelÃ­culas, 3 salas, funciones de 2 semanas, 5 usuarios)
- [ ] REQ-CIN-007 CRUD de gÃ©neros (P-10)
- [ ] REQ-CIN-008 CRUD de pelÃ­culas (P-11, P-12)
- [ ] REQ-CIN-009 Salas con generaciÃ³n de sillas (P-13)
- [ ] REQ-CIN-010 Sillas fuera de servicio (P-14)
- [ ] REQ-CIN-011 Programar funciones con cÃ¡lculo de fin (P-15, P-16)
- [ ] REQ-CIN-012 Regla de no solapamiento (P-16)
- [ ] RevisiÃ³n cruzada de los PR de Erika

### Cierre
- [ ] Sprint Review con el PO y Sprint Retrospective

## Sprint 2 (23 al 27 de noviembre) Â· Facilita Yeziel

**Meta:** la app funciona sobre MySQL, el Cliente compra y cancela en lÃ­nea, el Taquillero vende con las mismas reglas, el Acomodador valida el ingreso, el Administrador consulta el dashboard y gestiona al personal, y la interfaz es responsiva y maneja errores.

### Erika (EVGP), primero 013 a 015 porque REQ-CIN-018 depende de ellos
- [x] REQ-CIN-013 Mapa de sillas con JavaScript: contador, lÃ­mite y total (P-05)
- [x] REQ-CIN-014 Compra en lÃ­nea en una transacciÃ³n con pago simulado (P-05, P-06, P-08)
- [x] REQ-CIN-015 Reglas de compra con bloqueo pesimista (P-06)
- [ ] REQ-CIN-016 Mis boletas y comprobante (P-07, P-08)
- [ ] REQ-CIN-017 CancelaciÃ³n dentro del plazo (P-07)
- [ ] REQ-CIN-023 Usuarios del personal (P-17)
- [ ] RevisiÃ³n cruzada de los PR de Yeziel

### Yeziel (YARC)
- [ ] MigraciÃ³n de H2 a MySQL con perfiles
- [ ] REQ-CIN-018 Venta en taquilla (P-18, P-05, P-06, P-08)
- [ ] REQ-CIN-019 ValidaciÃ³n de boleta (P-19)
- [ ] REQ-CIN-020 Rechazo de boletas invÃ¡lidas (P-19)
- [ ] REQ-CIN-021 OcupaciÃ³n de funciÃ³n (P-20)
- [ ] REQ-CIN-022 Dashboard (P-09)
- [ ] REQ-CIN-024 PÃ¡ginas de error con @ControllerAdvice (P-21)
- [ ] RevisiÃ³n cruzada de los PR de Erika

### Ambos
- [ ] Pruebas de extremo a extremo sobre MySQL (incluida AC-015.1 con dos navegadores)
- [ ] RevisiÃ³n responsiva desde 375 px en todas las pantallas
- [ ] README con pasos para H2 y MySQL (NFR-CIN-007)
- [ ] Matriz de trazabilidad (REQ â†’ responsable â†’ tarea â†’ cÃ³digo â†’ prueba â†’ pantalla â†’ entidad)
- [ ] Sprint Review, Retrospective y release v1.0.0

**DemostraciÃ³n y entrega final:** lunes 30 de noviembre de 2026.

## Horas estimadas

| Actividad | Erika S1 | Erika S2 | Yeziel S1 | Yeziel S2 | Total |
|---|---|---|---|---|---|
| EspecificaciÃ³n SDD y tablero | 4 | 1 | 2 | 1 | 8 |
| MER y entidades JPA | 2 | 1 | 4 | 1 | 8 |
| Base de datos (H2 y MySQL) | 0 | 1 | 1 | 3 | 5 |
| Controladores y repositorios | 5 | 6 | 6 | 6 | 23 |
| Pantallas | 7 | 8 | 5 | 6 | 26 |
| RevisiÃ³n cruzada, pruebas y documentaciÃ³n | 2 | 3 | 2 | 3 | 10 |
| **Total** | **20** | **20** | **20** | **20** | **80** |
