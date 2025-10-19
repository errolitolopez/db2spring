package com.errol.db2spring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Dependency {
    private String groupId;
    private String artifactId;
    private String version;
    private boolean versionRequired = true;
    private String scope;
}
