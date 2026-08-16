<#assign requiredColumns = columns?filter(column -> !column.isNullable() && !column.primaryKey)>
<#assign nullableColumns = columns?filter(column -> column.isNullable() && !column.primaryKey)>
package ${packageTestSetup};

${classImports}
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
<#if fieldImports??>

${fieldImports}
</#if>
import java.util.ArrayList;
import java.util.List;

@Component
public class ${classNameTestSetup} {

    @Autowired
    private ${classNameRepository} ${classNameRepository.toCamelCase()};

    public void cleanUp() {
        ${classNameRepository.toCamelCase()}.deleteAll();
    }

    public ${className} create() {
        return create(null);
    }

    public List<${className}> create(int count) {
        List<${className}> entities = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            entities.add(buildEntity(i));
        }
        return ${classNameRepository.toCamelCase()}.saveAll(entities);
    }

    private ${className} create(Integer row) {
        return ${classNameRepository.toCamelCase()}.save(buildEntity(row));
    }

    public ${className} buildEntity() {
        return buildEntity(null);
    }

    private ${className} buildEntity(Integer row) {
        String suffix = row == null ? "" : "_" + row;
        ${className} entity = new ${className}();
        // required
<#list requiredColumns as column>
        <#if column.javaType == "String">
        entity.set${column.smartColumnName.toPascalCase()}("${column.smartColumnName.toUpperSnakeCase()}" + suffix);
        <#elseif column.javaType == "LocalDate">
        entity.set${column.smartColumnName.toPascalCase()}(LocalDate.of(LocalDate.now().getYear(), 12, 9));
        <#elseif column.javaType == "LocalDateTime">
        entity.set${column.smartColumnName.toPascalCase()}(LocalDateTime.of(LocalDateTime.now().getYear(), 12, 9, 0, 0));
        <#elseif column.javaType == "Byte">
        entity.set${column.smartColumnName.toPascalCase()}((byte) 12);
        <#elseif column.javaType == "Short">
        entity.set${column.smartColumnName.toPascalCase()}((short) 12);
        <#elseif column.javaType == "Integer">
        entity.set${column.smartColumnName.toPascalCase()}(1234);
        <#elseif column.javaType == "Long">
        entity.set${column.smartColumnName.toPascalCase()}(1234L);
        <#elseif column.javaType == "Float">
        entity.set${column.smartColumnName.toPascalCase()}(12.34F);
        <#elseif column.javaType == "Double">
        entity.set${column.smartColumnName.toPascalCase()}(12.34D);
        <#elseif column.javaType == "BigInteger">
        entity.set${column.smartColumnName.toPascalCase()}(BigInteger.valueOf(1234));
        <#elseif column.javaType == "BigDecimal">
        entity.set${column.smartColumnName.toPascalCase()}(new BigDecimal("12.34"));
        </#if>
</#list>
        // nullable
<#list nullableColumns as column>
        entity.set${column.smartColumnName.toPascalCase()}(null);
</#list>
        return entity;
    }
}