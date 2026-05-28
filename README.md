# User Profile Store

Spring Boot scaffold for a user profile key-value store.

## Requirements covered

- Java 17 target
- Layered architecture
- REST API contracts
- DTOs for request and response bodies
- Thread-safe repository design point using `ConcurrentMap`
- Separate controller, service, repository, model, and DTO packages
- Swagger/OpenAPI UI
- Spring Boot Actuator
- Simple extensible design based on interfaces

Business logic is intentionally not implemented yet. API methods currently return
HTTP 501 through a shared exception handler.

## Project layout

```text
src/main/java/com/harshith/userprofile
|-- config
|-- controller
|-- dto
|-- exception
|-- model
|-- repository
`-- service
```

## Development commands

Use the Maven Wrapper so a system Maven installation is not required.

```bash
./mvnw test
./mvnw spring-boot:run
```

After the app starts:

- Actuator health: <http://localhost:8080/actuator/health>
- OpenAPI JSON: <http://localhost:8080/v3/api-docs>
- Swagger UI: <http://localhost:8080/swagger-ui.html>
- Example API contract: <http://localhost:8080/api/v1/profiles/user-123>
