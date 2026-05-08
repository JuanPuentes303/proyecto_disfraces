# 🎭 Disfraces Rivera

Sistema web para la gestión, alquiler y venta de disfraces. Desarrollado con **Spring Boot**, **Thymeleaf** y **MySQL**, con autenticación JWT para la API REST y Spring Security para las vistas web.

---

## Tecnologías

- **Java 17**
- **Spring Boot 4.0** (Web MVC, Security, JPA, Mail, Validation)
- **Thymeleaf** — motor de plantillas para las vistas
- **MySQL 8+** — base de datos relacional
- **JWT (JJWT 0.12)** — autenticación stateless para la API
- **Maven** — gestión de dependencias y build

---

## Requisitos previos

- Java 17+
- Maven 3.8+
- MySQL 8+
- Cuenta Gmail con contraseña de aplicación (para el envío de correos)

---

## Configuración

### 1. Base de datos

Crea la base de datos y el usuario en MySQL:

```sql
CREATE DATABASE disfraces_rivera CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'disfraces_user'@'localhost' IDENTIFIED BY 'TuContraseñaSegura';
GRANT ALL PRIVILEGES ON disfraces_rivera.* TO 'disfraces_user'@'localhost';
FLUSH PRIVILEGES;
```

### 2. Variables de entorno

Copia `application.properties.example` a `application.properties` y ajusta los valores, o define las siguientes variables de entorno antes de ejecutar la aplicación:

| Variable            | Descripción                                     |
|---------------------|-------------------------------------------------|
| `DB_URL`            | URL JDBC de la base de datos                    |
| `DB_USERNAME`       | Usuario de MySQL                                |
| `DB_PASSWORD`       | Contraseña de MySQL                             |
| `MAIL_USERNAME`     | Correo Gmail del remitente                      |
| `MAIL_PASSWORD`     | Contraseña de aplicación de Gmail               |
| `ADMIN_EMAIL`       | Correo que recibe notificaciones de reservas    |
| `JWT_SECRET`        | Clave secreta para firmar tokens JWT (mín. 32 chars) |

---

## Ejecución

```bash
# Clonar el repositorio
git clone <url-del-repo>
cd disfraces-rivera

# Compilar y ejecutar
./mvnw spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

Al iniciar por primera vez, `DataInitializer` crea automáticamente los roles (`ROLE_USER`, `ROLE_ADMIN`) y las categorías de disfraces predeterminadas.

---

## Estructura del proyecto

```
src/
└── main/
    ├── java/com/disfracesrivera/
    │   ├── config/          # Seguridad, JWT, CORS, inicialización de datos
    │   ├── controller/      # Controladores MVC (vistas Thymeleaf)
    │   │   └── api/         # Controladores REST (JSON)
    │   ├── dto/             # Objetos de transferencia de datos
    │   ├── exception/       # Manejo global de excepciones
    │   ├── model/           # Entidades JPA
    │   ├── repository/      # Repositorios Spring Data JPA
    │   └── service/         # Lógica de negocio
    └── resources/
        ├── static/          # CSS, JS, imágenes estáticas
        ├── templates/       # Plantillas Thymeleaf
        ├── messages*.properties  # Internacionalización (es / en)
        └── application.properties
uploads/
└── disfraces/               # Imágenes subidas por el administrador
docs/
└── database.md              # Documentación del esquema de base de datos
```

---

## Módulos principales

### Catálogo público
- Listado de disfraces con filtros por categoría, talla y género
- Vista de detalle con galería de imágenes
- Disponibilidad en tiempo real

### Reservas
- Usuarios registrados pueden alquilar o comprar disfraces
- Validación de fechas y disponibilidad
- Notificación por correo electrónico al usuario y al administrador
- Seguimiento del estado: `ACTIVA`, `FINALIZADA`, `CANCELADA`, `VENCIDA`

### Panel de administración
- CRUD completo de disfraces y categorías
- Gestión de imágenes (carga y eliminación)
- Visualización y gestión de todas las reservas
- Acceso restringido a usuarios con `ROLE_ADMIN`

### Autenticación
- Registro e inicio de sesión web (formularios Thymeleaf + Spring Security)
- API REST con autenticación JWT para integración con clientes externos

---

## API REST

Base URL: `/api`

| Método | Endpoint                      | Descripción                          | Auth requerida |
|--------|-------------------------------|--------------------------------------|----------------|
| POST   | `/api/auth/login`             | Obtener token JWT                    | No             |
| GET    | `/api/disfraces`              | Listar disfraces públicos            | No             |
| GET    | `/api/disfraces/{id}`         | Detalle de un disfraz                | No             |
| POST   | `/api/reservas`               | Crear una reserva                    | JWT            |
| GET    | `/api/reservas/mis-reservas`  | Reservas del usuario autenticado     | JWT            |
| POST   | `/api/admin/disfraces`        | Crear disfraz                        | JWT (ADMIN)    |
| PUT    | `/api/admin/disfraces/{id}`   | Editar disfraz                       | JWT (ADMIN)    |
| DELETE | `/api/admin/disfraces/{id}`   | Eliminar disfraz                     | JWT (ADMIN)    |
| PATCH  | `/api/admin/reservas/{id}/estado` | Cambiar estado de reserva        | JWT (ADMIN)    |

Para más detalle consulta [`docs/endpoints.md`](docs/endpoints.md).

---

## Documentación adicional

- [Esquema de base de datos](docs/database.md)
- [Endpoints de la API](docs/endpoints.md)

---

## Contribuir

1. Haz un fork del repositorio
2. Crea una rama para tu feature: `git checkout -b feature/mi-feature`
3. Realiza tus cambios y haz commit: `git commit -m "feat: descripción del cambio"`
4. Sube tu rama: `git push origin feature/mi-feature`
5. Abre un Pull Request

---

## Licencia

Uso privado — Disfraces Rivera.