//package db2spring;
//
//import com.errol.db2spring.Db2springGenerator;
//import com.errol.db2spring.Db2springProperty;
//import com.errol.db2spring.model.Code;
//import com.errol.db2spring.model.Dependency;
//import com.errol.db2spring.model.Generator;
//import com.errol.db2spring.model.GeneratorType;
//import com.errol.db2spring.model.ProjectInfo;
//import com.errol.db2spring.model.TableMapping;
//import com.errol.db2spring.model.TypeOverride;
//import com.errol.db2spring.model.plugin.GenericPlugin;
//import com.errol.db2spring.model.plugin.Plugin;
//import com.errol.db2spring.model.plugin.SpecBuilderPlugin;
//import com.errol.db2spring.model.table.Column;
//import com.errol.db2spring.model.table.Table;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class Db2springGeneratorTest {
//
//    @Test
//    void testDb2springGenerator() {
//        ProjectInfo projectInfo = new ProjectInfo("com.errol", "db2spring", "17", "3.5.3");
//
//        List<Plugin> plugins = getPlugins();
//
//        List<Dependency> dependencies = getDependencies();
//        List<Generator> generators = getGenerators(projectInfo);
//        List<TypeOverride> typeOverrides = getTypeOverrides();
//
//        List<Table> tables = new ArrayList<>();
//        Table userTable = new Table("users", getColumns(), typeOverrides);
//        tables.add(userTable);
//
//        List<TableMapping> tableMappings = getTableMappings();
//        Db2springProperty db2springProperty = new Db2springProperty(projectInfo, tables, tableMappings, generators, dependencies, plugins);
//        List<Code> codes = Db2springGenerator.generateCodes(true, db2springProperty);
//    }
//
//    private static List<TableMapping> getTableMappings() {
//        List<TableMapping> tableMappings = new ArrayList<>();
//
//        TableMapping userTableMapping = new TableMapping("users", "Employee");
//
//        tableMappings.add(userTableMapping);
//        return tableMappings;
//    }
//
//    private static List<TypeOverride> getTypeOverrides() {
//        List<TypeOverride> typeOverrides = new ArrayList<>();
//
//        TypeOverride dateTimeToInstant = new TypeOverride("DATETIME", "Instant");
//        TypeOverride birthdateDateTimeToLocalDate = new TypeOverride("birth_date", "DATETIME", "LocalDate");
//
//        typeOverrides.add(dateTimeToInstant);
//        typeOverrides.add(birthdateDateTimeToLocalDate);
//        return typeOverrides;
//    }
//
//    private static List<Column> getColumns() {
//        List<Column> columns = new ArrayList<>();
//        Column idColumn = new Column("id", "BIGINT", 19, true, true);
//        Column nameColumn = new Column("name", "VARCHAR", 64, true, false);
//        Column birthDateColumn = new Column("birth_date", "DATETIME", null, false, false);
//
//        Column createdAtColumn = new Column("created_at", "DATETIME", null, true, false);
//        Column modifiedAtColumn = new Column("modified_at", "DATETIME", null, true, false);
//
//        columns.add(nameColumn);
//        columns.add(idColumn);
//        columns.add(birthDateColumn);
//        columns.add(createdAtColumn);
//        columns.add(modifiedAtColumn);
//        return columns;
//    }
//
//    private static List<Generator> getGenerators(ProjectInfo projectInfo) {
//        List<Generator> generators = new ArrayList<>();
//        generators.add(new Generator(GeneratorType.ENTITY, true, projectInfo.getRootPackage(), "entity", "src/main/java", null));
//        return generators;
//    }
//
//    private static List<Dependency> getDependencies() {
//        List<Dependency> dependencies = new ArrayList<>();
//
//        Dependency springValidationDependency = new Dependency("org.springframework.boot", "spring-boot-starter-validation", "3.3.5");
//        Dependency lombokDependency = new Dependency("org.projectlombok", "lombok", "1.18.34");
//        Dependency mapstructDependency = new Dependency("org.mapstruct", "mapstruct", "1.6.3");
//
//        dependencies.add(springValidationDependency);
//        dependencies.add(lombokDependency);
//        dependencies.add(mapstructDependency);
//        return dependencies;
//    }
//
//    private static List<Plugin> getPlugins() {
//        Plugin lombok = new GenericPlugin("Lombok", "1.18.34");
//        Plugin mapStruct = new GenericPlugin("Mapstruct", "1.5.5.Final");
//        Plugin lombokMapstructBinding = new GenericPlugin("LombokMapstructBinding", "0.2.0");
//        Plugin customMapper = new GenericPlugin("CustomMapper");
//        Plugin springValidation = new GenericPlugin("SpringValidation");
//        Plugin specBuilder = new SpecBuilderPlugin("utils", "SpecBuilder", "src/main/java");
//
//        List<Plugin> plugins = new ArrayList<>();
//        plugins.add(lombok);
//        plugins.add(mapStruct);
//        plugins.add(specBuilder);
//        plugins.add(springValidation);
//        plugins.add(customMapper);
//        plugins.add(lombokMapstructBinding);
//        return plugins;
//    }
//}
