package com.errol.db2spring.model;

import com.errol.db2spring.model.plugin.Plugin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Db2springXml {
    private DatabaseConnection databaseConnection;
    private SqlOption sqlOption;
    private ProjectInfo projectInfo;
    private List<TableMapping> tableMappings;
    private List<TypeOverride> typeOverrides;
    private List<GeneratorProperty> generatorProperties;
    private List<Plugin> plugins;
}
