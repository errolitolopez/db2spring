package com.errol.db2spring.model;

import com.errol.db2spring.utils.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ProjectInfo {
    private String projectName;
    private String groupId;
    private String artifactId;
    private String javaVersion;
    private String springBootVersion;
    private String fileStructure;

    public String getGroupId() {
        String id = StringUtil.isNotBlank(groupId) ? groupId : "com.example";
        id = sanitizeGroupId(id);
        return id;
    }

    public String getArtifactId() {
        String id;
        if (StringUtil.isNotBlank(artifactId)) {
            id = artifactId;
        } else if (StringUtil.isNotBlank(projectName)) {
            id = projectName;
        } else {
            id = "demo";
        }
        id = sanitizeArtifactOrProjectName(id);
        return id;
    }

    public String getProjectName() {
        if (StringUtil.isNotBlank(projectName)) {
            return sanitizeArtifactOrProjectName(projectName);
        }
        if (StringUtil.isNotBlank(artifactId)) {
            return sanitizeArtifactOrProjectName(artifactId);
        }
        return "demo";
    }

    private String sanitizeGroupId(String id) {
        id = id.trim();
        if (id.endsWith(".")) {
            id = id.substring(0, id.length() - 1);
        }
        id = id.replaceAll("[^a-zA-Z0-9.]", "");
        String[] parts = StringUtil.split(id, ".");
        return parts.length == 1 ? id + ".example" : id;
    }

    private String sanitizeArtifactOrProjectName(String id) {
        id = id.trim();
        if (id.endsWith(".")) {
            id = id.substring(0, id.length() - 1);
        }
        id = id.replace(" ", "-");
        id = id.replaceAll("[^a-zA-Z0-9.-]", "");
        return id;
    }

    public String getFileStructure() {
        if (StringUtil.isBlank(fileStructure)) {
            return "layered";
        }
        return fileStructure;
    }
}
