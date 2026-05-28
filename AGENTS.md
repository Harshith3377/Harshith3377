# Agent instructions

## Cursor Cloud specific instructions


This repository is a Java 17 Spring Boot scaffold for a user profile key-value
store. Use the Maven Wrapper (`./mvnw`) for development commands; see
`README.md` for the standard test and run commands.

The REST API, service, and repository layers are intentionally contract-only at
this stage. Business endpoints should return HTTP 501 until business logic is
added, while Actuator and Swagger/OpenAPI endpoints should start normally.

The cloud image may default to a newer JDK. This project targets Java 17; use a
Java 17 runtime for run verification when available.
=======
This repository currently contains only the top-level `README.md` portfolio
document. There are no dependency manifests, setup scripts, tests, build
targets, or application services to start.

For setup, the Cursor Cloud update script is intentionally a no-op. If source
code is added later, update the repository documentation and replace the no-op
with the minimal dependency refresh command for the new stack.

