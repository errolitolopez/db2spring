package com.errol.db2spring.xml;

import lombok.NoArgsConstructor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@NoArgsConstructor
public class XmlUtil {

    public static String getText(Element parent, String tag) {
        if (parent == null || tag == null || tag.isEmpty()) {
            return null;
        }

        NodeList nodes = parent.getElementsByTagName(tag);
        if (nodes.getLength() == 0) {
            return null;
        }

        Node node = nodes.item(0);
        if (node == null) {
            return null;
        }

        String text = node.getTextContent();
        return text != null ? text.trim() : null;
    }

    public static String getAttribute(Document doc, String tagName, String attributeName) {
        if (doc == null || tagName == null || attributeName == null) {
            return null;
        }

        NodeList nodes = doc.getElementsByTagName(tagName);
        if (nodes == null || nodes.getLength() == 0) {
            return null;
        }

        Node node = nodes.item(0);
        if (!(node instanceof Element element)) {
            return null;
        }

        String value = element.getAttribute(attributeName);
        return !value.isBlank() ? value.trim() : null;
    }

    public static <T> List<T> loadConfigs(Document doc, String tagName, Function<Element, T> mapper) {
        NodeList nodes = doc.getElementsByTagName(tagName);
        List<T> configs = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            configs.add(mapper.apply((Element) nodes.item(i)));
        }
        return configs;
    }

    public static String resolveVariables(String text, Map<String, String> variables) {
        if (text == null) {
            return null;
        }

        for (Map.Entry<String, String> entry : variables.entrySet()) {
            text = text.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        return text;
    }

    public static boolean safeParseBoolean(String value) {
        if (value == null || value.trim().isEmpty()) {
            return true;
        }
        String normalized = value.trim().toLowerCase();
        if ("true".equals(normalized)) {
            return true;
        } else if ("false".equals(normalized)) {
            return false;
        }
        return false;
    }

    public static Map<String, String> loadTagAttributes(Document doc, String tagName, String keyAttr, String valueAttr) {
        NodeList nodes = doc.getElementsByTagName(tagName);
        Map<String, String> result = new HashMap<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Element element = (Element) nodes.item(i);
            String key = element.getAttribute(keyAttr);
            String value = element.getAttribute(valueAttr);
            if (!key.isEmpty()) {
                result.put(key, value);
            }
        }
        return result;
    }
}
