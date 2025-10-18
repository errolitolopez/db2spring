package com.errol.db2spring;

import com.errol.db2spring.model.Dependency;
import com.errol.db2spring.model.GeneratorProperty;
import com.errol.db2spring.model.ProjectInfo;
import com.errol.db2spring.model.table.Table;
import com.errol.db2spring.model.TableMapping;
import com.errol.db2spring.model.plugin.Plugin;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class Db2springProperty {
    private final ProjectInfo projectInfo;
    private final List<Table> tables;
    private final List<TableMapping> tableMappings;
    private final List<GeneratorProperty> generatorProperties;
    private final List<Dependency> dependencies;
    private final List<Plugin> plugins;

    public List<Table> getTables() {
        if (tableMappings == null || tables == null) {
            return tables;
        }
        return tables;
    }

    public List<GeneratorProperty> getGeneratorProperties() {
        if (generatorProperties == null || projectInfo == null) {
            return Collections.emptyList();
        }
//        return generators
//                .stream()
//                .filter(g -> !g.getGeneratorType().equals(GeneratorType.DEFAULT))
//                .filter(g -> !(g.getGeneratorType().getValue().startsWith("dto-") && !g.isEnable()))
//                .map(generator -> {
//                    generator.setRootPackage(projectInfo.getRootPackage());
//                    return generator;
//                })
//                .collect(Collectors.toList());
        return generatorProperties;
    }
}
