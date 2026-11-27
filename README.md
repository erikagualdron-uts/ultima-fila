# Última Fila (The Back Row)

Sistema web del **Cine Comunitario Luz de Barrio**: cartelera pública, compra de boletas en línea y en taquilla, validación del ingreso a la sala y administración del cine. Proyecto de Programación en Java (UTS).

Spring Boot 4.2.0, Java 21, Thymeleaf, Spring Data JPA, Bootstrap 5. Base de datos **H2** por defecto y **MySQL** por perfil. Puerto **8093**.

## Requisitos previos

- **JDK 21** (`java -version` debe mostrar 21).
- **Git**.
- Para el perfil MySQL: un servidor **MySQL 8** o Docker.
- No hace falta instalar Maven: el proyecto trae Maven Wrapper (`mvnw` y `mvnw.cmd`).

## Arrancar con H2 (por defecto)

No necesita instalar ni configurar nada más.

```bash
git clone https://github.com/erikagualdron-uts/ultima-fila.git
cd ultima-fila
./mvnw spring-boot:run
```

En Windows (PowerShell o CMD): `.\mvnw.cmd spring-boot:run`. Cuando aparezca `Started UltimaFilaApplication`, abre **http://localhost:8093**.

- Los datos de ejemplo se **recrean en cada arranque** (5 géneros, 10 películas, 3 salas, funciones de dos semanas y 5 usuarios). Lo que se haga en una sesión no se conserva al reiniciar.
- La base es un archivo en `./data/` (fuera de Git). Solo puede haber **una instancia** a la vez: si arrancas dos, la segunda falla con «The file is locked: ultimafila.mv.db».
- Si el puerto 8093 está ocupado, usa otro: `./mvnw spring-boot:run "-Dspring-boot.run.arguments=--server.port=8094"`.
- En macOS o Linux, si `./mvnw` responde «Permission denied», ejecuta `chmod +x mvnw` (o usa `sh mvnw spring-boot:run`).

### Cuentas de prueba

Todas usan la clave **`Clave1234`** (datos ficticios de `src/main/resources/data.sql`).

| Rol | Correo | Al iniciar sesión va a |
|---|---|---|
| Administrador | `admin@luzdebarrio.test` | `/admin` (dashboard) |
| Taquillero | `taquilla@luzdebarrio.test` | `/taquilla` |
| Acomodador | `acomodador@luzdebarrio.test` | `/acomodador` (control de ingreso) |
| Cliente | `camila@correo.test` y `carlos@correo.test` | `/cartelera` |

Sin iniciar sesión se puede ver la página de inicio, la cartelera, el detalle de cada película y el mapa de sillas, y registrarse como cliente.

### Consola de H2

Con el perfil `h2` hay una consola web para consultar la base: **http://localhost:8093/h2-console**.

| Campo | Valor |
|---|---|
| JDBC URL | `jdbc:h2:file:./data/ultimafila;MODE=MySQL;DATABASE_TO_LOWER=TRUE` |
| Usuario | `sa` |
| Clave | (vacía) |

La consola solo existe con el perfil `h2`; con `mysql` está desactivada.

## Arrancar con MySQL

El perfil `mysql` usa las mismas entidades y repositorios: cambiar de base solo cambia el perfil. Las credenciales **nunca** van en el repositorio: se pasan como variables de entorno.

### Variables de entorno

| Variable | Obligatoria | Por defecto | Para qué sirve |
|---|---|---|---|
| `SPRING_PROFILES_ACTIVE` | Sí | `h2` | Debe valer `mysql` |
| `DB_USER` | Sí | — | Usuario de MySQL |
| `DB_PASSWORD` | Sí | — | Clave de MySQL |
| `DB_HOST` | No | `localhost` | Servidor |
| `DB_PORT` | No | `3306` | Puerto |
| `DB_NAME` | No | `ultimafila` | Base de datos; se crea sola si no existe |
| `DB_SSL_MODE` | No | `DISABLED` | Modo SSL del conector (`REQUIRED` en un MySQL administrado como Aiven) |
| `DDL_AUTO` | No | `update` | Qué hace Hibernate con las tablas: `create` las recrea, `update` las conserva |
| `DB_INIT` | No | `never` | `always` carga `data.sql` (datos de ejemplo); `never` no la ejecuta |

### MySQL local con Docker (opcional)

```bash
docker run -d --name ultimafila-mysql -e MYSQL_ROOT_PASSWORD=<tu-clave> -p 3306:3306 mysql:8.0
```

Usa una clave propia en lugar de `<tu-clave>` y, con este contenedor, `DB_USER=root`. Para detenerlo y reanudarlo: `docker stop ultimafila-mysql` y `docker start ultimafila-mysql`.

### Primer arranque (crea las tablas y carga los datos de ejemplo)

Bash:

```bash
export SPRING_PROFILES_ACTIVE=mysql DB_USER=root DB_PASSWORD='<tu-clave>' DDL_AUTO=create DB_INIT=always
./mvnw spring-boot:run
```

PowerShell:

```powershell
$env:SPRING_PROFILES_ACTIVE='mysql'; $env:DB_USER='root'; $env:DB_PASSWORD='<tu-clave>'; $env:DDL_AUTO='create'; $env:DB_INIT='always'
.\mvnw.cmd spring-boot:run
```

En el primer arranque con `create` salen unos avisos `WARN` de `alter table … drop foreign key`: son normales, porque las tablas todavía no existen.

### Arranques siguientes (conservan los datos)

Quita `DDL_AUTO` y `DB_INIT` (o déjalos en `update` y `never`):

```bash
unset DDL_AUTO DB_INIT
./mvnw spring-boot:run
```

En PowerShell: `Remove-Item Env:DDL_AUTO, Env:DB_INIT`. **No dejes `DB_INIT=always` ni `DDL_AUTO=create`** después del primer arranque: recargarían los datos de ejemplo o borrarían las tablas. Con una base vacía y los valores por defecto se crean las tablas pero **sin datos**, y no habrá cuentas para iniciar sesión.

### Spring Tools for Eclipse

Run Configurations → pestaña **Environment**: agrega `SPRING_PROFILES_ACTIVE`, `DB_USER`, `DB_PASSWORD` y las demás variables de la tabla.

## Pruebas

```bash
./mvnw test
```

La prueba `UltimaFilaApplicationTests` abre la misma base H2 en archivo que la aplicación, así que **cierra la aplicación antes**. Si no puedes cerrarla, usa una base en memoria: `./mvnw test "-Dspring.datasource.url=jdbc:h2:mem:pruebas;MODE=MySQL;DATABASE_TO_LOWER=TRUE"`.

Los resultados de las pruebas de extremo a extremo sobre MySQL están en [`especificaciones/pruebas-mysql.md`](especificaciones/pruebas-mysql.md) y los de la revisión a 375 px en [`especificaciones/revision-375px.md`](especificaciones/revision-375px.md).

## Estructura del proyecto

```
src/main/java/com/utsproyecto/ultimafila
├── controller   controladores que devuelven vistas Thymeleaf (patrón PRG)
├── repository   repositorios JPA y consultas JPQL
├── model        entidades y enums
└── config       interceptor de sesión por rol, configuración web y manejo de errores
src/main/resources
├── templates    vistas Thymeleaf (layout común, administración, compra, taquilla, ingreso)
├── static       Bootstrap, CSS y JavaScript propios, fuentes e imágenes (funciona sin internet)
├── data.sql     datos de ejemplo, compatibles con H2 y MySQL
└── application*.properties   configuración base y perfiles h2 y mysql
especificaciones   constitución, requisitos, MER, pantallas, tareas y resultados de pruebas
```

Documentación del proyecto: [`especificaciones/requisitos.md`](especificaciones/requisitos.md) (25 requisitos con criterios Given/When/Then), [`especificaciones/mer.md`](especificaciones/mer.md), [`especificaciones/pantallas.md`](especificaciones/pantallas.md), [`especificaciones/tasks.md`](especificaciones/tasks.md), [`DISENO.md`](DISENO.md) e [`INSTRUCCIONES.md`](INSTRUCCIONES.md).

## Decisiones

- **Consola de H2 con un módulo aparte.** Spring Boot 4 separó la consola web de H2 en el módulo `spring-boot-h2console`. Se agregó como complemento de la dependencia H2 (es la herramienta de consulta del perfil `h2`, no una función nueva del sistema) y solo se activa con el perfil `h2`. Está registrado en la nota de [`especificaciones/constitucion.md`](especificaciones/constitucion.md).
- **Tablas de administración con desplazamiento interno en pantallas pequeñas.** A 375 px ninguna pantalla se desplaza horizontalmente, pero las cinco tablas de administración (géneros, películas, salas, funciones y usuarios) se desplazan **dentro de su propio contenedor** para no recortar columnas. Se acepta como cumplimiento de NFR-CIN-003.
