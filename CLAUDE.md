# Última Fila

Claude Code lee este archivo automáticamente. Las reglas completas están en:

@INSTRUCCIONES.md
@DISENO.md

Antes de escribir código, lee todos los archivos de `especificaciones/`.

## Decisiones de la sesión

- Erika hace todo el proyecto, incluidas las tareas que antes eran de Yeziel. No reserves nada para él.
- Flujo: antes de cada requisito, di los archivos que vas a crear o modificar. Una rama y un PR a `main` por requisito, sin mezclar. Erika mezcla y hace pull.
- Base de datos: H2 por defecto y MySQL por perfil (`mysql`). Credenciales solo en variables de entorno, nunca en el repositorio. MySQL local para desarrollo; Aiven solo al desplegar (ver Despliegue).
- El despliegue (Dockerfile, Render y Aiven) va al final, después de los requisitos: no lo prepares antes (ver Despliegue).
- Antes de arrancar la app, cierra la instancia anterior (evita el bloqueo de `ultimafila.mv.db`).
- Prueba siempre en escritorio y a 375 px sin scroll lateral.
- Orden pendiente: migración a MySQL, luego REQ-CIN-018 a 022 y 024.

## Despliegue

- Se hace al final, después de los requisitos y las pruebas en MySQL. La entrega es el lunes 30 de noviembre de 2026; el domingo queda reservado para desplegar.
- Base de datos MySQL en Aiven (plan gratis).
- App Spring Boot empaquetada con Docker (`Dockerfile`) y publicada en Render, conectada a Aiven con variables de entorno.
- El proyecto ya tiene el perfil `mysql`: úsalo en Render, sin credenciales en el repositorio.
