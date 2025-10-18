package ${packageService};

${classImports}
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ${classNameService} {
    <#if classNameDtoCreate??>
    ${classNameDtoResponse!classNameDto} create(${classNameDtoCreate} ${suffixDtoCreate.toCamelCase()});
    <#else>

    ${classNameDtoResponse!classNameDto} create(${classNameDto} ${suffixDto.toCamelCase()});
    </#if>
    <#if classNameDtoUpdate??>

    ${classNameDtoResponse!classNameDto} update(${classNameDtoUpdate} ${suffixDtoUpdate.toCamelCase()});
    <#else>

    ${classNameDtoResponse!classNameDto} update(${classNameDto} ${suffixDto.toCamelCase()});
    </#if>

    ${classNameDtoResponse!classNameDto} details(${idColumn.javaType} ${idColumn.smartColumnName.toCamelCase()});
    <#if classNameDtoRequest??>

    Page<${classNameDtoResponse!classNameDto}> search(${classNameDtoRequest} ${suffixDtoRequest.toCamelCase()}, Pageable pageable);
    <#else>

    Page<${classNameDtoResponse!classNameDto}> search(Pageable pageable);
    </#if>

    void delete(${idColumn.javaType} ${idColumn.smartColumnName.toCamelCase()});
}
