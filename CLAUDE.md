# Última Fila

Claude Code lee este archivo automáticamente. Las reglas completas están en:

@INSTRUCCIONES.md
@DISENO.md

Antes de escribir código, lee todos los archivos de `especificaciones/`.

## Decisiones de la sesión

- Erika hace todo el proyecto, incluidas las tareas que antes eran de Yeziel. No reserves nada para él.
- Flujo: antes de cada requisito, di los archivos que vas a crear o modificar. Una rama y un PR a `main` por requisito, sin mezclar. Erika mezcla y hace pull.
- Base de datos: H2 por defecto y MySQL por perfil (`mysql`). Credenciales solo en variables de entorno, nunca en el repositorio. MySQL local por ahora (Aiven es una opción).
- El despliegue en Render, con Dockerfile, es del Sprint 2: no lo prepares todavía.
- Antes de arrancar la app, cierra la instancia anterior (evita el bloqueo de `ultimafila.mv.db`).
- Prueba siempre en escritorio y a 375 px sin scroll lateral.
- Orden pendiente: migración a MySQL, luego REQ-CIN-018 a 022 y 024.
