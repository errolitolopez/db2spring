package db2spring;

import com.errol.db2spring.Db2springGeneratorWeb;
import com.errol.db2spring.Db2springWebProperty;
import com.errol.db2spring.model.Dependency;
import com.errol.db2spring.model.FileModel;
import com.errol.db2spring.model.GeneratorProperty;
import com.errol.db2spring.model.ProjectInfo;
import com.errol.db2spring.model.TableMapping;
import com.errol.db2spring.model.TypeOverride;
import com.errol.db2spring.model.plugin.GenericPlugin;
import com.errol.db2spring.model.plugin.Plugin;
import com.errol.db2spring.utils.codegen.GeneratorPropertyUtil;
import com.errol.db2spring.utils.codegen.PackageUtil;
import com.errol.db2spring.utils.codegen.SuffixUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class Db2SpringGeneratorPropertyWebTest {

    @Test
    void testDb2springGeneratorWeb() {
        Db2springWebProperty property = buildProperty();

        Db2springGeneratorWeb db2springGeneratorWeb = new Db2springGeneratorWeb();

        List<FileModel> fileModels = db2springGeneratorWeb.generateJavaFiles(getSql(), property);

        FileModel mainApplicationFile = db2springGeneratorWeb.generateMainApplicationFile(property.getProjectInfo());
    }

    private Db2springWebProperty buildProperty() {
        return new Db2springWebProperty(
                new ProjectInfo("db2spring", "com.example", "db2spring", "17", "3.5.3"),
                buildDependencies(),
                buildTableMappings(),
                buildTypeOverrides(),
                buildGeneratorProperties(),
                buildPlugins()
        );
    }

    private static String getSql() {
        return """
                CREATE TABLE users (
                    user_id BIGINT PRIMARY KEY,
                    username VARCHAR(50) NOT NULL UNIQUE,
                    email VARCHAR(100) NOT NULL UNIQUE,
                    password_hash VARCHAR(255) NOT NULL,
                    display_name VARCHAR(100),
                    bio TEXT,
                    profile_picture_url VARCHAR(255),
                    is_active BOOLEAN DEFAULT TRUE,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    created_by BIGINT,
                    updated_by BIGINT
                );
                
                CREATE TABLE user_settings (
                    setting_id BIGINT PRIMARY KEY,
                    user_id BIGINT NOT NULL,
                    privacy_level VARCHAR(20) DEFAULT 'public',
                    email_notifications BOOLEAN DEFAULT TRUE,
                    push_notifications BOOLEAN DEFAULT TRUE,
                    theme VARCHAR(20) DEFAULT 'light',
                    language VARCHAR(10) DEFAULT 'en',
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    created_by BIGINT,
                    updated_by BIGINT,
                    FOREIGN KEY (user_id) REFERENCES users(user_id)
                );
                """;
    }

    private List<GeneratorProperty> buildGeneratorProperties() {
        return GeneratorPropertyUtil.GENERATORS.stream()
                .map(generator -> new GeneratorProperty(
                        generator,
                        true,
                        "src/main/java",
                        PackageUtil.resolveDefault(generator),
                        SuffixUtil.resolveDefault(generator)
                ))
                .toList();
    }

    private static List<TableMapping> buildTableMappings() {
        List<TableMapping> tableMappings = new ArrayList<>();

        TableMapping users = new TableMapping("users", "User", GeneratorPropertyUtil.GENERATORS);
        tableMappings.add(users);

        TableMapping userSettings = new TableMapping("user_settings", "UserSetting", GeneratorPropertyUtil.GENERATORS);
        tableMappings.add(userSettings);
        return tableMappings;
    }

    private static List<TypeOverride> buildTypeOverrides() {
        List<TypeOverride> typeOverrides = new ArrayList<>();

        TypeOverride dateTimeToInstant = new TypeOverride("TIMESTAMP", "Instant");

        typeOverrides.add(dateTimeToInstant);
        return typeOverrides;
    }


    private static List<Dependency> buildDependencies() {
        List<Dependency> dependencies = new ArrayList<>();

        Dependency springValidationDependency = new Dependency("org.springframework.boot", "spring-boot-starter-validation", "3.3.5");
        Dependency lombokDependency = new Dependency("org.projectlombok", "lombok", "1.18.34");
        Dependency mapstructDependency = new Dependency("org.mapstruct", "mapstruct", "1.6.3");

        dependencies.add(springValidationDependency);
        dependencies.add(lombokDependency);
        dependencies.add(mapstructDependency);
        return dependencies;
    }

    private static List<Plugin> buildPlugins() {
        Plugin lombok = new GenericPlugin("Lombok", "1.18.34");
        Plugin mapStruct = new GenericPlugin("Mapstruct", "1.5.5.Final");
        Plugin lombokMapstructBinding = new GenericPlugin("LombokMapstructBinding", "0.2.0");
        Plugin springValidation = new GenericPlugin("SpringBootStarterValidation");

        Plugin customMapper = new GenericPlugin("CustomMapper");

        List<Plugin> plugins = new ArrayList<>();
        plugins.add(lombok);
        plugins.add(mapStruct);
        plugins.add(springValidation);
        plugins.add(customMapper);
        plugins.add(lombokMapstructBinding);
        return plugins;
    }
}
