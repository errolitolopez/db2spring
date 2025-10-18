package ${packageEntity};

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

<#if pluginLombok??>
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
</#if>
<#if fieldImports??>

${fieldImports}
</#if>

@Entity
@Table(name = "${tableName}")
<#if pluginLombok??>
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
</#if>
public class ${classNameEntity} {
<#list columns as column>

<#if column.isPrimaryKey()>
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
</#if>
    @Column(name = "${column.columnName}")
    private ${column.javaType} ${column.smartColumnName};
</#list>
<#if !pluginLombok??>
    <#list columns as column>

    public ${column.javaType} get${column.smartColumnName.toPascalCase()}() {
        return ${column.smartColumnName.toPascalCase()};
    }

    public void set${column.smartColumnName.toPascalCase()}(${column.javaType} ${column.smartColumnName}) {
        this.${column.smartColumnName} = ${column.smartColumnName};
    }
    </#list>
</#if>
}
