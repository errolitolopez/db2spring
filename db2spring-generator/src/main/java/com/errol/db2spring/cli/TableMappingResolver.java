package com.errol.db2spring.cli;

import com.errol.db2spring.model.Db2springXml;
import com.errol.db2spring.model.TableMapping;
import com.errol.db2spring.model.table.Table;
import com.errol.db2spring.model.SmartString;
import com.errol.db2spring.utils.codegen.GeneratorPropertyUtil;
import com.errol.db2spring.utils.collection.CollectionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Resolves and enriches table mappings by combining configured mappings
 * with inferred mappings from loaded tables.
 */
public class TableMappingResolver {

    public void resolve(List<Table> tables, Db2springXml xml) {
        List<String> generators = GeneratorPropertyUtil.GENERATORS;

        List<TableMapping> fromConfig = xml.getTableMappings();
        List<TableMapping> finalMappings = new ArrayList<>();

        // 1. Mappings from config, with generators added
        for (TableMapping tableMapping : fromConfig) {
            tableMapping.setGenerators(generators);
            finalMappings.add(tableMapping);
        }

        Set<String> existing = CollectionUtil.findValuesAsSet(fromConfig, TableMapping::getTableName);

        // 2. Mappings inferred from loaded tables that weren't in config
        for (Table table : tables) {
            String tableName = table.getTableName();
            if (!existing.contains(tableName)) {
                // Default class name generation
                String className = SmartString.of(tableName).toPascalCase().toSingular().get();
                TableMapping newMapping = new TableMapping(tableName, className, generators);
                finalMappings.add(newMapping);
            }
        }

        // Set the final, combined, and enriched list back to the XML object
        xml.setTableMappings(finalMappings);
    }
}