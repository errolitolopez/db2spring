package com.errol.db2spring.context;

import com.errol.db2spring.model.GeneratorProperty;

import java.util.List;

public record NamingDataContext(
        String className,
        String rootPackage,
        List<String> generators,
        List<GeneratorProperty> generatorPropertyProperties) {
}
