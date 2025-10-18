package com.errol.db2spring.utils.codegen;

import com.errol.db2spring.model.ProjectInfo;
import com.errol.db2spring.utils.StringUtil;
import lombok.experimental.UtilityClass;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class ProjectInfoUtil {

    public static String getRootPackage(ProjectInfo info) {
        if (info == null) return "com.example.demo";

        String groupId = info.getGroupId();
        String artifactId = info.getArtifactId();
        String projectName = info.getProjectName();

        return Stream.of(groupId, artifactId, projectName)
                .filter(StringUtil::isNotBlank)
                .distinct()
                .map(String::toLowerCase)
                .collect(Collectors.joining("."));
    }


    public static String getProjectName(ProjectInfo projectInfo) {
        if (projectInfo == null) return "demo";

        String name = projectInfo.getProjectName();
        if (StringUtil.isNotBlank(name)) return name;

        String artifact = projectInfo.getArtifactId();
        if (StringUtil.isNotBlank(artifact)) return StringUtil.extractLastSegment(artifact, ".");

        return "demo";
    }
}
