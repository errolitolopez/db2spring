package com.errol.db2spring.sql;

import com.errol.db2spring.exception.Db2springException;
import com.errol.db2spring.model.table.Column;
import com.errol.db2spring.model.table.Table;
import lombok.experimental.UtilityClass;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@UtilityClass
public final class SqlParser {

    public static List<Table> getTables(String sql) {
        List<Table> tables = new ArrayList<>();
        List<String> createStatements = SqlUtil.splitStatements(sql);

        List<CreateTable> createTables = getCreateTables(createStatements);

        for (CreateTable createTable : createTables) {
            String tableName = createTable.getTable().getName();

            List<ColumnDefinition> columnDefinitions = createTable.getColumnDefinitions();
            List<Column> columns = getColumns(createTable, columnDefinitions);

            boolean primaryKey = columns.stream().anyMatch(Column::isPrimaryKey);

            if (!primaryKey) {
                throw new Db2springException("Failed to parse sql file, no primary key for table: " + tableName);
            }

            Table table = new Table(tableName, columns);
            tables.add(table);
        }

        return Collections.unmodifiableList(tables);
    }

    private static List<Column> getColumns(CreateTable createTable, List<ColumnDefinition> columnDefinitions) {
        List<Column> columns = new ArrayList<>();

        for (ColumnDefinition columnDef : columnDefinitions) {
            boolean primaryKey = SqlUtil.primaryKey(createTable, columnDef);
            boolean nullable = !primaryKey && SqlUtil.nullable(columnDef);

            columns.add(new Column(
                    columnDef.getColumnName(),
                    SqlUtil.getSqlType(columnDef),
                    null,
                    SqlUtil.getSize(columnDef),
                    nullable,
                    primaryKey,
                    false
            ));
        }

        return Collections.unmodifiableList(columns);
    }

    private static List<CreateTable> getCreateTables(List<String> sqlStatements) {
        List<CreateTable> createTables = new ArrayList<>();

        for (String sql : sqlStatements) {
            try {
                Statement stmt = CCJSqlParserUtil.parse(sql);
                if (stmt instanceof CreateTable createTable) {
                    createTables.add(createTable);
                } else {
                    throw new Db2springException("Failed to parse sql file, not a CREATE TABLE statement");
                }
            } catch (JSQLParserException e) {
                throw new Db2springException("Failed to parse sql file", e.getMessage());
            }
        }
        return Collections.unmodifiableList(createTables);
    }
}

