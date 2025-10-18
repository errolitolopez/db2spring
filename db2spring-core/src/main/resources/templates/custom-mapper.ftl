package ${packageMapper};

${classImports}

public interface ${classNameMapper} {
<#if classNameDto?? && classNameEntity??>
    ${classNameDto} to${suffixDto}(${classNameEntity} ${suffixEntity.toCamelCase()});

    ${classNameEntity} to${suffixEntity}(${classNameDto} ${suffixDto.toCamelCase()});

    void from${suffixDto}(${classNameDto} source, ${classNameEntity} target);
</#if>
<#if classNameDtoCreate?? && classNameEntity??>

    ${classNameDtoCreate} to${suffixDtoCreate}(${classNameEntity} ${suffixEntity});

    ${classNameEntity} from${suffixDtoCreate}(${classNameDtoCreate} ${suffixDtoCreate.toCamelCase()});
</#if>
<#if classNameDtoUpdate?? && classNameEntity??>

    ${classNameDtoUpdate} to${suffixDtoUpdate}(${classNameEntity} ${suffixEntity});

    ${classNameEntity} from${suffixDtoUpdate}(${classNameDtoUpdate} ${suffixDtoUpdate.toCamelCase()});

    void from${suffixDtoUpdate}(${classNameDtoUpdate} source, ${classNameEntity} target);
</#if>
<#if classNameDtoResponse?? && classNameEntity??>

    ${classNameDtoResponse} to${suffixDtoResponse}(${classNameEntity} ${suffixEntity});
</#if>
}
