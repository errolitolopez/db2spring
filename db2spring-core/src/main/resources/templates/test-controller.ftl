<#assign requiredColumns = columns?filter(column -> !column.isNullable() && !column.primaryKey)>
<#assign payloadColumns = columns?filter(column -> !column.primaryKey)>
package ${packageTestController};

${classImports}
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
<#if fieldImports??>

${fieldImports}
</#if>

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs("target/generated-snippets")
class ${classNameTestController} {

    private static final String BASE_API_URL = "/api/${apiName.toKebabCase()}";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ${classNameTestSetup} dataSetup;

    @Autowired
    private ${classNameMapper} ${classNameMapper.toCamelCase()};

    @BeforeEach
    void setUp() {
        dataSetup.cleanUp();
    }

    @Test
    void create_success() throws Exception {
        ${classNameDtoCreate} createDto = new ${classNameDtoCreate}();
<#list requiredColumns as column>
    <#if column.javaType == "String">
        createDto.set${column.smartColumnName.toPascalCase()}("${column.smartColumnName.toUpperSnakeCase()}");
    <#elseif column.javaType == "LocalDate">
        createDto.set${column.smartColumnName.toPascalCase()}(LocalDate.of(LocalDate.now().getYear(), 12, 9));
    <#elseif column.javaType == "LocalDateTime">
        createDto.set${column.smartColumnName.toPascalCase()}(LocalDateTime.of(LocalDateTime.now().getYear(), 12, 9, 0, 0));
    <#elseif column.javaType == "Byte">
        createDto.set${column.smartColumnName.toPascalCase()}((byte) 12);
    <#elseif column.javaType == "Short">
        createDto.set${column.smartColumnName.toPascalCase()}((short) 12);
    <#elseif column.javaType == "Integer">
        createDto.set${column.smartColumnName.toPascalCase()}(1234);
    <#elseif column.javaType == "Long">
        createDto.set${column.smartColumnName.toPascalCase()}(1234L);
    <#elseif column.javaType == "Float">
        createDto.set${column.smartColumnName.toPascalCase()}(12.34F);
    <#elseif column.javaType == "Double">
        createDto.set${column.smartColumnName.toPascalCase()}(12.34D);
    <#elseif column.javaType == "BigInteger">
        createDto.set${column.smartColumnName.toPascalCase()}(BigInteger.valueOf(1234));
    <#elseif column.javaType == "BigDecimal">
        createDto.set${column.smartColumnName.toPascalCase()}(new BigDecimal("12.34"));
    </#if>
</#list>
        mockMvc.perform(post(BASE_API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andDo(document(
                        "${apiName.toKebabCase()}/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
<#list payloadColumns as column>
                                fieldWithPath("${column.smartColumnName.toCamelCase()}").type("${column.getJsonFieldType()}").description("${column.smartColumnName.toSentenceCase()}").optional()<#sep>,
</#list>
),
                        relaxedResponseFields(
<#list columns as column>
                                fieldWithPath("${column.smartColumnName.toCamelCase()}").type("${column.getJsonFieldType()}").description("${column.smartColumnName.toSentenceCase()}").optional()<#sep>,
</#list>
)
                ));
    }

    @Test
    void update_success() throws Exception {
        ${className} created${className} = dataSetup.create();
        ${classNameDtoUpdate} updateDto = ${classNameMapper.toCamelCase()}.toUpdateDto(created${className});
        mockMvc.perform(put(BASE_API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andDo(document(
                        "${apiName.toKebabCase()}/update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
<#list columns as column>
                                fieldWithPath("${column.smartColumnName.toCamelCase()}").type("${column.getJsonFieldType()}").description("${column.smartColumnName.toSentenceCase()}").optional()<#sep>,
</#list>
),
                        relaxedResponseFields(
<#list columns as column>
                                fieldWithPath("${column.smartColumnName.toCamelCase()}").type("${column.getJsonFieldType()}").description("${column.smartColumnName.toSentenceCase()}").optional()<#sep>,
</#list>
)
                ));
    }

    @Test
    void details_success() throws Exception {
        ${className} created${className} = dataSetup.create();
        mockMvc.perform(get(BASE_API_URL + "/{${idColumn.smartColumnName.toCamelCase()}}", created${className}.get${idColumn.smartColumnName.toPascalCase()}()))
                .andExpect(status().isOk())
                .andDo(document(
                        "${apiName.toKebabCase()}/details",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("${idColumn.smartColumnName.toCamelCase()}").description("The ${idColumn.smartColumnName.toCamelCase()} of the ${className.toSentenceCase().uncapFirst()} to retrieve")
                        ),
                        relaxedResponseFields(
<#list columns as column>
                                fieldWithPath("${column.smartColumnName.toCamelCase()}").type("${column.getJsonFieldType()}").description("${column.smartColumnName.toSentenceCase()}").optional()<#sep>,
</#list>
                        )
                ));
    }

    @Test
    void search_success() throws Exception {
        dataSetup.create(3);
        mockMvc.perform(get(BASE_API_URL)
                        .param("page", "0")
                        .param("size", "20")
                        .param("sort", "${idColumn.smartColumnName.toCamelCase()},desc"))
                .andDo(document(
                        "${apiName.toKebabCase()}/search",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestParameters(
                                parameterWithName("page").description("Zero-based page number"),
                                parameterWithName("size").description("Number of records per page"),
                                parameterWithName("sort").description("Sorting criteria in the format `property,(asc|desc)`").optional()
                        ),
                        relaxedResponseFields(
                                fieldWithPath("content").description("The list of ${className.toSentenceCase()}"),
<#list columns as column>
                                fieldWithPath("content[].${column.smartColumnName.toCamelCase()}").type("${column.getJsonFieldType()}").description("${column.smartColumnName.toSentenceCase()}").optional()<#sep>,
</#list>
                        )
                ));
    }

    @Test
    void delete_success() throws Exception {
        ${className} created${className} = dataSetup.create();
        mockMvc.perform(delete(BASE_API_URL + "/{${idColumn.smartColumnName.toCamelCase()}}", created${className}.get${idColumn.smartColumnName.toPascalCase()}()))
                .andExpect(status().isNoContent())
                .andDo(document(
                        "${apiName.toKebabCase()}/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("${idColumn.smartColumnName.toCamelCase()}").description("The ${idColumn.smartColumnName.toCamelCase()} of the ${className.toSentenceCase().uncapFirst()} to delete")
                        )
                ));
    }
}
