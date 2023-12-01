# Funkos API Rest

<p align="center">
  <img src="images/logo.png" alt="Funko Sockets Server">
</p>

## 📝 Descripción

API Rest de Funkos desarrollada en Java 17 con Spring Boot.

## 💡 Instrucciones de uso

- ⚠ **.env:** Este fichero se deberá de crear en la carpeta raíz con los siguientes datos:
 

    API_PORT=8080
  
    DATABASE_USER=admin
  
    DATABASE_PASSWORD=123456
  
    POSTGRES_HOST=postgres-db
  
    POSTGRES_PORT=5432
  
    POSTGRES_DATABASE=shop
  
    MONGO_HOST=mongo-db
  
    MONGO_PORT=27017
  
    MONGO_DATABASE=shop
  
    PROFILE=prod


  Deberás de modificar los valores que deseas para ejecutar la aplicación y que se conecte a la base de datos. Profile indica el perfil de application-properties que se cargará

## ⚙ Herramientas

- Java 17.
- Gradle. 
- H2 Database.
- PostgreSQL.
- Spring Boot Starter Data JPA.
- Spring Boot Starter Web.
- Spring Boot Starter Cache.
- Spring Boot Starter Validation.
- Spring Boot Starter WebSocket.
- Jackson Dataformat XML.
- Spring Boot Starter Data MongoDB.
- Spring Boot Starter Thymeleaf.
- Bootstrap.
- Jackson Datatype JSR310.
- Spring Boot Starter Security.
- Spring Security Test.
- Java JWT.
- Springdoc OpenAPI Starter WebMvc UI.
- Lombok.
- Spring Boot Starter Test.

## 🗂️ Organización

- Config: Se encarga de definir la configuración de la aplicación.
  - Cors: Se encarga de definir la configuración de CORS.
  - Security: Se encarga de definir la configuración de seguridad de la aplicación.
  - Swagger: Se encarga de definir la configuración de Swagger.
  - Web: Se encarga de definir la configuración web de la aplicación.
  - Websockets: Se encarga de definir la configuración de Websocket.
  - StorageConfig: Se encarga de definir la configuración de almacenamiento de la aplicación.
- Manager Error: Se encarga de definir el manager de errores de la aplicación.
  - Exceptions: Se encarga de definir las excepciones. 
  - Model: Se encarga de definir el modelo de la excepción.
  - GlobalExceptionHanlder: Manejador de excepciones.
- Rest: Se encarga de definir todas las entidades de la aplicación junto con la paginación.
  - Controllers: Se encargan de definir los controladores.
  - Dto: Se encargan de definir los DTO.
  - Exceptions: Se encargan de definir las excepciones.
  - Mappers: Se encargan de definir los mappers.
  - Models: Se encargan de definir los modelos.
  - Repositories: Se encargan de definir los repositorios.
  - Services: Se encargan de definir los servicios.
  - Utils: Se encargan de definir las clases útiles.
- Storage: Se encarga de definir el almacenamiento de la aplicación.
  - Controller: Se encarga de definir el controlador de almacenamiento.
  - Exceptions: Se encarga de definir las excepciones.
  - Services: Se encarga de definir los servicios.
- Utils: Se encargan de definir las clases útiles que se van a utilizar en la aplicación.
- Web: Se encarga de definir la web de la aplicación.
  - Controllers: Se encargan de definir los controladores.
  - Store: Se encarga de definir la tienda.
- FunkosSpringRestApplication: El programa que ejecuta el funcionamiento de la aplicación.

# Entidades
## Categoría (`public.category`)

**Descripción:** La entidad representa las categorías a las que pueden pertenecer los Funkos en el sistema.

**Atributos:**
- **id** (bigint): Identificador único generado automáticamente.
- **active** (boolean): Indica si la categoría está activa.
- **created_at** (timestamp): Fecha y hora de creación.
- **type** (varchar): Tipo de la categoría.
- **updated_at** (timestamp): Fecha y hora de la última actualización.

## Funko (`public.funko`)

**Descripción:** La entidad representa los Funkos disponibles en el sistema.

**Atributos:**
- **id** (uuid): Identificador único del Funko.
- **created_at** (timestamp): Fecha y hora de creación.
- **image** (varchar): URL de la imagen del Funko.
- **name** (varchar): Nombre del Funko.
- **price** (double precision): Precio del Funko (debe ser mayor o igual a 0).
- **quantity** (integer): Cantidad disponible del Funko (debe ser mayor o igual a 0).
- **updated_at** (timestamp): Fecha y hora de la última actualización.
- **category_id** (bigint): Referencia a la categoría a la que pertenece.

## Usuarios (`public.users`)

**Descripción:** La entidad representa a los usuarios del sistema.

**Atributos:**
- **id** (uuid): Identificador único del usuario.
- **created_at** (timestamp): Fecha y hora de creación.
- **email** (varchar): Correo electrónico del usuario (único).
- **is_deleted** (boolean): Indica si el usuario ha sido eliminado.
- **name** (varchar): Nombre del usuario.
- **password** (varchar): Contraseña del usuario.
- **surname** (varchar): Apellido del usuario.
- **updated_at** (timestamp): Fecha y hora de la última actualización.
- **username** (varchar): Nombre de usuario único.

## Roles de Usuario (`public.user_roles`)

**Descripción:** La entidad asigna roles a los usuarios del sistema.

**Atributos:**
- **user_id** (uuid): Referencia al usuario al que se le asigna el rol.
- **roles** (varchar): Rol del usuario (puede ser 'USER' o 'ADMIN').
