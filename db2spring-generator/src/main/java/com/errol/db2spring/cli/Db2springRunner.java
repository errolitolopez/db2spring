package com.errol.db2spring.cli;

import com.errol.db2spring.Db2springGenerator;
import com.errol.db2spring.Db2springProperty;
import com.errol.db2spring.exception.Db2springException;
import com.errol.db2spring.logger.ColoredLogger;
import com.errol.db2spring.model.Db2springXml;
import com.errol.db2spring.model.FileModel;
import com.errol.db2spring.model.GeneratorProperty;
import com.errol.db2spring.model.TableMapping;
import com.errol.db2spring.model.table.Table;
import com.errol.db2spring.utils.collection.CollectionUtil;
import com.errol.db2spring.writer.Db2springFileWriter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class Db2springRunner {
    private final Db2springXml xml;
    private final List<Table> tables;

    private static final Db2springFileWriter writer = new Db2springFileWriter();
    private final GeneratorPropertyResolver propertyResolver = new GeneratorPropertyResolver();

    public void run() {
        try {
            // 1. Resolve Generator Properties (delegated)
            List<GeneratorProperty> resolvedGeneratorProperties = propertyResolver.resolve(xml);

            List<String> generates = CollectionUtil.findValuesAsList(
                    resolvedGeneratorProperties,
                    GeneratorProperty::getType,
                    GeneratorProperty::isGenerate
            );

            // Table mappings are already resolved by Db2springAppRunner
            List<TableMapping> resolvedTableMappings = xml.getTableMappings();

            Db2springProperty property = new Db2springProperty()
                    .setProjectInfo(xml.getProjectInfo())
                    .setTableMappings(resolvedTableMappings)
                    .setTypeOverrides(xml.getTypeOverrides())
                    .setGeneratorProperties(resolvedGeneratorProperties)
                    .setPlugins(xml.getPlugins())
                    .setExcludedColumns(xml.getExcludedColumns());

            // 2. Execute Generation
            Db2springGenerator db2SpringGenerator = new Db2springGenerator();
            List<FileModel> files = db2SpringGenerator.generateJavaFiles(tables, property);

            // 3. Write Files
            writeGeneratedFiles(files, generates);

        } catch (Exception e) {
            throw new Db2springException("Failed to run generator", e);
        }
    }

    private void writeGeneratedFiles(List<FileModel> files, List<String> generates) {
        for (FileModel file : files) {
            // Only write files that are marked for generation
            if (!generates.contains(file.getType())) continue;

            writer.writeFile(file.getFullPath(), file.getContent());

            ColoredLogger.info(String.format("Generated: %s", file.getFullPath()))
                    .color(ColoredLogger.BLUE)
                    .withoutDashes()
                    .log();
        }
    }
}