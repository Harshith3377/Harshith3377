# Agent instructions

## Cursor Cloud specific instructions

This repository is a Java 17 Spring Boot scaffold for a user profile key-value
store. Use the Maven Wrapper (`./mvnw`) for development commands; see
`README.md` for the standard test and run commands.

The REST API, service, and repository layers support the current in-memory user
profile management flow. Actuator and Swagger/OpenAPI endpoints should start
normally alongside the profile endpoints.

The cloud image may default to a newer JDK. This project targets Java 17; use a
Java 17 runtime for run verification when available.
