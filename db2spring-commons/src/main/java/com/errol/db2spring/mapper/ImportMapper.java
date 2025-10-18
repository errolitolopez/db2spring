package com.errol.db2spring.mapper;

import java.util.Map;

import static java.util.Map.entry;

public class ImportMapper {

    private static final Map<String, String> MAPPINGS = Map.ofEntries(
            entry("Clock", "java.time.Clock"),
            entry("DayOfWeek", "java.time.DayOfWeek"),
            entry("Duration", "java.time.Duration"),
            entry("Instant", "java.time.Instant"),
            entry("InstantSource", "java.time.InstantSource"),
            entry("LocalDate", "java.time.LocalDate"),
            entry("LocalDateTime", "java.time.LocalDateTime"),
            entry("LocalTime", "java.time.LocalTime"),
            entry("Month", "java.time.Month"),
            entry("MonthDay", "java.time.MonthDay"),
            entry("OffsetDateTime", "java.time.OffsetDateTime"),
            entry("OffsetTime", "java.time.OffsetTime"),
            entry("Period", "java.time.Period"),
            entry("Year", "java.time.Year"),
            entry("YearMonth", "java.time.YearMonth"),
            entry("ZoneId", "java.time.ZoneId"),
            entry("ZoneOffset", "java.time.ZoneOffset"),
            entry("ZonedDateTime", "java.time.ZonedDateTime"),
            entry("BigDecimal", "java.math.BigDecimal"),
            entry("BigInteger", "java.math.BigInteger")
    );

    public static Map<String, String> getMappings() {
        return MAPPINGS;
    }
}
