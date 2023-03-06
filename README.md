# Release tracker service (release-tracker-service)

Service is used for release manipulation
This is part of Backend assignment

# Technologies

- Java 17
- Maven
- Spring boot
- Postgres
- Docker
- Liquibase

## Project structure

Project is using multimodules
There are:

- [Domain module](release-tracker-domain)
- [Persistent module](release-tracker-postgresql-db)
- [View module](release-tracker-rest)

## Getting Started

This project is build with Maven and has support for Docker builds.

To build the project locally with Maven run `./mvnw clean install`.

To start the project locally run in folder release-tracker-app `./mvnw spring-boot:run`.

To run tests `./mvnw test`

To run checkstlye `./mvnw validate`

The default port is `8080`.

## Docker

This project is using Docker multistage

This will allow you to build and run project without any prerequisite

# Build project with Docker

If you want to build project please
execute `docker build --target build -t some-name:tag(e.g k0le/release-tracker-service:lates) . `
NOTE: this will skip tests

# Create image with Docker

If you want to build project and create image please
execute `docker build --target build -t some-name:tag(e.g k0le/release-tracker-service:lates) .`

# Docker-Compose

If you want to use docker-compose you can start separately
Postgres [docker-compose-postgres.yml](docker-compose/docker-compose-postgresql.yml) or you can start service with
docker [docker-compose-local.yml](docker-compose/docker-compose-local.yml)

# docker-compose-postgres.yml environment variables

| NAME              | DESCRIPTION       |
|-------------------|-------------------|
| POSTGRES_DATABASE | Database name     |
| POSTGRES_USER     | Database username |
| POSTGRES_PASSWORD | Database password |

# Project Environment variables

| NAME              | DESCRIPTION             |
|-------------------|-------------------------|
| POSTGRES_DB_URL   | Database connection url |
| POSTGRES_USER     | Database username       |
| POSTGRES_PASSWORD | Database password       |

There is also [env](.env) file with working environment variables example

# Checkstyle

Project is using checkstyle for code formatting
[checkstyle](codestyle/checkstyle.xml)

## Swagger

If you want to check [api documentation](http://localhost:8080/swagger-ui/index.html)

## Liquibase

Is used for generating table
[liquibase](release-tracker-postgresql-db/src/main/resources/db/changelog)

## Postman collection

If you want to manually test api use [postman collection](Release%20tracker.postman_collection.json)

## Prometheus and Graphana
[prometheus](docker-compose/docker-compose-prometheus.yml)
[graphana](docker-compose/docker-compose-graphana.yml)