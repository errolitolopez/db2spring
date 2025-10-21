package com.errol.db2spring;

import com.errol.db2spring.model.Dependency;
import com.errol.db2spring.model.GeneratorProperty;
import com.errol.db2spring.model.ProjectInfo;
import com.errol.db2spring.model.TableMapping;
import com.errol.db2spring.model.TypeOverride;
import com.errol.db2spring.model.plugin.Plugin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Db2springProperty {
    private ProjectInfo projectInfo;
    private List<Dependency> dependencies;
    private List<TableMapping> tableMappings;
    private List<TypeOverride> typeOverrides;
    private List<GeneratorProperty> generatorProperties;
    private List<Plugin> plugins;
    private List<String> excludedColumns;
}
