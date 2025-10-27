package ${packageController};

${classImports}
<#if pluginSpringBootStarterValidation??>
import jakarta.validation.Valid;
</#if>
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
<#if pluginSpringBootStarterValidation??>
import org.springframework.validation.annotation.Validated;
</#if>
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

<#if pluginSpringBootStarterValidation??>
@Validated
</#if>
@RestController
@RequestMapping("/api/${apiName.toKebabCase()}")
public class ${classNameController} {

    @Autowired
    private ${classNameService} ${classNameService.toCamelCase()};
    <#if classNameDtoCreate??>

    @PostMapping
    public ResponseEntity<${classNameDtoResponse!classNameDto}> create(@RequestBody <#if pluginSpringBootStarterValidation??>@Valid </#if>${classNameDtoCreate} ${suffixDtoCreate.toCamelCase()}) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(${classNameService.toCamelCase()}.create(${suffixDtoCreate.toCamelCase()}));
    }
    <#else>

    @PostMapping
    public ResponseEntity<${classNameDtoResponse!classNameDto}> create(@RequestBody <#if pluginSpringBootStarterValidation??>@Valid </#if>${classNameDto} ${suffixDto.toCamelCase()}) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(${classNameService.toCamelCase()}.create(${suffixDto.toCamelCase()}));
    }
    </#if>
    <#if classNameDtoUpdate??>

    @PutMapping
    public ResponseEntity<${classNameDtoResponse!classNameDto}> update(@RequestBody <#if pluginSpringBootStarterValidation??>@Valid </#if>${classNameDtoUpdate} ${suffixDtoUpdate.toCamelCase()}) {
        return ResponseEntity.ok(${classNameService.toCamelCase()}.update(${suffixDtoUpdate.toCamelCase()}));
    }
    <#else>

    @PutMapping
    public ResponseEntity<${classNameDtoResponse!classNameDto}> update(@RequestBody <#if pluginSpringBootStarterValidation??>@Valid </#if>${classNameDto} ${suffixDto.toCamelCase()}) {
        return ResponseEntity.ok(${classNameService.toCamelCase()}.update(${suffixDto.toCamelCase()}));
    }
    </#if>

    @GetMapping("/{${idColumn.smartColumnName.toCamelCase()}}")
    public ResponseEntity<${classNameDtoResponse!classNameDto}> details(@PathVariable ${idColumn.javaType} ${idColumn.smartColumnName.toCamelCase()}) {
        return ResponseEntity.ok(${classNameService.toCamelCase()}.details(${idColumn.smartColumnName.toCamelCase()}));
    }

    @GetMapping
    public ResponseEntity<Page<${classNameDtoResponse!classNameDto}>> search(<#if classNameDtoRequest??>${classNameDtoRequest} ${suffixDtoRequest.toCamelCase()}, </#if>Pageable pageable) {
        return ResponseEntity.ok(${classNameService.toCamelCase()}.search(<#if classNameDtoRequest??>${suffixDtoRequest.toCamelCase()}, </#if>pageable));
    }

    @DeleteMapping("/{${idColumn.smartColumnName.toCamelCase()}}")
    public ResponseEntity<Void> delete(@PathVariable ${idColumn.javaType} ${idColumn.smartColumnName.toCamelCase()}) {
        ${classNameService.toCamelCase()}.delete(${idColumn.smartColumnName.toCamelCase()});
        return ResponseEntity.noContent().build();
    }
}
