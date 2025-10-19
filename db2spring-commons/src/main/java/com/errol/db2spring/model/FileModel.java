package com.errol.db2spring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class FileModel {
    private String type;
    private String group;
    private String outputDir;
    private String rootPackage;
    private String subPackage;
    private String fullyQualifiedPackage;
    private String fullPath;
    private String filename;
    private String fileExtension;
    private String content;

    public String getFullyQualifiedPackage() {
        if (rootPackage == null && subPackage == null) return null;
        if (subPackage == null || subPackage.isBlank()) {
            return rootPackage;
        }
        return rootPackage + "." + subPackage;
    }

    public String getFullPath() {
        if (outputDir == null) return null;

        String javaPath = getFullyQualifiedPackage();
        String path = (javaPath != null) ? javaPath.replace('.', '/') + "/" : "";

        return outputDir + "/" + path + filename + "." + fileExtension;
    }
}
