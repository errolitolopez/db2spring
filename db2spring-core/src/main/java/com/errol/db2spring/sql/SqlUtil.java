package com.errol.db2spring.sql;

import lombok.experimental.UtilityClass;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public final class SqlUtil {

    public static List<String> splitStatements(String sql) {
        List<String> statements = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        int parens = 0;

        for (String line : sql.split("\n")) {
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("--")) {
                continue;
            }

            sb.append(line).append("\n");

            for (char c : line.toCharArray()) {
                if (c == '(') parens++;
                else if (c == ')') parens--;
            }

            if (trimmed.endsWith(";") && parens == 0) {
                statements.add(sb.toString().trim());
                sb.setLength(0);
            }
        }

        if (!sb.isEmpty()) {
            statements.add(sb.toString().trim());
        }

        return statements;
    }

    public static boolean primaryKey(CreateTable createTable, ColumnDefinition column) {
        if (column == null) return false;

        List<String> specs = column.getColumnSpecs(); // for newer JSqlParser
        if (specs != null) {
            for (int i = 0; i < specs.size() - 1; i++) {
                String current = specs.get(i);
                String next = specs.get(i + 1);
                if ("PRIMARY".equalsIgnoreCase(current) && "KEY".equalsIgnoreCase(next)) {
                    return true;
                }
            }
        }

        if (createTable.getIndexes() != null) {
            return createTable.getIndexes()
                    .stream()
                    .anyMatch(idx -> "PRIMARY KEY".equalsIgnoreCase(idx.getType())
                            && idx.getColumnsNames().contains(column.getColumnName()));
        }

        return false;
    }

    public static boolean nullable(ColumnDefinition column) {
        if (column == null) return true;

        List<String> specs = column.getColumnSpecs();
        if (specs == null || specs.isEmpty()) return true; // default nullable

        // Look for NOT NULL anywhere in specs
        for (int i = 0; i < specs.size() - 1; i++) {
            String current = specs.get(i);
            String next = specs.get(i + 1);

            if ("NOT".equalsIgnoreCase(current) && "NULL".equalsIgnoreCase(next)) {
                return false; // explicitly NOT NULL
            }
        }

        // If only NULL exists without NOT, it's nullable
        for (String s : specs) {
            if ("NULL".equalsIgnoreCase(s)) {
                return true;
            }
        }

        return true;
    }

    public static Integer getSize(ColumnDefinition column) {
        if (column == null || column.getColDataType() == null) {
            return null;
        }

        String dataType = column.getColDataType().toString();
        if (dataType != null) {
            int start = dataType.indexOf('(');
            int end = dataType.indexOf(')');
            if (start > 0 && end > start) {
                String sizeStr = dataType.substring(start + 1, end).trim();
                try {
                    return Integer.parseInt(sizeStr);
                } catch (NumberFormatException ignored) {

                }
            }
        }
        return null;
    }

    public static String getSqlType(ColumnDefinition column) {
        if (column == null || column.getColDataType() == null) return null;

        String dataType = column.getColDataType().getDataType();
        if (dataType == null) return null;

        // Remove parentheses and arguments
        int parenIndex = dataType.indexOf('(');
        if (parenIndex > 0) {
            return dataType.substring(0, parenIndex).trim().toUpperCase();
        }

        return dataType.trim().toUpperCase();
    }
}
