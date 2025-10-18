<#noparse><?xml version="1.0" encoding="UTF-8"?></#noparse>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>${projectInfo.springBootVersion}</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>

    <groupId>${projectInfo.groupId}</groupId>
	<artifactId>${projectInfo.artifactId}</artifactId>
	<version>v1.0.0</version>
	<name>${projectInfo.artifactId}</name>
	<description>Demo project for Spring Boot</description>

	<properties>
		<java.version>${projectInfo.javaVersion}</java.version>
		<#if pluginLombok??>
        <lombok.version>${pluginLombok.value}</lombok.version>
        </#if>
        <#if pluginMapstruct??>
        <mapstruct.version>${pluginMapstruct.value}</mapstruct.version>
        </#if>
        <#if pluginLombok?? && pluginMapstruct?? && pluginLombokMapstructBinding??>
        <lombok-mapstruct-binding.version>${pluginLombokMapstructBinding.value}</lombok-mapstruct-binding.version>
        </#if>
	</properties>

    <dependencies>
    <#if dependencies??>
	<#list dependencies as dependency>
	    <dependency>
	        <groupId>${dependency.groupId}</groupId>
	        <artifactId>${dependency.artifactId}</artifactId>
            <#if dependency.versionRequired && dependency.version?has_content>
	        <version>${dependency.version}</version>
            </#if>
            <#if dependency.scope?has_content>
            <scope>${dependency.scope}</scope>
            </#if>
	    </dependency>
	</#list>
    </#if>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <#if pluginLombok??>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
                </#if>
            </plugin>
            <#if pluginLombok?? || pluginMapstruct??>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <#noparse>
                    <source>${java.version}</source>
                    <target>${java.version}</target>
                    </#noparse>
                    <annotationProcessorPaths>
                        <#if pluginLombok??>
                        <path>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                            <version><#noparse>${lombok.version}</#noparse></version>
                        </path>
                        </#if>
                        <#if pluginMapstruct??>
                        <path>
                            <groupId>org.mapstruct</groupId>
                            <artifactId>mapstruct-processor</artifactId>
                            <version><#noparse>${mapstruct.version}</#noparse></version>
                        </path>
                        </#if>
                        <#if pluginLombok?? && pluginMapstruct?? && pluginLombokMapstructBinding??>
                        <path>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok-mapstruct-binding</artifactId>
                            <version><#noparse>{lombok-mapstruct-binding.version}</#noparse></version>
                        </path>
                        </#if>
                    </annotationProcessorPaths>
                </configuration>
            </plugin>
            </#if>
        </plugins>
    </build>
</project>
