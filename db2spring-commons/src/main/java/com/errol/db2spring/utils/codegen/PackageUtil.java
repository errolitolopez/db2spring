package com.errol.db2spring.utils.codegen;

import com.errol.db2spring.utils.StringUtil;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PackageUtil {

    public static String resolveDefault(String type) {
        return resolveDefaultIfEmpty(null, type);
    }

    public static String resolveDefaultIfEmpty(String given, String type) {
        if (StringUtil.isNotBlank(given)) {
            return given;
        }
        switch (type) {
            case "controller":
                return "controller";
            case "dto", "dto-response", "dto-create", "dto-request", "dto-update":
                return "dto";
            case "mapper":
                return "mapper";
            case "repository":
                return "repository";
            case "service":
                return "service";
            case "service-impl":
                return "service.impl";
            case "spec-builder":
                return "shared";
            case "entity":
                return "entity";
            default:
                return "";
        }
    }
}
