package com.errol.db2spring;

import com.errol.db2spring.contants.Db2springConstants;
import com.errol.db2spring.context.NamingDataContext;
import com.errol.db2spring.context.SubPackageContext;
import com.errol.db2spring.context.TableDataContext;
import com.errol.db2spring.exception.Db2springException;
import com.errol.db2spring.model.FileModel;
import com.errol.db2spring.model.GeneratorProperty;
import com.errol.db2spring.model.ProjectInfo;
import com.errol.db2spring.model.SmartString;
import com.errol.db2spring.model.TableMapping;
import com.errol.db2spring.model.plugin.Plugin;
import com.errol.db2spring.model.table.Column;
import com.errol.db2spring.model.table.Table;
import com.errol.db2spring.sql.SqlParser;
import com.errol.db2spring.utils.SmartStringUtil;
import com.errol.db2spring.utils.StringUtil;
import com.errol.db2spring.utils.codegen.ColumnUtil;
import com.errol.db2spring.utils.codegen.ImportUtil;
import com.errol.db2spring.utils.codegen.PackageUtil;
import com.errol.db2spring.utils.codegen.ProjectInfoUtil;
import com.errol.db2spring.utils.codegen.SuffixUtil;
import com.errol.db2spring.utils.collection.CollectionUtil;
import com.errol.db2spring.utils.collection.MapUtil;
import com.errol.db2spring.writer.FreeMarkerWriter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class Db2springGenerator {

    private static final FreeMarkerWriter writer = new FreeMarkerWriter();

    public List<FileModel> generate(String sql, Db2springProperty property) {
        if (StringUtil.isBlank(sql)) {
            log.warn("Empty SQL string provided; skipping code generation.");
            return List.of();
        }

        final List<Table> tables = SqlParser.getTables(sql);

        List<FileModel> configurationFiles = generateConfigurationFiles(property);

        return generateJavaFiles(tables, property);
    }

    public List<FileModel> generateJavaFiles(String sql, Db2springProperty property) {
        return generate(sql, property);
    }

    public List<FileModel> generate(List<Table> tables, Db2springProperty property) {
        return generateJavaFiles(tables, property);
    }

    public List<FileModel> generateJavaFiles(List<Table> tables, Db2springProperty property) {
        if (CollectionUtil.isBlank(tables)) {
            log.warn("No tables parsed from SQL; nothing to generate.");
            return List.of();
        }

        final List<GeneratorProperty> generatorProperties = property.getGeneratorProperties();
        if (CollectionUtil.isBlank(generatorProperties)) {
            log.warn("No generator properties found; skipping generation.");
            return List.of();
        }

        ProjectInfo projectInfo = property.getProjectInfo();
        List<FileModel> fileModels = new ArrayList<>();

        boolean isSpecBuilderGenerated = false;

        Map<String, TableMapping> tableMappingMap = MapUtil
                .toMap(property.getTableMappings(), TableMapping::getTableName);


        for (Table table : tables) {
            String tableName = table.getTableName();
            if (!tableMappingMap.containsKey(tableName)) {
                log.warn("Skipping table '{}': no mapping found", tableName);
                continue;
            }

            TableMapping tableMapping = tableMappingMap.get(tableName);

            List<String> generators = tableMapping.getGenerators();

            if (CollectionUtil.isBlank(generators)) {
                log.debug("No generators defined for table mapping: {}", tableName);
                continue;
            }

            Map<String, Object> data = new HashMap<>();
            data.put("projectInfo", projectInfo);

            String className = tableMapping.getClassName();
            String rootPackage = ProjectInfoUtil.getRootPackage(projectInfo);

            data.put("tableName", new SmartString(tableName));
            data.put("className", new SmartString(className));

            applyTableData(data, new TableDataContext(tableName, table, property.getTypeOverrides()));
            applyPluginData(property.getPlugins(), data);

            NamingDataContext namingContext = new NamingDataContext()
                    .setFileStructure(projectInfo.getFileStructure())
                    .setClassName(className)
                    .setRootPackage(rootPackage)
                    .setGenerators(generators)
                    .setGeneratorProperties(generatorProperties);
            applyNamingData(data, namingContext);

            for (String type : generators) {
                applyClassImportData(type, data);

                if (type.equals("spec-builder") && isSpecBuilderGenerated) {
                    continue;
                }

                if (type.equals("spec-builder")) isSpecBuilderGenerated = true;

                String typePascal = SmartStringUtil.toPascalCase(type);
                String subPackage = data.get("subPackage" + typePascal).toString();
                String filename = data.get("className" + typePascal).toString();

                String outputDir = CollectionUtil.findFirstAndMap(
                        generatorProperties,
                        GeneratorProperty::getOutputDir,
                        g -> g.getType().equals(type)
                ).orElse("src/main/java");

                boolean b = Optional.ofNullable(property.getPlugins())
                        .orElseGet(List::of)
                        .stream()
                        .noneMatch(plugin -> "Mapstruct".equals(plugin.getPluginName()));

                boolean mapStructAbsent = CollectionUtil
                        .noneMatch(property.getPlugins(), p -> p.getPluginName().equals("Mapstruct"));

                if (type.equals("mapper") && mapStructAbsent) {
                    fileModels.add(new FileModel()
                            .setType(type)
                            .setGroup(className)
                            .setOutputDir(outputDir)
                            .setRootPackage(rootPackage)
                            .setSubPackage(subPackage)
                            .setFilename(filename)
                            .setFileExtension("java")
                            .setContent(writer.writeContent(data, "custom-mapper")));

                    fileModels.add(new FileModel()
                            .setType(type)
                            .setGroup(className)
                            .setOutputDir(outputDir)
                            .setRootPackage(rootPackage)
                            .setSubPackage(subPackage)
                            .setFilename(filename + "Impl")
                            .setFileExtension("java")
                            .setContent(writer.writeContent(data, "custom-mapper-impl")));

                    continue;
                }

                fileModels.add(new FileModel()
                        .setType(type)
                        .setGroup(type.equals("spec-builder") ? null : className)
                        .setOutputDir(outputDir)
                        .setRootPackage(rootPackage)
                        .setSubPackage(subPackage)
                        .setFilename(filename)
                        .setFileExtension("java")
                        .setContent(writer.writeContent(data, type)));
            }
        }
        return fileModels;
    }

    private static String resolveSubPackage(SubPackageContext context) {
        String fileStructure = context.getFileStructure();
        String type = context.getType();
        String className = context.getClassName();

        String subPackage = context.getSubPackage();

        if ("layeredDto".equalsIgnoreCase(fileStructure) && Db2springConstants.ALL_DTO.contains(type)) {
            return subPackage + "." + className.toLowerCase();
        }

        if ("selfContained".equalsIgnoreCase(fileStructure)) {
            return className.toLowerCase() + "." + subPackage;
        }

        if ("featuredGroup".equalsIgnoreCase(fileStructure)) {
            if ("service-impl".equals(type)) {
                String[] parts = subPackage.split("\\.");
                if (parts.length >= 2) {
                    return parts[0] + "." + className.toLowerCase() + "." + parts[1];
                }
            }
            return subPackage + "." + className.toLowerCase();
        }
        return subPackage;
    }

    public List<FileModel> generateConfigurationFiles(Db2springProperty property) {
        List<FileModel> fileModels = new ArrayList<>();
        final ProjectInfo projectInfo = property.getProjectInfo();

        String projectName = SmartStringUtil.toKebabCase(ProjectInfoUtil.getProjectName(projectInfo));
        Map<String, Object> appPropsData = Map.of("projectName", projectName);

        Map<String, Object> pomData = new HashMap<>(Map.of(
                "dependencies", property.getDependencies(),
                "projectInfo", projectInfo
        ));
        applyPluginData(property.getPlugins(), pomData);

        return List.of(
                new FileModel()
                        .setOutputDir("src/main/resources")
                        .setFilename("application")
                        .setFileExtension("properties")
                        .setContent(writer.writeContent(appPropsData, "application-properties")),

                new FileModel()
                        .setFileExtension("gitignore")
                        .setContent(writer.writeContent(Map.of(), "git-ignore")),

                new FileModel()
                        .setFilename("pom")
                        .setFileExtension("xml")
                        .setContent(writer.writeContent(pomData, "pom"))
        );
    }

    public FileModel generateMainApplicationFile(ProjectInfo projectInfo) {
        String rootPackage = ProjectInfoUtil.getRootPackage(projectInfo);
        String className = SmartStringUtil.toPascalCase(ProjectInfoUtil.getProjectName(projectInfo) + "Application");
        Map<String, Object> data = Map.of("rootPackage", rootPackage, "className", className);

        return new FileModel()
                .setOutputDir("src/main/java")
                .setRootPackage(rootPackage)
                .setFilename(className)
                .setFileExtension("java")
                .setContent(writer.writeContent(data, "main"));
    }

    private static void applyClassImportData(String type, Map<String, Object> data) {
        data.put("classImports", ImportUtil.getClassImports(data, type));
    }

    private static void applyPluginData(List<Plugin> plugins, Map<String, Object> data) {
        if (CollectionUtil.isBlank(plugins)) return;
        plugins.forEach(plugin -> data.put("plugin" + plugin.getPluginName(), plugin));
    }

    private static void applyTableData(Map<String, Object> data, TableDataContext context) {
        Table table = context.getTable();
        if (Objects.isNull(table)) {
            log.warn("Table {} not found in table map.", context.getTableName());
            return;
        }

        data.put("tableName", new SmartString(context.getTableName()));

        List<Column> columns = ColumnUtil.resolvedColumns(table.getColumns(), context.getTypeOverrides());
        data.put("columns", columns);

        Column idColumn = CollectionUtil.findFirst(columns, Column::isPrimaryKey)
                .orElseThrow(() -> new Db2springException("Primary key is missing for table: " + table.getTableName()));

        data.put("idColumn", idColumn);

        data.put("dateColumns", CollectionUtil.findAll(columns, Column::isDate));

        applyFieldImportData(data, columns);
    }

    private static void applyFieldImportData(Map<String, Object> data, List<Column> columns) {
        data.put("fieldImports", ImportUtil.getFieldImports(
                columns.stream()
                        .map(column -> ColumnUtil.getFullyQualifiedName(column.getJavaType()))
                        .collect(Collectors.toSet())
        ));
    }

    private static void applyNamingData(Map<String, Object> data, NamingDataContext context) {
        context.getGenerators().forEach(type -> {
            //noinspection OptionalGetWithoutIsPresent
            GeneratorProperty generatorProperty = CollectionUtil
                    .findFirst(context.getGeneratorProperties(), g -> g.getType().equals(type))
                    .get();

            final String typePascal = SmartStringUtil.toPascalCase(type);

            String subPackage =  PackageUtil.resolveDefaultIfEmpty(generatorProperty.getSubPackage(), type);

            if (!type.equals("spec-builder")) {
                SubPackageContext subPackageContext = new SubPackageContext()
                        .setFileStructure(context.getFileStructure())
                        .setType(type)
                        .setClassName(context.getClassName()) // Unmodified class name
                        .setSubPackage(subPackage);

                subPackage = resolveSubPackage(subPackageContext);
            }

            // package e.g. (key=packageDto, value=com.example.demo.dto)
            data.putIfAbsent("subPackage" + typePascal, subPackage);

            String suffix = SuffixUtil.resolveDefaultIfEmpty(generatorProperty.getSuffix(), type);
            String className = context.getClassName() + suffix;

            if (type.equals("spec-builder")) {
                // Exclude class name
                className = suffix;
            }


            // package e.g. (key=packageDto, value=com.example.demo.dto)
            data.putIfAbsent("package" + typePascal, context.getRootPackage() + "." + subPackage);

            // package e.g. (key=classNameDto, value=UserDto)
            data.putIfAbsent("className" + typePascal, new SmartString(className));

            // package e.g. [(key=suffixDto, value=UserDto)]
            data.putIfAbsent("suffix" + typePascal, new SmartString(SuffixUtil.resolveFinalSuffix(type, suffix)));
        });
    }
}
