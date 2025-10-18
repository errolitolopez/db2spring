package com.errol.db2spring.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DatabaseConnection {
    private final String url;
    private final String username;
    private final String password;
    private final String driverClass;
    private final String driverJar;
}
