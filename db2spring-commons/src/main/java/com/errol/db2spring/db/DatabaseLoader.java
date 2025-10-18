package com.errol.db2spring.db;

import com.errol.db2spring.exception.Db2springException;
import com.errol.db2spring.logger.ColoredLogger;
import com.errol.db2spring.model.DatabaseConnection;
import com.errol.db2spring.model.TableMapping;
import com.errol.db2spring.model.table.Column;
import com.errol.db2spring.model.table.Table;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class DatabaseLoader {
    private final DatabaseConnection dbConnection;
    private final List<TableMapping> tableMappings;

    public List<Table> getTables() {
        try {
            Connection connection = DriverManager.getConnection(
                    dbConnection.getUrl(),
                    dbConnection.getUsername(),
                    dbConnection.getPassword()
            );

            DatabaseMetaData meta = connection.getMetaData();
            List<Table> tables = new ArrayList<>();


            for (TableMapping tableMapping : tableMappings) {
                String tableName = tableMapping.getTableName();
                try (ResultSet tablesRs = meta.getTables(null, null, tableName, new String[]{"TABLE"})) {
                    if (!tablesRs.next()) {
                        ColoredLogger.log(new ColoredLogger.LogOptions("Table [" + tableName + "] not found in database, skipping.")
                                .messageColor(ColoredLogger.YELLOW)
                                .level(ColoredLogger.Level.WARNING)
                                .withDashes(false));
                        continue;
                    }
                }
                List<Column> columns = getColumns(meta, connection, tableName);
                tables.add(new Table(tableName, columns));
            }
            return tables;
        } catch (Exception e) {
            throw new Db2springException("Failed to inspect database", e);
        }
    }

    private List<Column> getColumns(DatabaseMetaData meta, Connection conn, String tableName) throws SQLException {
        List<Column> columns = new ArrayList<>();

        try (ResultSet colsRs = meta.getColumns(conn.getCatalog(), null, tableName, "%")) {
            while (colsRs.next()) {
                String columnName = colsRs.getString("COLUMN_NAME");
                String sqlType = colsRs.getString("TYPE_NAME");
                int size = colsRs.getInt("COLUMN_SIZE");
                boolean nullable = colsRs.getInt("NULLABLE") == DatabaseMetaData.columnNullable;

                // check PK
                boolean primary = isPrimaryKey(meta, tableName, columnName);

                Column column = new Column(columnName, sqlType, null, size, nullable, primary, false);
                columns.add(column);
            }
        }
        return columns;
    }

    private boolean isPrimaryKey(DatabaseMetaData meta, String tableName, String columnName) throws SQLException {
        try (ResultSet pkRs = meta.getPrimaryKeys(null, null, tableName)) {
            while (pkRs.next()) {
                if (columnName.equalsIgnoreCase(pkRs.getString("COLUMN_NAME"))) {
                    return true;
                }
            }
        }
        return false;
    }
}
