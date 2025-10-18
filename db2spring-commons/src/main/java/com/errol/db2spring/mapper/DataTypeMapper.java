package com.errol.db2spring.mapper;

import lombok.experimental.UtilityClass;

import java.util.Map;

import static java.util.Map.entry;

@UtilityClass
public final class DataTypeMapper {

    private static final Map<String, String> TYPE_MAP = Map.<String, String>ofEntries(
            // Numeric types
            entry("TINYINT", "Byte"),
            entry("SMALLINT", "Short"),
            entry("MEDIUMINT", "Integer"),
            entry("INT", "Integer"),
            entry("INTEGER", "Integer"),
            entry("YEAR", "Integer"),
            entry("BIGINT", "Long"),
            entry("SERIAL", "Long"),
            entry("BIGSERIAL", "Long"),
            entry("BIGINT UNSIGNED", "BigInteger"),
            entry("DECIMAL", "BigDecimal"),
            entry("NUMERIC", "BigDecimal"),
            entry("DEC", "BigDecimal"),
            entry("NUMBER", "BigDecimal"),
            entry("MONEY", "BigDecimal"),
            entry("SMALLMONEY", "BigDecimal"),
            entry("FLOAT", "Float"),
            entry("DOUBLE", "Double"),
            entry("REAL", "Double"),
            entry("BIT", "Boolean"),
            entry("BOOLEAN", "Boolean"),
            entry("BOOL", "Boolean"),

            // Binary / blob
            entry("BINARY", "byte[]"),
            entry("VARBINARY", "byte[]"),
            entry("TINYBLOB", "byte[]"),
            entry("BLOB", "byte[]"),
            entry("MEDIUMBLOB", "byte[]"),
            entry("LONGBLOB", "byte[]"),

            // String types
            entry("CHAR", "String"),
            entry("VARCHAR", "String"),
            entry("TEXT", "String"),
            entry("TINYTEXT", "String"),
            entry("MEDIUMTEXT", "String"),
            entry("LONGTEXT", "String"),
            entry("ENUM", "String"),
            entry("SET", "String"),
            entry("NVARCHAR", "String"),
            entry("NCHAR", "String"),
            entry("NTEXT", "String"),
            entry("UUID", "String"),
            entry("UNIQUEIDENTIFIER", "String"),
            entry("JSON", "String"),
            entry("JSONB", "String"),
            entry("XML", "String"),

            // Date / Time types
            entry("DATE", "LocalDate"),
            entry("DATETIME", "LocalDateTime"),
            entry("TIMESTAMP", "LocalDateTime"),
            entry("TIMESTAMP WITHOUT TIME ZONE", "LocalDateTime"),
            entry("SMALLDATETIME", "LocalDateTime"),
            entry("TIMESTAMP WITH TIME ZONE", "OffsetDateTime"),
            entry("TIMESTAMPTZ", "OffsetDateTime"),
            entry("TIME", "LocalTime"),
            entry("TIME WITH TIME ZONE", "OffsetTime")
    );

    private static final String JAVA_OBJECT = "Object";

    /**
     * Get corresponding Java type for a given DB type. Returns Object if unknown.
     */
    public static String getJavaType(String dbType) {
        if (dbType == null || dbType.isBlank()) return JAVA_OBJECT;
        return TYPE_MAP.getOrDefault(dbType.trim().toUpperCase(), JAVA_OBJECT);
    }
}
