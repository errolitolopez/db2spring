package ${packageDtoCreate};
<#if pluginSpringBootStarterValidation??>

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
</#if>
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
public class ${classNameDtoCreate} {
<#list columns as column>
<#if !column.primaryKey>

    <#if pluginSpringBootStarterValidation??>
        <#if column.javaType == "String">
            <#if !column.isNullable()>
    @NotBlank(message="${column.smartColumnName.toSentenceCase()} is required")
            </#if>
            <#if column.size?? && column.size gt 0>
    @Size(max=${column.size}, message="${column.smartColumnName.toSentenceCase()} must not exceed ${column.size} characters")
            </#if>
        <#else>
            <#if !column.isNullable()>
    @NotNull(message="${column.smartColumnName.toSentenceCase()} is required")
            </#if>
        </#if>
    </#if>
    private ${column.javaType} ${column.smartColumnName.toCamelCase()};
</#if>  
</#list>
<#if !pluginLombok??>
    <#list columns as column>
        <#if !column.primaryKey>

    public ${column.javaType} get${column.smartColumnName.toPascalCase()}() {
        return ${column.smartColumnName.toCamelCase()};
    }

    public void set${column.smartColumnName.toPascalCase()}(${column.javaType} ${column.smartColumnName.toCamelCase()}) {
        this.${column.smartColumnName.toCamelCase()} = ${column.smartColumnName.toCamelCase()};
    }
        </#if>
    </#list>
</#if>
}
