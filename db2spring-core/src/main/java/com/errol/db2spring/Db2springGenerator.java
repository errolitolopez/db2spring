package com.errol.db2spring;

import com.errol.db2spring.context.NamingDataContext;
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
import com.errol.db2spring.utils.codegen.GeneratorPropertyUtil;
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

            applyTableData(data, new TableDataContext(tableName, List.of(table), property.getTypeOverrides()));
            applyNamingData(data, new NamingDataContext(className, rootPackage, generators, generatorProperties));
            applyPluginData(property.getPlugins(), data);

            for (String type : generators) {
                applyClassImportData(type, data);

                if (type.equals("spec-builder") && isSpecBuilderGenerated) {
                    continue;
                }

                if (type.equals("spec-builder")) isSpecBuilderGenerated = true;

                String typePascal = SmartStringUtil.toPascalCase(type);
                String packageName = data.get("package" + typePascal).toString();
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
                        .noneMatch(property.getPlugins(), plugin -> plugin.getPluginName().equals("Mapstruct"));

                if (type.equals("mapper") && mapStructAbsent) {
                    fileModels.add(new FileModel(
                            type,
                            className,
                            outputDir,
                            packageName,
                            filename,
                            "java",
                            writer.writeContent(data, "custom-mapper")
                    ));

                    fileModels.add(new FileModel(
                            type,
                            className,
                            outputDir,
                            packageName,
                            filename + "Impl",
                            "java",
                            writer.writeContent(data, "custom-mapper-impl")
                    ));

                    continue;
                }

                fileModels.add(new FileModel(
                        type,
                        type.equals("spec-builder") ? null : className,
                        outputDir,
                        packageName,
                        filename,
                        "java",
                        writer.writeContent(data, type)
                ));
            }
        }
        return fileModels;
    }

    public List<FileModel> generateConfigurationFiles(Db2springProperty property) {
        List<FileModel> fileModels = new ArrayList<>();
        final ProjectInfo projectInfo = property.getProjectInfo();

        String propsContent = writer.writeContent(
                Map.of("projectName", SmartStringUtil.toKebabCase(ProjectInfoUtil.getProjectName(projectInfo))),
                "application-properties"
        );
        FileModel appPropsFile = new FileModel(null, null, "src/main/resources", null, "application", "properties", propsContent);

        String gitIgnoreContent = writer.writeContent(Map.of(), "git-ignore");
        FileModel gitIgnoreFile = new FileModel(null, null, null, null, null, "git-ignore", gitIgnoreContent);

        Map<String, Object> data = new HashMap<>(Map.of(
                "dependencies", property.getDependencies(),
                "projectInfo", projectInfo
        ));
        applyPluginData(property.getPlugins(), data);

        FileModel pomFile = new FileModel(null, null, null, null, "pom", "xml", writer.writeContent(data, "pom"));

        return List.of(gitIgnoreFile, appPropsFile, pomFile);
    }

    public FileModel generateMainApplicationFile(ProjectInfo projectInfo) {
        String rootPackage = ProjectInfoUtil.getRootPackage(projectInfo);
        String className = SmartStringUtil.toPascalCase(ProjectInfoUtil.getProjectName(projectInfo) + "Application");
        return new FileModel(
                null,
                null,
                "src/main/java",
                rootPackage,
                className,
                "java",
                writer.writeContent(
                        Map.of(
                                "rootPackage", rootPackage,
                                "className", className
                        ),
                        "main"
                ));
    }

    private static void applyClassImportData(String type, Map<String, Object> data) {
        data.put("classImports", ImportUtil.getClassImports(data, type));
    }

    private static void applyPluginData(List<Plugin> plugins, Map<String, Object> data) {
        if (CollectionUtil.isBlank(plugins)) return;
        plugins.forEach(plugin -> data.put("plugin" + plugin.getPluginName(), plugin));
    }

    private static void applyTableData(Map<String, Object> data, TableDataContext context) {
        Map<String, Table> tableMap = MapUtil.toMap(context.tables(), Table::getTableName);

        if (CollectionUtil.isEmpty(tableMap)) {
            log.warn("No tables found in context for table data application.");
            return;
        }

        Table table = tableMap.get(context.tableName());
        if (Objects.isNull(table)) {
            log.warn("Table {} not found in table map.", context.tableName());
            return;
        }

        data.put("tableName", new SmartString(context.tableName()));

        List<Column> columns = ColumnUtil.resolvedColumns(table.getColumns(), context.typeOverrides());
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
        context.generators().forEach(type -> {
            //noinspection OptionalGetWithoutIsPresent
            GeneratorProperty generatorProperty = GeneratorPropertyUtil
                    .findBy(context.generatorPropertyProperties(), GeneratorProperty::getType, type)
                    .get();

            final String typPascal = SmartStringUtil.toPascalCase(type);

            // package e.g. (key=packageDto, value=com.example.demo.dto)
            String rootPackage = context.rootPackage();
            String subPackage = PackageUtil.resolveDefaultIfEmpty(generatorProperty.getSubPackage(), type);

            data.put("package" + typPascal, rootPackage + "." + subPackage);

            String suffix = SuffixUtil.resolveDefaultIfEmpty(generatorProperty.getSuffix(), type);

            String className = context.className() + suffix;

            if (type.equals("spec-builder")) {
                // Exclude class name
                className = "SpecBuilder" + suffix;
            }

            // package e.g. (key=classNameDto, value=UserDto)
            data.put("className" + typPascal, new SmartString(className));

            // package e.g. [(key=suffixDto, value=UserDto)]
            data.put("suffix" + typPascal, new SmartString(SuffixUtil.resolveFinalSuffix(type, suffix)));
        });
    }
}
