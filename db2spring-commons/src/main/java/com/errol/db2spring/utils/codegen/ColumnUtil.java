package com.errol.db2spring.utils.codegen;

import com.errol.db2spring.mapper.DataTypeMapper;
import com.errol.db2spring.mapper.ImportMapper;
import com.errol.db2spring.model.TypeOverride;
import com.errol.db2spring.model.table.Column;
import com.errol.db2spring.utils.collection.CollectionUtil;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class ColumnUtil {

    public static Integer resolveDefaultSize(Integer size, String sqlType) {
        if (size != null) {
            return size;
        }
        return resolveDefaultSizeIfEmpty(sqlType);
    }

    public static Integer resolveDefaultSizeIfEmpty(String sqlType) {
        return switch (sqlType) {
            case "TINYINT" -> 3;
            case "SMALLINT" -> 5;
            case "MEDIUMINT" -> 8;
            case "INT", "INTEGER", "DECIMAL", "NUMERIC" -> 10;
            case "BIGINT" -> 19;
            default -> null;
        };
    }

    public static String getFullyQualifiedName(String javaType) {
        return ImportMapper.getMappings().get(javaType);
    }

    public static List<Column> resolvedColumns(List<Column> columns, List<TypeOverride> typeOverrides, List<String> excludedColumns) {
        if (CollectionUtil.isBlank(columns)) {
            return List.of();
        }
        return columns.stream()
                .filter(column -> !CollectionUtil.findAll(excludedColumns).contains(column.getColumnName()))
                .map(column -> resolveColumn(column, typeOverrides))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static Column resolveColumn(Column column, List<TypeOverride> typeOverrides) {
        String sqlType = column.getSqlType();
        String javaType = DataTypeMapper.getJavaType(sqlType);

        column.setJavaType(resolveJavaType(typeOverrides, javaType, sqlType));
        column.setDate(isDate(javaType));
        column.setSize(resolveDefaultSize(column.getSize(), sqlType));
        return column;
    }

    private static String resolveJavaType(List<TypeOverride> typeOverrides, String javaType, String sqlType) {
        if (CollectionUtil.isBlank(typeOverrides)) {
            return javaType;
        }

        return CollectionUtil.findFirstAndMap(
                typeOverrides,
                TypeOverride::getJavaType,
                t -> t.getSqlType().equalsIgnoreCase(sqlType)
        ).orElse(javaType);
    }

    private static boolean isDate(String javaType) {
        return ImportMapper.getMappings().entrySet().stream()
                .filter(e -> e.getValue().startsWith("java.time"))
                .map(Map.Entry::getKey)
                .anyMatch(k -> k.equalsIgnoreCase(javaType));
    }
}
