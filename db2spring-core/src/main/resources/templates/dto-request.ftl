package ${packageDtoRequest};
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
public class ${classNameDtoRequest} {
<#list columns as column>

    private ${column.javaType} ${column.smartColumnName.toCamelCase()};
</#list>
<#list dateColumns as dateColumn>

    private ${dateColumn.javaType} ${dateColumn.smartColumnName.toCamelCase()}Start;

    private ${dateColumn.javaType} ${dateColumn.smartColumnName.toCamelCase()}End;
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
    <#list dateColumns as dateColumn>

    public ${dateColumn.javaType} get${dateColumn.smartColumnName.toPascalCase()}Start() {
        return ${dateColumn.smartColumnName.toCamelCase()}Start;
    }

    public void set${dateColumn.smartColumnName.toPascalCase()}Start(${dateColumn.javaType} ${dateColumn.smartColumnName.toCamelCase()}Start) {
        this.${dateColumn.smartColumnName.toCamelCase()}Start = ${dateColumn.smartColumnName.toCamelCase()}Start;
    }

    public ${dateColumn.javaType} get${dateColumn.smartColumnName.toPascalCase()}End() {
        return ${dateColumn.smartColumnName.toCamelCase()}End;
    }

    public void set${dateColumn.smartColumnName.toPascalCase()}End(${dateColumn.javaType} ${dateColumn.smartColumnName.toCamelCase()}End) {
        this.${dateColumn.smartColumnName.toCamelCase()}End = ${dateColumn.smartColumnName.toCamelCase()}End;
    }
    </#list>
</#if>
}
