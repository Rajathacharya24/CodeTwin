# CodeTwin — AI Digital Twin for Software Systems

CodeTwin is an AI-powered software intelligence platform that analyzes a software repository and builds a living model of the system.

## Architecture

This project is built as a **Modular Monolith** using Clean Architecture principles.
See `docs/architecture/architecture.md` for more details.

## Technologies

- Java 21
- Spring Boot
- PostgreSQL
- Maven

## Setup

1. Copy `.env.example` to `.env` and fill in the values.
2. Build with `mvn clean install`.
3. Run with `mvn spring-boot:run`.