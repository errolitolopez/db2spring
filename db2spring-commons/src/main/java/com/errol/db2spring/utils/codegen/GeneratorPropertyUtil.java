package com.errol.db2spring.utils.codegen;

import com.errol.db2spring.model.GeneratorProperty;
import com.errol.db2spring.utils.collection.MapUtil;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@UtilityClass
public class GeneratorPropertyUtil {

    public static final List<String> GENERATORS = List.of(
            "controller",
            "dto",
            "dto-create",
            "dto-request",
            "dto-response",
            "dto-update",
            "entity",
            "mapper",
            "repository",
            "service",
            "service-impl",
            "spec-builder"
    );

    public static <T> Optional<GeneratorProperty> findBy(
            List<GeneratorProperty> properties,
            Function<GeneratorProperty, T> keyExtractor,
            T value
    ) {
        return Optional.ofNullable(properties)
                .orElseGet(List::of)
                .stream()
                .filter(p -> Objects.equals(keyExtractor.apply(p), value))
                .findFirst();
    }

    public static List<GeneratorProperty> getGenerators() {
        return GENERATORS.stream()
                .map(generator -> new GeneratorProperty(
                        generator,
                        true,
                        "src/main/java",
                        PackageUtil.resolveDefault(generator),
                        SuffixUtil.resolveDefault(generator)
                ))
                .toList();
    }

    public static List<GeneratorProperty> resolveGenerators(List<GeneratorProperty> generatorProperties) {
        Map<String, GeneratorProperty> generatorPropertyMap = MapUtil
                .toMap(generatorProperties, GeneratorProperty::getType);

        return GENERATORS.stream()
                .map(type -> {
                    GeneratorProperty gp = generatorPropertyMap.get(type);

                    boolean enable = gp != null && gp.isGenerate();
                    String outputDir = gp != null
                            ? Objects.requireNonNullElse(gp.getOutputDir(), "src/main/java") : "src/main/java";
                    String subPackage = gp != null
                            ? PackageUtil.resolveDefaultIfEmpty(gp.getSubPackage(), type)
                            : PackageUtil.resolveDefault(type);
                    String suffix = gp != null
                            ? SuffixUtil.resolveDefaultIfEmpty(gp.getSuffix(), type)
                            : SuffixUtil.resolveDefault(type);

                    return new GeneratorProperty(type, enable, outputDir, subPackage, suffix);
                }).toList();
    }
}
