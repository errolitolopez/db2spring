package com.errol.db2spring.cli;

import com.errol.db2spring.model.Db2springXml;
import com.errol.db2spring.model.table.Table;
import com.errol.db2spring.xml.XmlLoader;

import java.time.Instant;
import java.util.List;

public class Db2springAppRunner {

    private final TableSourceLoader tableSourceLoader = new TableSourceLoader();
    private final TableMappingResolver mappingResolver = new TableMappingResolver();

    public void run(String[] args) {
        if (args.length == 0) {
            CliLogger.logErrorAndExit("No config file provided. Usage: java -jar db2spring.jar <config.xml>");
        }

        String configFile = args[0];
        Instant start = Instant.now();

        CliLogger.logHeader();

        try {
            // 1. Load Configuration
            Db2springXml xml = XmlLoader.load(configFile);

            // 2. Load Tables from configured source
            List<Table> tables = tableSourceLoader.load(xml);

            if (tables.isEmpty()) {
                CliLogger.logFooter(start);
            }

            // 3. Resolve/Enrich Table Mappings
            mappingResolver.resolve(tables, xml);

            // 4. Run Generator
            runGenerator(xml, tables);

            CliLogger.logFooter(start);

        } catch (Exception e) {
//            e.printStackTrace();
            CliLogger.logErrorAndExit("Failed to run generator: " + e.getMessage());
        }
    }

    private void runGenerator(Db2springXml db2SpringXml, List<Table> tables) {
        Db2springRunner runner = new Db2springRunner(db2SpringXml, tables);
        runner.run();
    }
}