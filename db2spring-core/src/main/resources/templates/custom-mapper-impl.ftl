package ${packageMapper}.impl;

${classImports}
import ${packageMapper}.${classNameMapper};

@Component
public class ${classNameMapper}Impl implements ${classNameMapper} {
<#if classNameDto?? && classNameEntity??>

    public ${classNameDto} to${suffixDto}(${classNameEntity} ${suffixEntity.toCamelCase()}) {
        if (${suffixEntity.toCamelCase()} == null) return null;

        ${classNameDto} ${suffixDto.toCamelCase()} = new ${classNameDto}();
        <#list columns as column>
        ${suffixDto.toCamelCase()}.set${column.smartColumnName.toPascalCase()}(${suffixEntity.toCamelCase()}.get${column.smartColumnName.toPascalCase()}());
        </#list>
        return dto;
    }

    public ${classNameEntity} to${suffixEntity}(${classNameDto} ${suffixDto.toCamelCase()}) {
        if (${suffixDto.toCamelCase()} == null) return null;

        ${classNameEntity} ${suffixEntity.toCamelCase()} = new ${classNameEntity}();
        <#list columns as column>
        ${suffixEntity.toCamelCase()}.set${column.smartColumnName.toPascalCase()}(${suffixDto.toCamelCase()}.get${column.smartColumnName.toPascalCase()}());
        </#list>
        return ${suffixEntity.toCamelCase()};
    }

    public void from${suffixDto}(${classNameDto} source, ${classNameEntity} target) {
        if (source == null || target == null) return;

    <#list columns as column>
        target.set${column.smartColumnName.toPascalCase()}(source.get${column.smartColumnName.toPascalCase()}());
    </#list>
    }
</#if>

<#if classNameDtoCreate?? && classNameEntity??>
    public ${classNameDtoCreate} to${suffixDtoCreate}(${classNameEntity} ${suffixEntity.toCamelCase()}) {
        if (${suffixEntity.toCamelCase()} == null) return null;

        ${classNameDtoCreate} ${suffixDtoCreate.toCamelCase()} = new ${classNameDtoCreate}();
        <#list columns as column>
        ${suffixDtoCreate.toCamelCase()}.set${column.smartColumnName.toPascalCase()}(${suffixEntity.toCamelCase()}.get${column.smartColumnName.toPascalCase()}());
        </#list>
        return ${suffixDtoCreate.toCamelCase()};
    }

    public ${classNameEntity} from${suffixDtoCreate}(${classNameDtoCreate} ${suffixDtoCreate.toCamelCase()}) {
        if (${suffixDtoCreate.toCamelCase()} == null) return null;

        ${classNameEntity} ${suffixEntity.toCamelCase()} = new ${classNameEntity}();
        <#list columns as column>
        ${suffixEntity.toCamelCase()}.set${column.smartColumnName.toPascalCase()}(${suffixDtoCreate.toCamelCase()}.get${column.smartColumnName.toPascalCase()}());
        </#list>
        return ${suffixEntity.toCamelCase()};
    }
</#if>

<#if classNameDtoUpdate?? && classNameEntity??>
    public ${classNameDtoUpdate} to${suffixDtoUpdate}(${classNameEntity} ${suffixEntity.toCamelCase()}) {
        if (${suffixEntity.toCamelCase()} == null) return null;
    
        ${classNameDtoUpdate} ${suffixDtoUpdate.toCamelCase()} = new ${classNameDtoUpdate}();
        <#list columns as column>
        ${suffixDtoUpdate.toCamelCase()}.set${column.smartColumnName.toPascalCase()}(${suffixEntity.toCamelCase()}.get${column.smartColumnName.toPascalCase()}());
        </#list>
        return ${suffixDtoUpdate.toCamelCase()};
    }
    
    public ${classNameEntity} from${suffixDtoUpdate}(${classNameDtoUpdate} ${suffixDtoUpdate.toCamelCase()}) {
        if (${suffixDtoUpdate.toCamelCase()} == null) return null;
    
        ${classNameEntity} ${suffixEntity.toCamelCase()} = new ${classNameEntity}();
        <#list columns as column>
        ${suffixEntity.toCamelCase()}.set${column.smartColumnName.toPascalCase()}(${suffixDtoUpdate.toCamelCase()}.get${column.smartColumnName.toPascalCase()}());
        </#list>
        return ${suffixEntity.toCamelCase()};
    }

    public void from${suffixDtoUpdate}(${classNameDtoUpdate} source, ${classNameEntity} target) {
        if (source == null || target == null) return;
    
        <#list columns as column>
        target.set${column.smartColumnName.toPascalCase()}(source.get${column.smartColumnName.toPascalCase()}());
        </#list>
    }
</#if>

<#if classNameDtoResponse?? && classNameEntity??>
    public ${classNameDtoResponse} to${suffixDtoResponse}(${classNameEntity} ${suffixEntity.toCamelCase()}) {
        if (${suffixEntity.toCamelCase()} == null) return null;

        ${classNameDtoResponse} ${suffixDtoResponse.toCamelCase()} = new ${classNameDtoResponse}();
        <#list columns as column>
        ${suffixDtoResponse.toCamelCase()}.set${column.smartColumnName.toPascalCase()}(${suffixEntity.toCamelCase()}.get${column.smartColumnName.toPascalCase()}());
        </#list>
        return ${suffixDtoResponse.toCamelCase()};
    }
</#if>
}
