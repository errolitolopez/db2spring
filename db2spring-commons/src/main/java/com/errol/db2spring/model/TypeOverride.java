package com.errol.db2spring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class TypeOverride {
    private String columnName;
    private String sqlType;
    private String javaType;
}
