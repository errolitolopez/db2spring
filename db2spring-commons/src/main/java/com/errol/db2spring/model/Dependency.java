package com.errol.db2spring.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Dependency {
    private final String groupId;
    private final String artifactId;
    private final String version;
    private final boolean versionRequired;
    private final String scope;

    public Dependency(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.versionRequired = true;
        this.scope = null;
    }
}
