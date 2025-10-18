package com.errol.db2spring.xml;

import com.errol.db2spring.model.DatabaseConnection;
import com.errol.db2spring.model.GeneratorProperty;
import com.errol.db2spring.model.ProjectInfo;
import com.errol.db2spring.model.SqlOption;
import com.errol.db2spring.model.TableMapping;
import com.errol.db2spring.model.TypeOverride;
import com.errol.db2spring.model.plugin.GenericPlugin;
import com.errol.db2spring.model.plugin.Plugin;
import com.errol.db2spring.utils.StringUtil;
import lombok.NoArgsConstructor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;

@NoArgsConstructor
public class XmlParser {

    public static DatabaseConnection parseDatabaseConnection(Document document) {
        Element element = (Element) document.getElementsByTagName("database-connection").item(0);

        String url = XmlUtil.getText(element, "url");
        String username = XmlUtil.getText(element, "user");
        String password = XmlUtil.getText(element, "password");
        String driverClass = XmlUtil.getText(element, "driver-class");
        String driverJar = XmlUtil.getText(element, "driver-jar");

        return new DatabaseConnection(url, username, password, driverClass, driverJar);
    }

    public static ProjectInfo parseProjectInfo(Document document) {
        Element element = (Element) document.getElementsByTagName("project-info").item(0);

        return new ProjectInfo(XmlUtil.getText(element, "project-name"),
                XmlUtil.getText(element, "group-id"),
                XmlUtil.getText(element, "artifact-id"),
                null,
                null
        );
    }

    public static List<TableMapping> parseTableMappings(Document document) {
        return XmlUtil.loadConfigs(document, "table", XmlParser::parseTableMapping);
    }

    private static TableMapping parseTableMapping(Element element) {
        return new TableMapping(element.getAttribute("table-name"), element.getAttribute("class-name"));
    }

    public static List<GeneratorProperty> parseGenerators(Document document) {
        return XmlUtil.loadConfigs(document, "generator", XmlParser::parseGenerator);
    }

    private static GeneratorProperty parseGenerator(Element element) {
        String generate = element.getAttribute("generate");

        return new GeneratorProperty(
                element.getAttribute("type"),
                StringUtil.isBlank(generate) || generate.equalsIgnoreCase("true"),
                element.getAttribute("output-dir"),
                element.getAttribute("sub-package"),
                element.getAttribute("suffix")
        );
    }

    public static List<Plugin> parsePlugins(Document document) {
        return XmlUtil.loadConfigs(document, "plugin", XmlParser::parsePlugin);
    }

    private static Plugin parsePlugin(Element el) {
        return new GenericPlugin(el.getAttribute("name"));
    }

    public static List<TypeOverride> parseTypeOverrides(Document doc) {
        return XmlUtil.loadConfigs(doc, "type-override", XmlParser::parseTypeOverride);
    }

    private static TypeOverride parseTypeOverride(Element element) {
        return new TypeOverride(
                element.getAttribute("column-name"),
                element.getAttribute("sql-type"),
                element.getAttribute("java-type")
        );
    }

    public static SqlOption parseSqlOption(Document doc) {
        if (doc == null) {
            return null;
        }

        Element root = doc.getDocumentElement();
        if (root == null) {
            return null;
        }

        String sqlStatement = XmlUtil.getText(root, "sql");
        String src = XmlUtil.getAttribute(doc, "sql-file", "src");

        if (sqlStatement != null || src != null) {
            return new SqlOption(src, sqlStatement);
        }

        return null;
    }
}