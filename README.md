# `db2spring`: Database to Spring Code Generator

**db2spring** is a Java code generation tool that automates the repetitive task of generating boilerplate code for
Spring Boot applications from an existing relational database schema or raw SQL `CREATE TABLE` statements.

By defining your database connection and desired class mappings in an XML configuration file, **db2spring** generates
ready-to-use Spring Boot components, including Entities, Repositories, Services, Controllers, and various Data Transfer
Objects (DTOs).

The recommended way to use **db2spring** is through its Maven plugin:

```bash
mvn db2spring:generate
```

---

## Features

- **Database Support:** Load schema information from a live database connection using JDBC or directly from SQL
  `CREATE TABLE` statements.
- **Maven Plugin:** Generate code directly from your Maven project using `mvn db2spring:generate`.
- **Customizable Generation:** Control which components are generated, their output directories, package structure, and
  class suffixes.
- **Case Conversion & Inflection:** Automatically converts SQL naming conventions such as `user_settings` into
  appropriate Java names such as `UserSettings` and `UserSetting`.
- **Plugin Support:** Conditional generation for popular libraries and frameworks such as Lombok, MapStruct, and Spring
  Boot Validation.
- **Type Overrides:** Define custom mappings from SQL types to specific Java types, such as `TIMESTAMP` to
  `java.time.Instant`.
- **Multiple Project Structures:** Supports different output layouts such as `layered`, `layeredDto`, `selfContained`,
  and `featuredGroup`.

---

## Module Structure

The project is structured as a multi-module Maven project:

| Module                       | Description                                                                   | Key Responsibilities                                                               |
|:-----------------------------|:------------------------------------------------------------------------------|:-----------------------------------------------------------------------------------|
| **`db2spring-commons`**      | Shared data models and utility classes used across the project.               | Models, utility classes, type mappings, and database inspection logic.             |
| **`db2spring-core`**         | Main code generation engine and Freemarker templates.                         | Code generation, template processing, SQL parsing, and Spring component templates. |
| **`db2spring-generator`**    | Standalone CLI entry point for db2spring.                                     | Configuration loading, CLI execution, and generator orchestration.                 |
| **`db2spring-maven-plugin`** | Maven plugin integration for running db2spring directly from a Maven project. | Provides the `db2spring:generate` Maven goal and configuration handling.           |

---

## ️ Setup and Usage

## Prerequisites

- **Java 11**
- **Maven 3.x**

## 1. Add the Maven Plugin

Add the following plugin to your project's `pom.xml`:

```xml

<build>
    <plugins>
        <plugin>
            <groupId>io.github.errolitolopez</groupId>
            <artifactId>db2spring-maven-plugin</artifactId>
            <version>1.2.0</version>
        </plugin>
    </plugins>
</build>
```

The plugin is available from Maven Central, so no additional repository configuration is required.

## 2. Configure `db2spring-config.xml`

Create a `db2spring-config.xml` file in the root directory of your Maven project.

The Maven plugin uses `db2spring-config.xml` by default.

You can also specify a custom configuration file:

```bash
mvn db2spring:generate -Ddb2spring.config=my-db2spring-config.xml
```

The following XML sections can be used to configure db2spring:

| XML Tag                 | Purpose                                                                                                             |
|:------------------------|:--------------------------------------------------------------------------------------------------------------------|
| `<database-connection>` | JDBC URL, username, password, and driver information.                                                               |
| `<sql-file>` or `<sql>` | Specifies the source of table metadata (either a path to a DDL file or inline SQL).                                 |
| `<project-info>`        | Defines the base Maven coordinates (`group-id`, `artifact-id`) and the project's root package.                      |
| `<plugin>`              | Enables features like **Lombok**, **Mapstruct**, and **SpringBootStarterValidation**.                               |
| `<table-mapping>`       | Maps a database table name to the desired generated Java class name.                                                |
| `<generator>`           | Fine-tunes the output for specific file types (e.g., `type="controller"`, `generate="true"` or `generate="false"`). |
| `<file-structure>`      | Choose the output file and folder layout (e.g., `layered`, `layeredDto`, `selfContained` or `featuredGroup`).       |

## 3. Generate the Code

With `db2spring-config.xml` in your project root, simply run:

```bash
mvn db2spring:generate
```

Or specify a custom configuration file:

```bash
mvn db2spring:generate -Ddb2spring.config=db2spring-config.xml
```

The generator will load the database metadata and generate the configured Spring Boot components.

Example output:

```text
[INFO] Running db2spring generator...
[INFO] Starting db2spring generator
[INFO] Loading tables from database...
[INFO] Generated: ../src/main/java/com/example/myapi/entity/User.java
[INFO] Generated: ../src/main/java/com/example/myapi/repository/UserRepository.java
[INFO] Generated: ../src/main/java/com/example/myapi/service/UserService.java
[INFO] Generated: ../src/main/java/com/example/myapi/controller/UserController.java
[INFO] db2spring generation completed successfully!
```

> **Note:** When using a database connection and an external JDBC driver, make sure the driver JAR is correctly located
> and referenced in your `db2spring-config.xml`. db2spring dynamically loads the configured JDBC driver.

---

## Sample `db2spring-config.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<db2spring>
  <database-connection>
    <url>jdbc:postgresql://localhost:5432/sample_schema</url>
    <user>postgres</user>
    <password>password</password>
    <driver-class>org.postgresql.Driver</driver-class>
    <driver-jar>postgresql-42.7.12.jar</driver-jar>
  </database-connection>

  <project-info>
    <group-id>com.exist.energy</group-id>
    <artifact-id>myapi</artifact-id>
    <project-name>myapi</project-name>
  </project-info>

  <plugin name="SpringBootStarterValidation"/>
  <plugin name="Lombok"/>
  <plugin name="Mapstruct"/>

  <table table-name="app_user" class-name="AppUser"/>

  <type-override column-name="" sql-type="DATETIME" java-type="Instant"/>

  <file-structure>layeredDto</file-structure>

  <generator generate="true" type="entity" sub-package="domain" output-dir="src/main/java"/>
  <generator generate="true" type="repository" sub-package="repository" output-dir="src/main/java"/>
  <generator generate="true" type="dto" sub-package="dto" output-dir="src/main/java"/>
  <generator generate="true" type="dto-create" sub-package="dto" output-dir="src/main/java"/>
  <generator generate="true" type="dto-update" sub-package="dto" output-dir="src/main/java"/>
  <generator generate="true" type="dto-response" sub-package="dto" output-dir="src/main/java" suffix="ResponseDto"/>
  <generator generate="true" type="dto-request" sub-package="dto" output-dir="src/main/java" suffix="FilterDto"/>
  <generator generate="true" type="mapper" sub-package="service.mapper" output-dir="src/main/java"/>
  <generator generate="true" type="service" sub-package="service" output-dir="src/main/java"/>
  <generator generate="true" type="controller" sub-package="web.rest" output-dir="src/main/java" suffix="Resource"/>
  <generator generate="true" type="spec-builder" sub-package="shared" output-dir="src/main/java"/>
  <generator generate="true" type="test-setup" sub-package="data" output-dir="src/test/java"/>
  <generator generate="true" type="test-service" sub-package="service" output-dir="src/test/java"/>
  <generator generate="true" type="test-controller" sub-package="web.rest" output-dir="src/test/java"/>
  <generator generate="true" type="adoc" sub-package="" output-dir="src/asciidoc"/>
</db2spring>
```

---

## Quick Start

For a Maven project with `db2spring-config.xml` in the project root:

```bash
mvn db2spring:generate
```

For a custom configuration file:

```bash
mvn db2spring:generate -Ddb2spring.config=my-db2spring-config.xml
```

That's it. db2spring will connect to the configured database, inspect the schema, and generate the configured Spring
Boot components.