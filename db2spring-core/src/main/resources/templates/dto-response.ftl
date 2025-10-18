package ${packageDtoResponse};
<#if pluginLombok??>

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
</#if>
<#if fieldImports??>

${fieldImports}
</#if>

<#if pluginLombok??>
@Data
@NoArgsConstructor
@AllArgsConstructor
</#if>
public class ${classNameDtoResponse} {
<#list columns as column>

    private ${column.javaType} ${column.smartColumnName.toCamelCase()};
</#list>
<#if !pluginLombok??>
    <#list columns as column>

    public ${column.javaType} get${column.smartColumnName.toPascalCase()}() {
        return ${column.smartColumnName.toCamelCase()};
    }

    public void set${column.smartColumnName.toPascalCase()}(${column.javaType} ${column.smartColumnName.toCamelCase()}) {
        this.${column.smartColumnName.toCamelCase()} = ${column.smartColumnName.toCamelCase()};
    }
    </#list>
</#if>
}
