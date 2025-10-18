package com.errol.db2spring.utils.codegen;

import com.errol.db2spring.utils.StringUtil;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SuffixUtil {

    public static String resolveFinalSuffix(String type, String suffix) {
        if (type.equals("entity") && StringUtil.isBlank(suffix)) {
            return "Entity";
        }
        return suffix;
    }

    public static String resolveDefault(String type) {
        return resolveDefaultIfEmpty(null, type);
    }

    public static String resolveDefaultIfEmpty(String given, String type) {
        if (StringUtil.isNotBlank(given)) {
            return given;
        }
        switch (type) {
            case "controller":
                return "Controller";
            case "dto":
                return "Dto";
            case "dto-create":
                return "CreateDto";
            case "dto-request":
                return "Request";
            case "dto-response":
                return "Response";
            case "dto-update":
                return "UpdateDto";
            case "mapper":
                return "Mapper";
            case "repository":
                return "Repository";
            case "service":
                return "Service";
            case "service-impl":
                return "ServiceImpl";
            case "spec-builder":
                return "SpecBuilder";
            default:
                return "";
        }
    }
}
