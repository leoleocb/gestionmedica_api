# Gestión Médica API

API REST para la gestión de una clínica: pacientes, médicos, citas, expedientes, recetas, medicamentos, alergias y enfermedades. Incluye autenticación con **JWT** y control de acceso por roles (**ADMIN**, **MÉDICO**, **PACIENTE**).

Proyecto desarrollado durante mi formación en Cibertec. Se conecta con un frontend en Angular (ver enlace al final).

## Tecnologías

- Java 17
- Spring Boot 3.5 (Web, Data JPA, Security, Validation)
- MySQL 8
- JWT (jjwt 0.11.5)
- Lombok
- Maven

## Arquitectura

```
com.cibertec.gestionmedica
├── config       # CORS e inicialización de datos (roles y usuario admin)
├── controller   # Endpoints REST
├── dto          # Objetos de transferencia (login, registro, respuestas)
├── exception    # Manejo global de errores (@ControllerAdvice)
├── model        # Entidades JPA
├── repository   # Repositorios Spring Data
├── security     # Filtro JWT, configuración de seguridad y roles
└── service      # Lógica de autenticación y médicos
```

## Funcionalidades

- Registro e inicio de sesión con token JWT (expira en 1 hora).
- Roles con permisos distintos por endpoint.
- Gestión de citas con reglas de negocio:
  - Máximo 3 citas por paciente al día.
  - Solo de lunes a viernes, entre 08:00 y 17:00.
  - No se permite cancelar con menos de 2 horas de anticipación.
  - Consulta de horarios disponibles por médico y fecha.
- Expedientes médicos, recetas con sus ítems, alergias y enfermedades.
- Validaciones y respuestas de error en formato JSON.

## Endpoints principales

| Recurso | Ruta base | Acceso |
|---|---|---|
| Autenticación | `/api/auth/**` | Público |
| Pacientes | `/api/pacientes` | ADMIN (perfil: PACIENTE) |
| Médicos | `/api/medicos` | ADMIN (consultas por especialidad: todos los roles) |
| Citas | `/api/citas` | PACIENTE, MÉDICO, ADMIN según la operación |
| Expedientes | `/api/expedientes` | MÉDICO, ADMIN (PACIENTE solo los suyos) |
| Recetas | `/api/recetas` | MÉDICO crea, ADMIN y MÉDICO consultan |
| Medicamentos | `/api/medicamentos` | Lectura pública, escritura ADMIN |
| Alergias / Enfermedades | `/api/alergias`, `/api/enfermedades` | MÉDICO y ADMIN escriben |

## Cómo ejecutarlo

### Requisitos

- JDK 17
- MySQL 8 en ejecución
- Maven (o usar el wrapper `./mvnw` incluido)

### Pasos

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/leoleocb/gestionmedica_api.git
   cd gestionmedica_api
   ```
2. Crear la base de datos:
   ```sql
   CREATE DATABASE gestionmedica_db;
   ```
3. Configurar las variables de entorno (si no se definen, se usan valores de desarrollo local):

   | Variable | Descripción |
   |---|---|
   | `DB_URL` | URL JDBC de MySQL |
   | `DB_USERNAME` | Usuario de la base de datos |
   | `DB_PASSWORD` | Contraseña de la base de datos |
   | `JWT_SECRET` | Clave para firmar los tokens (mínimo 32 caracteres) |

4. Ejecutar:
   ```bash
   ./mvnw spring-boot:run
   ```
5. La API queda disponible en `http://localhost:8080`.

Al iniciar por primera vez se crean los roles y un usuario administrador de desarrollo (`admin@example.com`). **Cambia esa contraseña si lo usas fuera de un entorno local.**

### Probar la autenticación

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","password":"<contraseña del admin>"}'
```

Usa el token devuelto en las siguientes peticiones: `Authorization: Bearer <token>`.

## Mejoras pendientes

- Mover más lógica de negocio de los controllers a una capa de servicios.
- Usar DTOs en todas las respuestas en lugar de devolver entidades.
- Agregar pruebas unitarias y de integración.
- Documentar la API con Swagger/OpenAPI.

## Frontend

Cliente en Angular: **[agregar enlace al repositorio]**

## Autor

**Leandro Coba** — Estudiante de Ingeniería de Sistemas (UPC)
[LinkedIn](https://www.linkedin.com/in/leandro-david-coba-huayas-372426355/) · ldch07@outlook.com
