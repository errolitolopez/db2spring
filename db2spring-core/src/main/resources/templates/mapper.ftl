package ${packageMapper};

${classImports}
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ${classNameMapper} {
<#if classNameDto?? && classNameEntity??>
    ${classNameDto} to${suffixDto}(${classNameEntity} ${suffixEntity.toCamelCase()});

    ${classNameEntity} to${suffixEntity}(${classNameDto} ${suffixDto.toCamelCase()});

    @Mapping(target = "${idColumn.smartColumnName.toCamelCase()}", ignore = true)
    void from${suffixDto}(${classNameDto} source, @MappingTarget ${classNameEntity} target);
</#if>
<#if classNameDtoCreate?? && classNameEntity??>

    ${classNameDtoCreate} to${suffixDtoCreate}(${classNameEntity} ${suffixEntity});

    ${classNameEntity} from${suffixDtoCreate}(${classNameDtoCreate} ${suffixDtoCreate.toCamelCase()});
</#if>
<#if classNameDtoUpdate?? && classNameEntity??>

    ${classNameDtoUpdate} to${suffixDtoUpdate}(${classNameEntity} ${suffixEntity});

    ${classNameEntity} from${suffixDtoUpdate}(${classNameDtoUpdate} ${suffixDtoUpdate.toCamelCase()});

    @Mapping(target = "${idColumn.smartColumnName.toCamelCase()}", ignore = true)
    void from${suffixDtoUpdate}(${classNameDtoUpdate} source, @MappingTarget ${classNameEntity} target);
</#if>
<#if classNameDtoResponse?? && classNameEntity??>

    ${classNameDtoResponse} to${suffixDtoResponse}(${classNameEntity} ${suffixEntity});
</#if>
}
