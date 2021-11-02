<h1 align="center">
  Gfood
</h1>

<p align="center">
  <a href="#-technologies">Technologies</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <a href="#-project">Project</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <a href="#-solution">Solution</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <a href="#-configuration">Configuration</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <a href="#-build-and-run">Build and Run</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <a href="#-developing">Developing</a>
</p>

<p align="center">
  <img alt="License" src="https://img.shields.io/static/v1?label=License&message=MIT&color=8257E5&labelColor=000000">
  <img src="https://img.shields.io/static/v1?label=Architecture&message=Modular Monolith&color=8257E5&labelColor=000000" alt="Modular Monolith" />
</p>

<br>

## ✨ Technologies

- [Java](https://www.oracle.com/java/technologies/downloads/)
- [Maven](https://maven.apache.org/download.cgi)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Springdoc](https://github.com/springdoc/springdoc-openapi)
- [Mysql](https://dev.mysql.com/downloads/mysql/)
- [Flyway](https://flywaydb.org/documentation/usage/maven/)
- [Spring Testing](https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html#testing-introduction)

## 💻 Project

Gfood is an app for managing food delivery which was designed following a mudular monolith architecture. This project was based on [this original monolith](https://github.com/microservices-patterns/ftgo-monolith).

## 💡 Solution

A modular monolith is an architecture more close to microsservices and therefore it has several benefits regarding reusability, dependency management, observability and teams workflow.

Gfood was designed in several modules, each with its own responsability:

- gfood-application: The app itself which depends on all modules to run.
- gfood-\*-service: Domain service which provides business functionality.
- gfood-\*-service-api: API definitions for consumers of the domain service.
- gfood-common: Common components reused by others.
- gfood-domain: Domain classes and repositories that map database structures.
- gfood-end-to-end-tests: Integration tests of all functionalities.
- gfood-flyway: Migrations that create gfood database structures.
- gfood-swagger: Swagger UI configuring all gfood APIs.

Each project generates its own jar file, which are dependencies of gfood-application. The following diagram shows gfood in terms of its services and related functionalities:

![gfood](images/gfood.png)

## 🛠️ Configuration

Gfood requires a mysql database for persisting data, so you have to configure an user and database as folows:

```
$ sudo mysql

CREATE USER 'user'@'%' IDENTIFIED BY '123456';
GRANT ALL PRIVILEGES ON *.* TO 'user'@'%' WITH GRANT OPTION;

exit

$ mysql -u user -p

CREATE DATABASE gfood;

exit
```

On building phase, the tables will be created by migrations configured using Flyway.

## 🚀 Build and Run

For building and testing, execute the script:

```sh
$ ./build-and-test.sh
```

For building and running, execute the script:

```sh
$ ./build-and-run.sh
```

The APIs will be available in Swagger-UI: http://localhost:8080/swagger-ui.html.

## 👩‍💻 Developing

Adding more functionality to gfood requires following the steps below to maintain consistency with modular monolith architecture:

- Create a new spring boot project for the new service (ex: gfood-payment-service)
- Create a new spring boot project for the service api (ex: gfood-payment-service-api)
- Import the new ServiceConfig class in `GfoodApplication`
- Create new domain classes and repositories inside `gfood-domain`
- Create any common class inside `gfood-common`
- Create new migrations inside `gfood-flyway`
- Add unit tests inside the new service project
- Add integration tests inside gfood-end-to-end-tests
- Change scripts to build and test/run the new project
