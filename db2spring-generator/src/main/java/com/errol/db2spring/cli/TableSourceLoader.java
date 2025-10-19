package com.errol.db2spring.cli;

import com.errol.db2spring.db.DatabaseLoader;
import com.errol.db2spring.jar.JarLoader;
import com.errol.db2spring.logger.ColoredLogger;
import com.errol.db2spring.model.DatabaseConnection;
import com.errol.db2spring.model.Db2springXml;
import com.errol.db2spring.model.SqlOption;
import com.errol.db2spring.model.table.Table;
import com.errol.db2spring.sql.SqlFileReader;
import com.errol.db2spring.sql.SqlParser;

import java.util.List;
import java.util.stream.Collectors;

public class TableSourceLoader {

    public List<Table> load(Db2springXml xml) throws Exception {
        SqlOption sqlOption = xml.getSqlOption();

        if (sqlOption == null) {
            return loadFromDatabase(xml);
        } else if (sqlOption.getSql() != null && !sqlOption.getSql().isBlank()) {
            return loadFromSqlStatement(xml);
        } else if (sqlOption.getSrc() != null && !sqlOption.getSrc().isBlank()) {
            return loadFromSqlFile(xml);
        }

        return List.of(); 
    }
    
    private List<Table> loadFromDatabase(Db2springXml db2SpringXml) throws Exception {
        DatabaseConnection dbConn = db2SpringXml.getDatabaseConnection();
        JarLoader.loadExternalDriver(dbConn.getDriverJar(), dbConn.getDriverClass());
        CliLogger.logInfo(String.format("Driver Jar: %s", dbConn.getDriverJar()));
        CliLogger.logInfo(String.format("JDBC Driver: %s", dbConn.getDriverClass()));

        ColoredLogger.info("Loading tables from database...")
                .color(ColoredLogger.BLUE)
                .withoutDashes()
                .log();

        DatabaseLoader databaseLoader = new DatabaseLoader(
                db2SpringXml.getDatabaseConnection(),
                db2SpringXml.getTableMappings()
        );

        return databaseLoader.getTables();
    }

    private List<Table> loadFromSqlFile(Db2springXml xml) throws Exception {
        String src = xml.getSqlOption().getSrc();
        CliLogger.logInfo(String.format("SQL File: %s", src));

        ColoredLogger.log(new ColoredLogger.LogOptions("Parsing tables from sql files...")
                .messageColor(ColoredLogger.BLUE).withDashes(false));

        String sql = SqlFileReader.readAllSql(src)
                .stream()
                .collect(Collectors.joining("\n"));

        return SqlParser.getTables(sql);
    }

    private List<Table> loadFromSqlStatement(Db2springXml xml) throws Exception {
        ColoredLogger.info("Parsing tables from sql statement...")
                .color(ColoredLogger.BLUE)
                .withoutDashes()
                .log();

        return SqlParser.getTables(xml.getSqlOption().getSql());
    }
}