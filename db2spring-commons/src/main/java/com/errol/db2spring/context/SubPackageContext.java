package com.errol.db2spring.context;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SubPackageContext {
    private String fileStructure;
    private String type;
    private String className;
    private String subPackage;
}
