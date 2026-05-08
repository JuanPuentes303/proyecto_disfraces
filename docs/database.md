# 📦 Documentación de Base de Datos — Disfraces Rivera

**Motor:** MySQL 8+  
**Base de datos:** `disfraces_rivera`  
**Zona horaria:** `America/Bogota`  
**Charset recomendado:** `utf8mb4`

---

## Configuración inicial

```sql
CREATE DATABASE IF NOT EXISTS disfraces_rivera
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'disfraces_user'@'localhost'
  IDENTIFIED BY 'Disfraces123*';

GRANT ALL PRIVILEGES ON disfraces_rivera.* TO 'disfraces_user'@'localhost';
FLUSH PRIVILEGES;

USE disfraces_rivera;
```

---

## Diagrama de tablas

```
categorias ──< disfraces ──< imagenes_disfraz
                    │
                    └──< reservas >── usuarios >──< usuario_roles >── roles
```

---

## Tablas

### `roles`

Roles del sistema para control de acceso.

```sql
CREATE TABLE roles (
  id     BIGINT       NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(50)  NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_roles_nombre (nombre)
);
```

| Columna | Tipo        | Restricciones            | Descripción         |
|---------|-------------|--------------------------|---------------------|
| id      | BIGINT      | PK, AUTO_INCREMENT       | Identificador único |
| nombre  | VARCHAR(50) | NOT NULL, UNIQUE         | Ej: `ROLE_USER`, `ROLE_ADMIN` |

**Datos iniciales (cargados por `DataInitializer`):**

```sql
INSERT INTO roles (nombre) VALUES ('ROLE_USER'), ('ROLE_ADMIN');
```

---

### `usuarios`

Registro de usuarios del sistema (clientes y administradores).

```sql
CREATE TABLE usuarios (
  id              BIGINT        NOT NULL AUTO_INCREMENT,
  nombre          VARCHAR(100)  NOT NULL,
  apellido        VARCHAR(100)  DEFAULT NULL,
  correo          VARCHAR(150)  NOT NULL,
  telefono        VARCHAR(20)   DEFAULT NULL,
  password        VARCHAR(255)  NOT NULL,
  activo          TINYINT(1)    NOT NULL DEFAULT 1,
  fecha_creacion  DATETIME      NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_usuarios_correo (correo)
);
```

| Columna        | Tipo         | Restricciones      | Descripción                           |
|----------------|--------------|--------------------|---------------------------------------|
| id             | BIGINT       | PK, AUTO_INCREMENT | Identificador único                   |
| nombre         | VARCHAR(100) | NOT NULL           | Nombre del usuario                    |
| apellido       | VARCHAR(100) |                    | Apellido del usuario                  |
| correo         | VARCHAR(150) | NOT NULL, UNIQUE   | Email (usado como username)           |
| telefono       | VARCHAR(20)  |                    | Teléfono de contacto                  |
| password       | VARCHAR(255) | NOT NULL           | Contraseña encriptada con BCrypt      |
| activo         | TINYINT(1)   | NOT NULL           | `1` = activo, `0` = deshabilitado     |
| fecha_creacion | DATETIME     | NOT NULL           | Fecha y hora del registro             |

---

### `usuario_roles`

Tabla de unión para la relación muchos a muchos entre `usuarios` y `roles`.

```sql
CREATE TABLE usuario_roles (
  usuario_id BIGINT NOT NULL,
  rol_id     BIGINT NOT NULL,
  PRIMARY KEY (usuario_id, rol_id),
  CONSTRAINT fk_ur_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios (id),
  CONSTRAINT fk_ur_rol     FOREIGN KEY (rol_id)     REFERENCES roles (id)
);
```

| Columna    | Tipo   | Restricciones | Descripción             |
|------------|--------|---------------|-------------------------|
| usuario_id | BIGINT | PK, FK        | Referencia a `usuarios` |
| rol_id     | BIGINT | PK, FK        | Referencia a `roles`    |

---

### `categorias`

Categorías para clasificar los disfraces del catálogo.

```sql
CREATE TABLE categorias (
  id          BIGINT       NOT NULL AUTO_INCREMENT,
  nombre      VARCHAR(100) NOT NULL,
  descripcion VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_categorias_nombre (nombre)
);
```

| Columna     | Tipo         | Restricciones      | Descripción                  |
|-------------|--------------|--------------------|------------------------------|
| id          | BIGINT       | PK, AUTO_INCREMENT | Identificador único          |
| nombre      | VARCHAR(100) | NOT NULL, UNIQUE   | Nombre de la categoría       |
| descripcion | VARCHAR(255) |                    | Descripción breve            |

**Datos iniciales (cargados por `DataInitializer`):**

```sql
INSERT INTO categorias (nombre, descripcion) VALUES
  ('Terror',      'Disfraces de miedo, Halloween y personajes oscuros'),
  ('Infantil',    'Disfraces para niños y niñas'),
  ('Superhéroes', 'Disfraces de héroes y personajes de acción'),
  ('Princesas',   'Disfraces de princesas y fantasía'),
  ('Animales',    'Disfraces de animales'),
  ('Época',       'Disfraces antiguos, clásicos o históricos');
```

---

### `disfraces`

Catálogo principal de disfraces disponibles para alquiler o compra.

```sql
CREATE TABLE disfraces (
  id               BIGINT         NOT NULL AUTO_INCREMENT,
  nombre           VARCHAR(150)   NOT NULL,
  descripcion      TEXT           DEFAULT NULL,
  talla            VARCHAR(50)    DEFAULT NULL,
  genero           VARCHAR(50)    DEFAULT NULL,
  precio_alquiler  DECIMAL(10,2)  DEFAULT NULL,
  precio_compra    DECIMAL(10,2)  DEFAULT NULL,
  disponible_venta TINYINT(1)     NOT NULL DEFAULT 1,
  activo           TINYINT(1)     NOT NULL DEFAULT 1,
  fecha_creacion   DATETIME       NOT NULL,
  categoria_id     BIGINT         DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_disfraces_categoria FOREIGN KEY (categoria_id) REFERENCES categorias (id)
);
```

| Columna          | Tipo          | Restricciones      | Descripción                                      |
|------------------|---------------|--------------------|--------------------------------------------------|
| id               | BIGINT        | PK, AUTO_INCREMENT | Identificador único                              |
| nombre           | VARCHAR(150)  | NOT NULL           | Nombre del disfraz                               |
| descripcion      | TEXT          |                    | Descripción detallada                            |
| talla            | VARCHAR(50)   |                    | Talla disponible (ej: `S`, `M`, `L`, `XL`)      |
| genero           | VARCHAR(50)   |                    | Género (ej: `Hombre`, `Mujer`, `Unisex`)         |
| precio_alquiler  | DECIMAL(10,2) |                    | Precio por alquiler en COP                       |
| precio_compra    | DECIMAL(10,2) |                    | Precio de venta en COP                           |
| disponible_venta | TINYINT(1)    | NOT NULL           | `1` = disponible para compra                     |
| activo           | TINYINT(1)    | NOT NULL           | `1` = visible en catálogo                        |
| fecha_creacion   | DATETIME      | NOT NULL           | Fecha de ingreso al sistema                      |
| categoria_id     | BIGINT        | FK (nullable)      | Referencia a `categorias`                        |

---

### `imagenes_disfraz`

Imágenes asociadas a cada disfraz. Un disfraz puede tener múltiples imágenes.

```sql
CREATE TABLE imagenes_disfraz (
  id          BIGINT       NOT NULL AUTO_INCREMENT,
  url_imagen  VARCHAR(255) NOT NULL,
  principal   TINYINT(1)   NOT NULL DEFAULT 0,
  disfraz_id  BIGINT       NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_imagenes_disfraz FOREIGN KEY (disfraz_id) REFERENCES disfraces (id)
    ON DELETE CASCADE
);
```

| Columna    | Tipo         | Restricciones      | Descripción                                               |
|------------|--------------|--------------------|-----------------------------------------------------------|
| id         | BIGINT       | PK, AUTO_INCREMENT | Identificador único                                       |
| url_imagen | VARCHAR(255) | NOT NULL           | Ruta relativa al archivo (ej: `uploads/disfraces/uuid.jpg`) |
| principal  | TINYINT(1)   | NOT NULL           | `1` = imagen principal mostrada en catálogo               |
| disfraz_id | BIGINT       | FK, NOT NULL       | Referencia a `disfraces` (cascade delete)                 |

> La relación tiene `CascadeType.ALL` y `orphanRemoval = true` en JPA, por lo que al eliminar un disfraz se eliminan todas sus imágenes automáticamente.

---

### `reservas`

Registro de reservas (alquileres o compras) realizadas por los usuarios.

```sql
CREATE TABLE reservas (
  id              BIGINT      NOT NULL AUTO_INCREMENT,
  fecha_inicio    DATE        NOT NULL,
  fecha_fin       DATE        NOT NULL,
  tipo            VARCHAR(30) NOT NULL,
  estado          VARCHAR(30) NOT NULL DEFAULT 'ACTIVA',
  observaciones   TEXT        DEFAULT NULL,
  fecha_creacion  DATETIME    NOT NULL,
  usuario_id      BIGINT      NOT NULL,
  disfraz_id      BIGINT      NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_reservas_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios (id),
  CONSTRAINT fk_reservas_disfraz FOREIGN KEY (disfraz_id) REFERENCES disfraces (id)
);
```

| Columna        | Tipo        | Restricciones | Descripción                                  |
|----------------|-------------|---------------|----------------------------------------------|
| id             | BIGINT      | PK, AUTO_INCREMENT | Identificador único                    |
| fecha_inicio   | DATE        | NOT NULL      | Fecha de inicio del alquiler / compra        |
| fecha_fin      | DATE        | NOT NULL      | Fecha de fin (igual a inicio para compras)   |
| tipo           | VARCHAR(30) | NOT NULL      | Enum: `ALQUILER` \| `COMPRA`                 |
| estado         | VARCHAR(30) | NOT NULL      | Enum: `ACTIVA` \| `FINALIZADA` \| `CANCELADA` \| `VENCIDA` |
| observaciones  | TEXT        |               | Notas adicionales del usuario                |
| fecha_creacion | DATETIME    | NOT NULL      | Fecha y hora en que se creó la reserva       |
| usuario_id     | BIGINT      | FK, NOT NULL  | Usuario que realizó la reserva               |
| disfraz_id     | BIGINT      | FK, NOT NULL  | Disfraz reservado                            |

**Valores del enum `TipoReserva`:**

| Valor     | Descripción                          |
|-----------|--------------------------------------|
| `ALQUILER`| El usuario alquila por un período    |
| `COMPRA`  | El usuario compra el disfraz         |

**Valores del enum `EstadoReserva`:**

| Valor        | Descripción                                    |
|--------------|------------------------------------------------|
| `ACTIVA`     | Reserva vigente                                |
| `FINALIZADA` | Reserva completada correctamente               |
| `CANCELADA`  | Reserva cancelada por el usuario o administrador |
| `VENCIDA`    | Reserva no devuelta en el plazo acordado       |

---

## Consultas útiles

### Ver todos los disfraces activos con su categoría

```sql
SELECT d.id, d.nombre, d.talla, d.genero, d.precio_alquiler, d.precio_compra, c.nombre AS categoria
FROM disfraces d
LEFT JOIN categorias c ON d.categoria_id = c.id
WHERE d.activo = 1
ORDER BY d.nombre;
```

### Ver reservas activas con datos del usuario y disfraz

```sql
SELECT r.id, u.nombre, u.correo, d.nombre AS disfraz, r.tipo, r.estado, r.fecha_inicio, r.fecha_fin
FROM reservas r
JOIN usuarios u ON r.usuario_id = u.id
JOIN disfraces d ON r.disfraz_id = d.id
WHERE r.estado = 'ACTIVA'
ORDER BY r.fecha_inicio;
```

### Verificar disponibilidad de un disfraz en un rango de fechas

```sql
SELECT COUNT(*) AS conflictos
FROM reservas
WHERE disfraz_id = ?
  AND estado IN ('ACTIVA')
  AND fecha_inicio <= ?   -- fecha_fin solicitada
  AND fecha_fin   >= ?;   -- fecha_inicio solicitada
```

### Imagen principal de cada disfraz

```sql
SELECT d.id, d.nombre, i.url_imagen
FROM disfraces d
JOIN imagenes_disfraz i ON i.disfraz_id = d.id AND i.principal = 1
WHERE d.activo = 1;
```

### Roles de un usuario

```sql
SELECT u.correo, r.nombre AS rol
FROM usuarios u
JOIN usuario_roles ur ON ur.usuario_id = u.id
JOIN roles r ON r.id = ur.rol_id
WHERE u.correo = 'correo@ejemplo.com';
```

---

## Notas importantes

- Hibernate gestiona el DDL con `spring.jpa.hibernate.ddl-auto=update`. En producción se recomienda cambiar a `validate` y usar migraciones con **Flyway** o **Liquibase**.
- Las contraseñas se almacenan hasheadas con **BCrypt** — nunca en texto plano.
- Las credenciales de la base de datos y el secreto JWT **no deben ir en `application.properties` en producción**; usa variables de entorno o un gestor de secretos.
- El campo `activo` en `disfraces` y `usuarios` permite deshabilitar registros sin eliminarlos (soft delete).