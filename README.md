# Backend Pastelería "Mil Sabores"

API REST para la gestión de una pastelería: catálogo de productos y categorías, registro y autenticación de usuarios, administración de cuentas y perfil del cliente. Construida con **Spring Boot 4.1.1** y **Java 21**, con autenticación stateless mediante **JWT** y persistencia en **MySQL**.

---

## Tabla de contenidos

- [Tecnologías](#tecnologías)
- [Requisitos previos](#requisitos-previos)
- [Configuración](#configuración)
- [Ejecución](#ejecución)
- [Documentación de la API](#documentación-de-la-api)
- [Autenticación y roles](#autenticación-y-roles)
- [Endpoints](#endpoints)
- [Ejemplos de uso](#ejemplos-de-uso)
- [Modelo de datos](#modelo-de-datos)
- [Validaciones](#validaciones)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Pruebas](#pruebas)
- [Colección Bruno](#colección-bruno)
- [Notas y consideraciones](#notas-y-consideraciones)

---

## Tecnologías

| Tecnología | Uso | Versión |
| --- | --- | --- |
| Java | Lenguaje base | 21 |
| Spring Boot | Framework principal | 4.1.1 |
| Spring Web MVC | Controladores REST | (Starter) |
| Spring Security | Seguridad y autorización | (Starter) |
| Spring Data JPA | Persistencia | (Starter) |
| MySQL Connector/J | Driver de base de datos | (Starter) |
| jjwt (api / impl / jackson) | Generación y validación de JWT | 0.11.5 |
| Springdoc OpenAPI | Documentación / Swagger UI | 3.1.0 |
| Lombok | Reducción de código boilerplate | (Starter) |
| Bean Validation | Validación de DTOs | (Starter) |
| Bruno | Cliente de API para pruebas manuales | — |

---

## Requisitos previos

- **JDK 21** instalado (`java -version`).
- **Maven** (opcional: se incluye el wrapper `mvnw` / `mvnw.cmd`).
- Acceso a una instancia de **MySQL** (por defecto el proyecto apunta a una base RDS en AWS).
- Variables de entorno configuradas (ver siguiente sección).
- Opcional: [Bruno](https://www.usebruno.com/) para usar la colección incluida en `bruno/`.

---

## Configuración

Toda la configuración está en `src/main/resources/application.properties`:

```properties
spring.application.name=pasteleria
jwt.secret=${JWT_SECRET:CLAVE_SUPER_SEGURA_PARA_JWT_2026_32B}
spring.datasource.url=jdbc:mysql://pasteleria-mil-sabores.ce5w7u02ycny.us-east-1.rds.amazonaws.com:3306/db_pasteleria?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=admin
spring.datasource.password=${DB_PASSWORD:}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.path=/doc/swagger-ui.html
server.port=8080
spring.web.error.include-message=always
spring.web.error.include-stacktrace=never
```

### Variables de entorno

| Variable | Descripción | Valor por defecto |
| --- | --- | --- |
| `JWT_SECRET` | Secreto usado para firmar los tokens HS256. Debe tener al menos 32 caracteres. | `CLAVE_SUPER_SEGURA_PARA_JWT_2026_32B` (solo desarrollo) |
| `DB_PASSWORD` | Contraseña del usuario `admin` de la base de datos. | Vacía → **debe configurarse** para conectarse a la RDS |

Ejemplo en PowerShell:

```powershell
$env:JWT_SECRET = "MiClaveSecretaDeAlMenos32Caracteres!!!"
$env:DB_PASSWORD = "la-contrasenia-de-la-bd"
.\mvnw.cmd spring-boot:run
```

Ejemplo en Linux/macOS:

```bash
export JWT_SECRET="MiClaveSecretaDeAlMenos32Caracteres!!!"
export DB_PASSWORD="la-contrasenia-de-la-bd"
./mvnw spring-boot:run
```

### Base de datos

- El esquema se gestiona con `spring.jpa.hibernate.ddl-auto=update`: Hibernate crea/actualiza las tablas `categoria`, `productos` y `usuario` automáticamente.
- Para usar una base local, cambia `spring.datasource.url`, `username` y `password` en `application.properties` o sobreescríbelos por variables de entorno:
  - `SPRING_DATASOURCE_URL`
  - `SPRING_DATASOURCE_USERNAME`
  - `SPRING_DATASOURCE_PASSWORD`

### CORS

Solo se permite el origen del frontend de desarrollo `http://localhost:5173`, con los métodos `GET, POST, PUT, DELETE, OPTIONS`, las cabeceras `Authorization` y `Content-Type`, y credenciales habilitadas. Para producción, agregar el dominio correspondiente en `CorsConfig.java` y en la anotación `@CrossOrigin` de los controladores.

---

## Ejecución

```bash
# Compilar y ejecutar
./mvnw spring-boot:run

# En Windows
mvnw.cmd spring-boot:run
```

La API quedará disponible en `http://localhost:8080`.

Para generar el JAR y ejecutarlo de forma independiente:

```bash
./mvnw clean package
java -jar target/pasteleria-0.0.1-SNAPSHOT.jar
```

---

## Documentación de la API

Con la aplicación en ejecución:

| Recurso | URL |
| --- | --- |
| Swagger UI | http://localhost:8080/doc/swagger-ui.html |
| Especificación OpenAPI (JSON) | http://localhost:8080/v3/api-docs |

Ambas rutas son públicas (no requieren token). En Swagger puedes usar el botón **Authorize** e ingresar el token con el prefijo `Bearer ` para probar los endpoints protegidos.

---

## Autenticación y roles

El sistema usa **JWT stateless** (sin sesiones en el servidor):

1. El cliente llama a `POST /api/v1/auth/login` o `POST /api/v1/auth/registro`.
2. La respuesta devuelve `{ "token": "...", "tipo": "Bearer", "expiracion": 86400000 }`.
3. En cada petición protegida se envía la cabecera: `Authorization: Bearer <token>`.
4. El filtro `JwtAuthFilter` valida el token, extrae el correo (`sub`) y el rol (`rol`), y lo registra en el contexto de seguridad.

- Duración del token: **24 horas** (`86.400.000` ms).
- Contraseñas almacenadas con hash **BCrypt**.
- Roles definidos en `Rol`: `ADMIN` y `CLIENTE`. Los nuevos registros siempre se crean como `CLIENTE`.
- Regla del cupón de bienvenida: si en el registro se envía `FELICES50` (se normaliza a mayúsculas), se guarda en el perfil; cualquier otro valor se descarta.
- Correo duplicado al registrar → `409 Conflict`.

### Matriz de acceso

| Endpoint | Acceso |
| --- | --- |
| `POST /api/v1/auth/login`, `POST /api/v1/auth/registro` | Público |
| `GET /api/v1/productos/**`, `GET /api/v1/categorias/**` | Público (catálogo visible sin sesión) |
| `POST/PUT/DELETE /api/v1/productos/**`, `/api/v1/categorias/**` | Solo `ADMIN` |
| `/api/v1/usuarios/**` (todos) | Solo `ADMIN` |
| `GET/PUT /api/v1/perfil` | Cualquier usuario autenticado |
| `/doc/**`, `/v3/api-docs/**`, `/error` | Público |

---

## Endpoints

Base: `http://localhost:8080`

### Autenticación — `/api/v1/auth`

| Método | Ruta | Descripción | Respuestas |
| --- | --- | --- | --- |
| `POST` | `/login` | Inicia sesión con correo y contraseña | `200` con token · `400` validación · `401` credenciales inválidas |
| `POST` | `/registro` | Registra un nuevo cliente | `201` con token · `400` validación · `409` correo ya existente |

### Perfil — `/api/v1/perfil`

| Método | Ruta | Descripción | Respuestas |
| --- | --- | --- | --- |
| `GET` | `/perfil` | Devuelve el perfil del usuario autenticado (según el correo del token) | `200` · `401` · `404` |
| `PUT` | `/perfil` | Actualiza nombre, apellido y fecha de nacimiento | `200` · `400` validación · `401` · `404` |

### Productos — `/api/v1/productos`

| Método | Ruta | Descripción | Respuestas |
| --- | --- | --- | --- |
| `GET` | `/productos` | Lista todos los productos | `200` |
| `GET` | `/productos/{codigo}` | Busca un producto por código | `200` · `404` |
| `POST` | `/productos` | Crea un producto | `200` · `400` validación/categoría inexistente · `409` código duplicado |
| `PUT` | `/productos/{codigo}` | Actualiza un producto existente | `200` · `400` · `404` |
| `DELETE` | `/productos/{codigo}` | Elimina un producto | `200` · `404` |

> El campo `categoria` es el **nombre de la categoría** (texto). El servicio lo busca ignorando mayúsculas y tildes; si no existe responde `400 "La categoria no existe"`.

### Categorías — `/api/v1/categorias`

| Método | Ruta | Descripción | Respuestas |
| --- | --- | --- | --- |
| `GET` | `/categorias` | Lista todas las categorías | `200` |
| `GET` | `/categorias/{idCategoria}` | Busca una categoría por ID | `200` · `404` |
| `POST` | `/categorias` | Crea una categoría | `200` · `400` validación |
| `PUT` | `/categorias/{idCategoria}` | Actualiza una categoría | `200` · `400` · `404` |
| `DELETE` | `/categorias/{idCategoria}` | Elimina una categoría | `200` · `404` |

### Usuarios (admin) — `/api/v1/usuarios`

| Método | Ruta | Descripción | Respuestas |
| --- | --- | --- | --- |
| `GET` | `/usuarios` | Lista todos los usuarios | `200` |
| `GET` | `/usuarios/{idUsuario}` | Busca un usuario por ID | `200` · `404` |
| `POST` | `/usuarios` | Crea un usuario (contraseña obligatoria, rol por defecto `CLIENTE`) | `201` · `400` |
| `PUT` | `/usuarios/{idUsuario}` | Actualiza un usuario (si se omite la contraseña, se conserva la actual) | `200` · `404` |
| `DELETE` | `/usuarios/{idUsuario}` | Elimina un usuario | `200` · `404` |

---

## Ejemplos de uso

### Registro

```bash
curl -X POST http://localhost:8080/api/v1/auth/registro \
  -H "Content-Type: application/json" \
  -d '{
    "nombreUsuario": "Ana",
    "apellidoUsuario": "Pérez",
    "correoUsuario": "ana@correo.com",
    "contraseniaUsuario": "secreto123",
    "fechaNacUsuario": "1998-05-14T00:00:00.000Z",
    "cuponBienvenida": "FELICES50"
  }'
```

### Login

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"correo": "ana@correo.com", "password": "secreto123"}'
```

Respuesta:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9....",
  "tipo": "Bearer",
  "expiracion": 86400000
}
```

### Crear una categoría (requiere ADMIN)

```bash
curl -X POST http://localhost:8080/api/v1/categorias \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN_ADMIN>" \
  -d '{"nombre_categoria": "Tortas Cuadradas"}'
```

### Crear un producto (requiere ADMIN)

```bash
curl -X POST http://localhost:8080/api/v1/productos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN_ADMIN>" \
  -d '{
    "codigo": "TC001",
    "imagen": "tortacuadradachocolate.png",
    "categoria": "Tortas Cuadradas",
    "nombre": "Torta Cuadrada de Chocolate",
    "precio": 45000,
    "stock": 8,
    "descripcion": "Deliciosa torta de chocolate con capas de ganache y un toque de avellanas."
  }'
```

### Consultar el perfil

```bash
curl http://localhost:8080/api/v1/perfil \
  -H "Authorization: Bearer <TOKEN>"
```

---

## Modelo de datos

### `categoria`

| Campo | Tipo | Observaciones |
| --- | --- | --- |
| `id_categoria` | Integer | PK autoincremental |
| `nombre_categoria` | String | Obligatorio |

### `productos`

| Campo | Tipo | Observaciones |
| --- | --- | --- |
| `codigo` | String | PK (3 a 6 caracteres) |
| `imagen` | String | Referencia al archivo de imagen |
| `id_categoria` | FK | Categoría del producto (obligatoria) |
| `nombre` | String | Obligatorio |
| `precio` | Integer | En pesos chilenos |
| `stock` | Integer | Unidades disponibles |
| `descripcion` | String | Obligatoria |

### `usuario`

| Campo | Tipo | Observaciones |
| --- | --- | --- |
| `id` | Long | PK autoincremental |
| `nombreUsuario` | String | Obligatorio |
| `apellidoUsuario` | String | Obligatorio |
| `correoUsuario` | String | Obligatorio, único a nivel de lógica de negocio |
| `contraseniaUsuario` | String | Hash BCrypt; **nunca se serializa en respuestas** (`WRITE_ONLY`) |
| `fechaNacUsuario` | Date | Obligatoria, debe ser pasada |
| `cuponBienvenida` | String | Opcional (`FELICES50`) |
| `rol` | Enum | `ADMIN` o `CLIENTE` (STRING) |

---

## Validaciones

**Producto** (`ProductoRequest`):

- `codigo`: obligatorio, 3–6 caracteres.
- `imagen`, `categoria`, `nombre`, `descripcion`: obligatorios.
- `nombre`: 5–50 caracteres; `descripcion`: 10–500 caracteres.
- `precio`: 1.000 a 1.600.000.
- `stock`: 1 a 100.

**Categoría** (`CategoriaRequest`): `nombre_categoria` obligatorio, 3–50 caracteres.

**Registro** (`RegisterRequest`): nombre y apellido de 2–50 caracteres; correo con formato válido; contraseña de 6–72 caracteres; fecha de nacimiento pasada; cupón de hasta 20 caracteres.

**Login** (`LoginRequest`): correo y contraseña obligatorios.

**Perfil** (`PerfilRequest`): nombre y apellido de 2–50 caracteres; fecha de nacimiento pasada.

---

## Estructura del proyecto

```text
BackendPasteleria/
├── bruno/                         # Colección de peticiones para Bruno
│   ├── bruno.json
│   ├── categorias/                # Altas de las 10 categorías
│   └── productos/                 # Altas de los 33 productos
├── documentacion/                 # Anexos de la evaluación (Word / Excel)
├── src/main/java/example/pasteleria/
│   ├── PasteleriaApplication.java # Punto de entrada
│   ├── config/                    # Seguridad, filtro JWT, CORS, OpenAPI
│   │   ├── CorsConfig.java
│   │   ├── JwtAuthFilter.java
│   │   ├── OpenApiConfig.java
│   │   └── SecurityConfig.java
│   ├── controller/                # Auth, Categoría, Perfil, Producto, Usuario
│   ├── dto/                       # Requests y responses
│   ├── enums/Rol.java
│   ├── model/                     # Entidades JPA
│   ├── repository/                # Repositorios Spring Data
│   └── service/                   # Lógica de negocio, JWT y autenticación
├── src/main/resources/application.properties
├── src/test/java/.../PasteleriaApplicationTests.java
├── pom.xml
├── mvnw / mvnw.cmd
└── README.md
```

---

## Pruebas

```bash
./mvnw test
```

Incluye `PasteleriaApplicationTests`, que verifica que el contexto de Spring arranca correctamente (requiere acceso a la base de datos configurada).

---

## Colección Bruno

La carpeta `bruno/` contiene una colección lista para importar en [Bruno](https://www.usebruno.com/):

- `categorias/`: 10 peticiones `POST` para poblar las categorías (Todos Los Productos, Sin Azúcar, Tortas Cuadradas, etc.).
- `productos/`: 33 peticiones `POST` para poblar el catálogo (TC001, PI001, MV008, etc.).

Antes de ejecutarlas:

1. Importar la carpeta `bruno/` como colección en Bruno.
2. Reemplazar el token `Bearer ...` de cabecera por un token válido de administrador (los incluidos en los archivos `.bru` están expirados).
3. Ejecutar primero las categorías y después los productos, ya que estos referencian el nombre de una categoría existente.

---

## Notas y consideraciones

- El secreto JWT por defecto y la URL de la base son aptos solo para desarrollo; en producción deben eliminarse los valores por defecto y usar secretos/credenciales inyectadas.
- `ddl-auto=update` es práctico para desarrollo, pero en producción conviene migraciones controladas (Flyway/Liquibase).
- Los endpoints de usuarios y categorías devuelven entidades directamente; la contraseña no se expone porque está marcada como `WRITE_ONLY`, pero se recomienda migrar también a DTOs de respuesta para usuarios y categorías.
- `spring.jpa.show-sql=true` imprime las consultas en consola; desactivarlo en producción.
- La validación de unicidad de correo se realiza en el servicio (`existsByCorreoUsuario`); se recomienda añadir un índice único en la columna para garantizarlo a nivel de base de datos.
