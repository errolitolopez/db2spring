package com.errol.db2spring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableMapping {
    private String tableName;
    private String className;

    private List<String> generators;

    public TableMapping(String tableName, String className) {
        this.tableName = tableName;
        this.className = className;
    }
}
