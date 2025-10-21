package com.errol.db2spring.xml;

import com.errol.db2spring.exception.Db2springException;
import com.errol.db2spring.model.Db2springXml;
import lombok.NoArgsConstructor;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

@NoArgsConstructor
public class XmlLoader {

    public static Db2springXml load(String filePath) {
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(filePath));
            doc.getDocumentElement().normalize();

            return new Db2springXml(
                    XmlParser.parseDatabaseConnection(doc),
                    XmlParser.parseSqlOption(doc),
                    XmlParser.parseProjectInfo(doc),
                    XmlParser.parseTableMappings(doc),
                    XmlParser.parseTypeOverrides(doc),
                    XmlParser.parseGenerators(doc),
                    XmlParser.parsePlugins(doc),
                    XmlParser.parseExcludedColumns(doc)
            );
        } catch (Exception e) {
            throw new Db2springException("Failed to load config: " + filePath, e);
        }
    }
}