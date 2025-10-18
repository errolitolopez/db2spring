package com.errol.db2spring.model;


import com.errol.db2spring.utils.SmartStringUtil;

public class SmartString {

    private final String value;

    public SmartString(String value) {
        this.value = value == null ? "" : value;
    }

    /**
     * Entry point for fluent API
     */
    public static SmartString of(String value) {
        return new SmartString(value);
    }

    /**
     * Get the underlying string
     */
    public String get() {
        return value;
    }

    // Removed the private splitWords() method as it's in SmartStringUtil now

    public SmartString toPascalCase() {
        // Delegate to SmartStringUtil
        return new SmartString(SmartStringUtil.toPascalCase(value));
    }

    public SmartString toCamelCase() {
        // Delegate to SmartStringUtil
        return new SmartString(SmartStringUtil.toCamelCase(value));
    }

    public SmartString toSnakeCase() {
        // Delegate to SmartStringUtil
        return new SmartString(SmartStringUtil.toSnakeCase(value));
    }

    public SmartString toUpperSnakeCase() {
        // Delegate to SmartStringUtil
        return new SmartString(SmartStringUtil.toUpperSnakeCase(value));
    }

    public SmartString toKebabCase() {
        // Delegate to SmartStringUtil
        return new SmartString(SmartStringUtil.toKebabCase(value));
    }

    public SmartString toSentenceCase() {
        // Delegate to SmartStringUtil
        return new SmartString(SmartStringUtil.toSentenceCase(value));
    }

    public SmartString toLowerSentenceCase() {
        // Delegate to SmartStringUtil
        return new SmartString(SmartStringUtil.toLowerSentenceCase(value));
    }

    public SmartString toPlural() {
        // Delegate to SmartStringUtil
        return new SmartString(SmartStringUtil.toPlural(value));
    }

    public SmartString toSingular() {
        // Delegate to SmartStringUtil
        return new SmartString(SmartStringUtil.toSingular(value));
    }

    public SmartString capFirst() {
        // Delegate to SmartStringUtil
        return new SmartString(SmartStringUtil.capFirst(value));
    }

    public SmartString uncapFirst() {
        // Delegate to SmartStringUtil
        return new SmartString(SmartStringUtil.uncapFirst(value));
    }

    @Override
    public String toString() {
        return value;
    }
}