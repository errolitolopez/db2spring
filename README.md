# `db2spring`: Database to Spring Code Generator

**db2spring** is a powerful, command-line interface (CLI) tool designed to automate the repetitive task of generating boilerplate code for Spring Boot applications from an existing relational database schema or raw SQL `CREATE TABLE` statements.

By defining your database connection and desired class mappings in an XML configuration file, **db2spring** generates complete, ready-to-use Spring Boot components, including Entities, Repositories, Services, Controllers, and various Data Transfer Objects (DTOs).

-----

## ✨ Features

* **Flexible Source:** Load schema information from a live database connection (via JDBC) or directly from SQL `CREATE TABLE` statements in a file or string.
* **Customizable Generation:** Fine-tune which files (e.g., `controller`, `service`, `entity`) are generated, their output directories, and their class suffixes.
* **Case Conversion & Inflection:** Automatically handles SQL naming conventions (e.g., `user_settings`) and converts them to appropriate Java names (e.g., `UserSettings`, `UserSetting`) using SmartString utilities.
* **Plugin Support:** Conditional generation for popular plugins like Lombok, MapStruct, and Spring Boot Validation.
* **Type Overrides:** Define custom mappings for SQL types to specific Java types (e.g., `TIMESTAMP` -\> `java.time.Instant`).

-----

## 📦 Module Structure

The project is structured as a multi-module Maven project to separate concerns between core logic, shared data models, and the executable CLI.

| Module | Description | Key Responsibilities |
| :--- | :--- | :--- |
| **`db2spring-commons`** | Contains all **shared data models, core utilities, and type mappers**. It has no dependencies on other project modules. | Models (`Table`, `Column`, `Db2springXml`), Utility classes (`SmartStringUtil`, `CollectionUtil`, `MapUtil`), Database inspection logic (`DatabaseLoader`). |
| **`db2spring-core`** | Houses the **main generation engine** and all Freemarker templates (`.ftl`) for the output files. | Generation logic (`Db2springGenerator`), Template engine (`FreeMarkerWriter`), SQL parsing (`SqlParser`), and all templates for Spring components (`entity.ftl`, `controller.ftl`, etc.). |
| **`db2spring-generator`** | The **executable CLI module** that provides the main entry point for the user. It loads the configuration, coordinates the table loading, and triggers the core generator. | CLI execution (`Db2springApplication`), Configuration loading (`Db2springAppRunner`), Generator orchestration (`Db2springRunner`), and configuration file resolution. |

-----

## ⚙️ Setup and Usage

### Prerequisites

1.  **Java 17+**
2.  **Maven 3.x**

### 1\. Build the Project

Use Maven to build the entire project. The `db2spring-generator` module is configured to produce a **fat JAR** (using the `maven-shade-plugin`) for easy command-line execution.

```bash
mvn clean install
```

### 2\. Configure `config.xml`

The CLI tool requires an XML configuration file to define the project structure, database source, and generation options.

A typical configuration file, like the example provided, includes the following sections:

| XML Tag                 | Purpose                                                                                                             |
|:------------------------|:--------------------------------------------------------------------------------------------------------------------|
| `<database-connection>` | JDBC URL, username, password, and driver information.                                                               |
| `<sql-file>` or `<sql>` | Specifies the source of table metadata (either a path to a DDL file or inline SQL).                                 |
| `<project-info>`        | Defines the base Maven coordinates (`group-id`, `artifact-id`) and the project's root package.                      |
| `<plugin>`              | Enables features like **Lombok**, **Mapstruct**, and **SpringBootStarterValidation**.                               |
| `<table-mapping>`       | Maps a database table name to the desired generated Java class name.                                                |
| `<generator>`           | Fine-tunes the output for specific file types (e.g., `type="controller"`, `generate="true"` or `generate="false"`). |
| `<file-structure>`      | Choose the output file and folder layout (e.g., `layered`, `layeredDto`, `selfContained` or `featuredGroup`).       |

### 3\. Run the Generator

Execute the fat JAR, passing the path to your configuration file as the first argument:

```bash
java -jar db2spring-generator.jar path/to/your/config.xml
```

If successful, the tool will log the generation process and then exit with a success message.

> **Note:** If you are using a database connection and an external JDBC driver, ensure the driver JAR is correctly located and referenced in your `config.xml` or made available to the application, as the tool uses custom logic to load the driver dynamically.


-----

## 📄 Sample `config.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<db2spring>
    <database-connection>
        <url>jdbc:mysql://localhost:3306/sample_schema</url>
        <user>root</user>
        <password>123qwe</password>
        <driver-class>com.mysql.cj.jdbc.Driver</driver-class>
        <driver-jar>path/to/your/driver.jar</driver-jar>
    </database-connection>

     <sql-file src="path/to/your/file.sql"/>
     <sql>sql create statement</sql>

    <project-info>
        <group-id>com.example</group-id>
        <artifact-id>db2spring</artifact-id>
        <project-name>db2spring</project-name>
    </project-info>

    <plugin name="SpringBootStarterValidation"/>
    <plugin name="Lombok"/>
    <plugin name="Mapstruct"/>

    <table table-name="users" class-name="User"/>

    <type-override column-name="" sql-type="DATETIME" java-type="Instant"/>

    <generator generate="true" type="entity" sub-package="entity" output-dir="../src/main/java"/>
    <generator generate="true" type="repository" sub-package="repository" output-dir="../src/main/java"/>
    <generator generate="true" type="dto" sub-package="dto" output-dir="../src/main/java"/>
    <generator generate="true" type="dto-create" sub-package="dto" output-dir="../src/main/java"/>
    <generator generate="true" type="dto-update" sub-package="dto" output-dir="../src/main/java"/>
    <generator generate="true" type="dto-response" sub-package="dto" output-dir="../src/main/java"/>
    <generator generate="true" type="dto-request" sub-package="dto" output-dir="../src/main/java"/>
    <generator generate="true" type="mapper" sub-package="mapper" output-dir="../src/main/java"/>
    <generator generate="true" type="service" sub-package="service" output-dir="../src/main/java"/>
    <generator generate="true" type="service-impl" sub-package="service.impl" output-dir="../src/main/java"/>
    <generator generate="true" type="controller" sub-package="controller" output-dir="../src/main/java"/>
    <generator generate="true" type="spec-builder" sub-package="shared" output-dir="../src/main/java"/>
</db2spring>
```