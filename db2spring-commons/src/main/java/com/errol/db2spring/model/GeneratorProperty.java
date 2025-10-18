package com.errol.db2spring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GeneratorProperty {
    private String type;
    private boolean generate;
    private String outputDir;
    private String subPackage;
    private String suffix;
}
