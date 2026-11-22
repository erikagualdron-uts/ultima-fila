# Última Fila

Claude Code lee este archivo automáticamente. Las reglas completas están en:

@INSTRUCCIONES.md
@DISENO.md

Antes de escribir código, lee todos los archivos de `especificaciones/`.

Al terminar cada requisito: rama `feature/REQ-CIN-XXX`, revisar `git status` (sin claves, `target/` ni `.env`), commit `REQ-CIN-XXX: descripción` y `git push -u origin` de esa rama, sin preguntar cada vez, y mostrar el resumen. Nunca push a `main` ni force push. Si hay conflicto o el push falla, detente y avisa.
