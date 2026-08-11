package ${packageEntity};

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

<#if pluginLombok??>
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
</#if>
<#if fieldImports??>

${fieldImports}
</#if>

@Entity
@Table(name = "${tableName}")
<#if pluginLombok??>
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
