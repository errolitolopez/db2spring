package com.errol.db2spring.cli;

import com.errol.db2spring.logger.ColoredLogger;
import com.errol.db2spring.model.Db2springXml;
import com.errol.db2spring.model.GeneratorProperty;
import com.errol.db2spring.utils.codegen.GeneratorPropertyUtil;
import com.errol.db2spring.utils.collection.CollectionUtil;

import java.util.List;
import java.util.Set;

public class GeneratorPropertyResolver {

    /**
     * Resolves the final list of GeneratorProperties by combining configured
     * properties with default properties for any missing generators.
     * Also logs warnings for missing configurations.
     * @param xml The Db2springXml configuration object.
     * @return A list of all resolved GeneratorProperty objects.
     */
    public List<GeneratorProperty> resolve(Db2springXml xml) {
        List<GeneratorProperty> configured = xml.getGeneratorProperties();

        Set<String> configuredTypes = CollectionUtil
                .findValuesAsSet(configured, GeneratorProperty::getType);

        List<GeneratorProperty> missing = GeneratorPropertyUtil.getGenerators().stream()
                .filter(g -> !configuredTypes.contains(g.getType()))
                .toList();

        if (!missing.isEmpty()) {
            logMissingGeneratorWarning(missing);
        }

        return GeneratorPropertyUtil.resolveGenerators(configured);
    }

    private void logMissingGeneratorWarning(List<GeneratorProperty> missing) {
        List<String> missingTypes = CollectionUtil.findValuesAsList(missing, GeneratorProperty::getType);

        ColoredLogger.warning("Some generators are missing: " + missingTypes)
                .color(ColoredLogger.YELLOW)
                .withoutDashes()
                .log();

        ColoredLogger.warning("Default properties will be applied for missing generators:")
                .color(ColoredLogger.YELLOW)
                .withoutDashes()
                .log();

        for (int i = 0; i < missing.size(); i++) {
            GeneratorProperty property = missing.get(i);

            ColoredLogger.info("Generator: " + property.getType()).withoutDashes().log();
            ColoredLogger.info("- Output directory (root): " + property.getOutputDir()).withoutDashes().log();
            ColoredLogger.info("- Sub-package: " + property.getSubPackage()).withoutDashes().log();
            ColoredLogger.info("- Suffix: " + property.getSuffix()).withoutDashes().log();

            if (i < missing.size() - 1) {
                ColoredLogger.info("").withoutDashes().log();
            }
        }

        ColoredLogger.info("To skip generating them next time, set `generate=false`.")
                .withoutDashes()
                .log();
    }
}