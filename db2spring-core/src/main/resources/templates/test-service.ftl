<#assign requiredColumns = columns?filter(column -> !column.isNullable() && !column.primaryKey)>
package ${packageTestService};

${classImports}
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class ${classNameTestService} {

    @Autowired
    private ${classNameTestSetup} ${classNameTestSetup.toCamelCase()};

    @Autowired
    private ${classNameService} ${classNameService.toCamelCase()};

    @BeforeEach
    void setUp() {
        ${classNameTestSetup.toCamelCase()}.cleanUp();
    }

    @Test
    void create_shouldSaveAndReturnResponseDto() {
        // stub
    }

    @Test
    void update_shouldUpdateExisting${className}AndReturnResponseDto() {
        // stub
    }

    @Test
    void search_shouldReturnPageOfResponseDtos() {
        // stub
    }

    @Test
    void delete_shouldRemoveExisting${className}() {
        // stub
    }
}