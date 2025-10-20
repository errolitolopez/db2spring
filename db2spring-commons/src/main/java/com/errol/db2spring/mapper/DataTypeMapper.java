package com.errol.db2spring.mapper;

import lombok.experimental.UtilityClass;

import java.util.Map;

import static java.util.Map.entry;

@UtilityClass
public final class DataTypeMapper {

    private static final Map<String, String> TYPE_MAP = Map.<String, String>ofEntries(
            // Numeric types
            entry("SMALLINT", "Short"),
            entry("INT2", "Short"),
            entry("INTEGER", "Integer"),
            entry("INT", "Integer"),
            entry("INT4", "Integer"),
            entry("BIGINT", "Long"),
            entry("INT8", "Long"),
            entry("SERIAL", "Long"),
            entry("BIGSERIAL", "Long"),
            entry("DECIMAL", "BigDecimal"),
            entry("NUMERIC", "BigDecimal"),
            entry("REAL", "Float"),
            entry("FLOAT4", "Float"),
            entry("DOUBLE PRECISION", "Double"),
            entry("FLOAT8", "Double"),
            entry("MONEY", "BigDecimal"),

            // Boolean
            entry("BOOLEAN", "Boolean"),
            entry("BOOL", "Boolean"),

            // Character / String
            entry("CHAR", "String"),
            entry("CHARACTER", "String"),
            entry("VARCHAR", "String"),
            entry("CHARACTER VARYING", "String"),
            entry("TEXT", "String"),
            entry("CITEXT", "String"), // case-insensitive text extension
            entry("UUID", "String"),
            entry("JSON", "String"),
            entry("JSONB", "String"),
            entry("XML", "String"),
            entry("ENUM", "String"),

            // Binary types
            entry("BYTEA", "byte[]"),

            // Date / Time
            entry("DATE", "LocalDate"),
            entry("TIME", "LocalTime"),
            entry("TIME WITHOUT TIME ZONE", "LocalTime"),
            entry("TIME WITH TIME ZONE", "OffsetTime"),
            entry("TIMESTAMP", "LocalDateTime"),
            entry("TIMESTAMP WITHOUT TIME ZONE", "LocalDateTime"),
            entry("TIMESTAMP WITH TIME ZONE", "OffsetDateTime"),
            entry("TIMESTAMPTZ", "OffsetDateTime"),
            entry("INTERVAL", "Duration"),

            // Network types
            entry("CIDR", "String"),
            entry("INET", "String"),
            entry("MACADDR", "String"),
            entry("MACADDR8", "String"),

            // Geometric types
            entry("POINT", "String"),
            entry("LINE", "String"),
            entry("LSEG", "String"),
            entry("BOX", "String"),
            entry("PATH", "String"),
            entry("POLYGON", "String"),
            entry("CIRCLE", "String"),

            // Full text search
            entry("TSVECTOR", "String"),
            entry("TSQUERY", "String"),

            // Bit strings
            entry("BIT", "Boolean"),
            entry("VARBIT", "String"),
            entry("BIT VARYING", "String"),

            // Other / system
            entry("OID", "Long"),
            entry("REGCLASS", "String"),
            entry("PG_LSN", "String"),
            entry("TXID_SNAPSHOT", "String"),

            // Arrays — typically represented as List or array
            entry("INT[]", "Integer[]"),
            entry("TEXT[]", "String[]"),
            entry("VARCHAR[]", "String[]"),
            entry("UUID[]", "String[]"),
            entry("BYTEA[]", "byte[][]"),
            entry("NUMERIC[]", "BigDecimal[]"),
            entry("BOOLEAN[]", "Boolean[]")
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
