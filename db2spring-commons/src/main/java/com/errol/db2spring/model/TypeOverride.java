package com.errol.db2spring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class TypeOverride {
    private String columnName;
    private final String sqlType;
    private final String javaType;

    public TypeOverride(String sqlType, String javaType) {
        this.sqlType = sqlType;
        this.javaType = javaType;
    }
}
