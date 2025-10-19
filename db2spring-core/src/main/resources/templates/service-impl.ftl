package ${packageServiceImpl};

${classImports}
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
<#if classNameSpecBuilder??>
import org.springframework.data.jpa.domain.Specification;
</#if>
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ${classNameServiceImpl} implements ${classNameService} {

    @Autowired
    private ${classNameRepository} ${classNameRepository.toCamelCase()};

    @Autowired
    private ${classNameMapper} ${classNameMapper.toCamelCase()};
    <#if classNameDtoCreate??>

    @Override
    @Transactional
    public ${classNameDtoResponse!classNameDto} create(${classNameDtoCreate} ${suffixDtoCreate.toCamelCase()}) {
        ${classNameEntity} new${className} = ${classNameMapper.toCamelCase()}.from${suffixDtoCreate}(${suffixDtoCreate.toCamelCase()});

        ${classNameEntity} created${className} = ${classNameRepository.toCamelCase()}.save(new${className});
        return ${classNameMapper.toCamelCase()}.to${suffixDtoResponse!suffixDto}(created${className});
    }
    <#else>

    @Override
    @Transactional
    public ${classNameDtoResponse!classNameDto} create(${classNameDto} ${suffixDto.toCamelCase()}) {
        ${classNameEntity} new${className} = ${classNameMapper.toCamelCase()}.to${suffixEntity}(${suffixDto.toCamelCase()});

        ${classNameEntity} created${className} = ${classNameRepository.toCamelCase()}.save(new${className});
        return ${classNameMapper.toCamelCase()}.to${suffixDtoResponse!suffixDto}(created${className});
    }
    </#if>
    <#if classNameDtoUpdate??>

    @Override
    @Transactional
    public ${classNameDtoResponse!classNameDto} update(${classNameDtoUpdate} ${suffixDtoUpdate.toCamelCase()}) {
        ${classNameEntity} found${className} = getBy${idColumn.smartColumnName.toPascalCase()}(${suffixDtoUpdate.toCamelCase()}.get${idColumn.smartColumnName.toPascalCase()}());
        ${classNameMapper.toCamelCase()}.from${suffixDtoUpdate}(${suffixDtoUpdate.toCamelCase()}, found${className});

        ${classNameEntity} updated${className} = ${classNameRepository.toCamelCase()}.save(found${className});
        return ${classNameMapper.toCamelCase()}.to${suffixDtoResponse!suffixDto}(updated${className});
    }
    <#else>

    @Override
    @Transactional
    public ${classNameDtoResponse!classNameDto} update(${classNameDto} ${suffixDto.toCamelCase()}) {
        ${classNameEntity} found${className} = getBy${idColumn.smartColumnName.toPascalCase()}(${suffixDto.toCamelCase()}.get${idColumn.smartColumnName.toPascalCase()}());
        ${classNameMapper.toCamelCase()}.from${suffixDto}(${suffixDto.toCamelCase()}, found${className});

        ${classNameEntity} updated${className} = ${classNameRepository.toCamelCase()}.save(found${className});
        return ${classNameMapper.toCamelCase()}.to${suffixDtoResponse!suffixDto}(updated${className});
    }
    </#if>

    @Override
    @Transactional(readOnly = true)
    public ${classNameDtoResponse!classNameDto} details(${idColumn.javaType} ${idColumn.smartColumnName.toCamelCase()}) {
        ${classNameEntity} found${className} = getBy${idColumn.smartColumnName.toPascalCase()}(${idColumn.smartColumnName.toCamelCase()});

        return ${classNameMapper.toCamelCase()}.to${suffixDtoResponse!suffixDto}(found${className});
    }
    <#if classNameSpecBuilder?? && classNameDtoRequest??>

    @Override
    @Transactional(readOnly = true)
    public Page<${classNameDtoResponse!classNameDto}> search(${classNameDtoRequest} ${suffixDtoRequest.toCamelCase()}, Pageable pageable) {
        Specification<${classNameEntity}> spec = (root, criteriaQuery, criteriaBuilder) ->
                ${classNameSpecBuilder}.of(root, criteriaBuilder)
    <#list columns as column>
                     <#if column.javaType == "String">
                        .andLike("${column.smartColumnName.toCamelCase()}", ${suffixDtoRequest.toCamelCase()}.get${column.smartColumnName.toPascalCase()}())
                     <#elseif !column.date>
                        .andEqual("${column.smartColumnName.toCamelCase()}", ${suffixDtoRequest.toCamelCase()}.get${column.smartColumnName.toPascalCase()}())
                     </#if>
    </#list>
    <#list dateColumns as dateColumn>
                        .andRange("${dateColumn.smartColumnName.toCamelCase()}", ${suffixDtoRequest.toCamelCase()}.get${dateColumn.smartColumnName.toPascalCase()}Start(), ${suffixDtoRequest.toCamelCase()}.get${dateColumn.smartColumnName.toPascalCase()}End())
    </#list>
                        .build();

        return ${classNameRepository.toCamelCase()}.findAll(spec, pageable)
                .map(${suffixEntity.toCamelCase()} -> ${classNameMapper.toCamelCase()}.to${suffixDtoResponse!suffixDto}(${suffixEntity.toCamelCase()}));
    }
    <#else>

    @Override
    @Transactional(readOnly = true)
    public Page<${classNameDtoResponse!classNameDto}> search(Pageable pageable) {
        return ${classNameRepository.toCamelCase()}.findAll(pageable)
                .map(${suffixEntity.toCamelCase()} -> ${classNameMapper.toCamelCase()}.to${suffixDtoResponse!suffixDto}(${suffixEntity.toCamelCase()}));
    }
    </#if>

    @Override
    @Transactional
    public void delete(${idColumn.javaType} ${idColumn.smartColumnName.toCamelCase()}) {
        getBy${idColumn.smartColumnName.toPascalCase()}(${idColumn.smartColumnName.toCamelCase()});

        ${classNameRepository.toCamelCase()}.deleteById(${idColumn.smartColumnName.toCamelCase()});
    }

    private ${classNameEntity} getBy${idColumn.smartColumnName.toPascalCase()}(${idColumn.javaType} ${idColumn.smartColumnName.toCamelCase()}) {
        return ${classNameRepository.toCamelCase()}.findById(${idColumn.smartColumnName.toCamelCase()})
                .orElseThrow(() -> new IllegalStateException("${className.toSentenceCase()} not found with id: " + ${idColumn.smartColumnName.toCamelCase()}));
    }
}
