package com.errol.db2spring.utils.codegen;

import com.errol.db2spring.utils.StringUtil;
import com.errol.db2spring.utils.collection.MapUtil;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class ImportUtil {

    public static final Map<String, List<String>> REQUIRED_IMPORTS_MAP = Map.of(
            "controller", List.of(
                    "Dto",
                    "DtoCreate",
                    "DtoRequest",
                    "DtoResponse",
                    "DtoUpdate",
                    "Service"
            ),
            "service", List.of(
                    "Dto",
                    "DtoCreate",
                    "DtoRequest",
                    "DtoResponse",
                    "DtoUpdate"
            ),
            "service-impl", List.of(
                    "Dto",
                    "DtoCreate",
                    "DtoRequest",
                    "DtoResponse",
                    "DtoUpdate",
                    "Entity",
                    "Mapper",
                    "Repository",
                    "Service",
                    "SpecBuilder"
            ),
            "mapper", List.of(
                    "Dto",
                    "DtoCreate",
                    "DtoResponse",
                    "DtoUpdate",
                    "Entity"
            ),
            "repository", List.of(
                    "Entity"
            )
    );

    public static List<String> getRequiredImports(String type) {
        return Optional.ofNullable(REQUIRED_IMPORTS_MAP.get(type))
                .map(Collections::unmodifiableList)
                .orElse(List.of());
    }

    public static String getFieldImports(Set<String> fullyQualifiedNames) {
        return Optional.ofNullable(fullyQualifiedNames)
                .orElseGet(Set::of)
                .stream()
                .filter(StringUtil::isNotBlank)
                .map(fqn -> "import " + fqn + ";")
                .sorted()
                .collect(Collectors.joining("\n"));
    }

    public static String getClassImports(Map<String, Object> data, String type) {
        List<String> imports = new ArrayList<>();

        ImportUtil.getRequiredImports(type).forEach(name -> {
            String importClassName = MapUtil.getString(data, "className" + name);
            String importPackage = MapUtil.getString(data, "package" + name);
            imports.add("import " + importPackage + "." + importClassName + ";");
        });

        return imports
                .stream()
                .sorted()
                .collect(Collectors.joining("\n"));
    }
}
