package com.errol.db2spring.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SqlOption {
    private final String src;
    private final String sql;
}
