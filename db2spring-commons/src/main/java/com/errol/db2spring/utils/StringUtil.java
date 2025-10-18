package com.errol.db2spring.utils;

import lombok.experimental.UtilityClass;

import java.util.Locale;
import java.util.regex.Pattern;

@UtilityClass
public class StringUtil {

    // --- Null, Empty, and Blank Checks ---

    public static boolean isNull(String str) {
        return str == null;
    }

    public static boolean isEmpty(String str) {
        // Safe check: str != null && str.isEmpty() is redundant if str.isEmpty() is called on a non-null str.
        return str != null && str.isEmpty();
    }

    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isNotNull(String str) {
        return str != null;
    }

    public static boolean isNotEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    public static boolean isNotBlank(String str) {
        return str != null && !str.trim().isEmpty();
    }

    // --- Default Value / Coalesce ---

    /**
     * Returns the string or a default value if the string is null.
     */
    public static String defaultIfNull(String str, String defaultStr) {
        return str == null ? defaultStr : str;
    }

    /**
     * Returns the string or a default value if the string is null or empty ("").
     */
    public static String defaultIfEmpty(String str, String defaultStr) {
        return isNull(str) || isEmpty(str) ? defaultStr : str;
    }

    /**
     * Returns the string or a default value if the string is null, empty (""), or contains only whitespace.
     */
    public static String defaultIfBlank(String str, String defaultStr) {
        return isBlank(str) ? defaultStr : str;
    }

    // --- Trimming and Cleaning ---

    /**
     * Removes control characters and whitespace from both ends of a String. Returns null if the input is null.
     */
    public static String trim(String str) {
        return isNull(str) ? null : str.trim();
    }

    /**
     * Trims a String and returns null if the resulting trimmed String is empty or null.
     */
    public static String trimToNull(String str) {
        String trimmed = trim(str);
        return isBlank(trimmed) ? null : trimmed;
    }

    /**
     * Trims a String and returns an empty String if the resulting trimmed String is empty or null.
     */
    public static String trimToEmpty(String str) {
        String trimmed = trim(str);
        return defaultIfNull(trimmed, "");
    }

    // --- Case Manipulation and Comparison ---

    /**
     * Compares two Strings ignoring case, handling nulls safely.
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        return isNull(str1) ? isNull(str2) : str1.equalsIgnoreCase(str2);
    }

    /**
     * Converts a String to upper case, handling nulls safely.
     */
    public static String upperCase(String str) {
        return isNull(str) ? null : str.toUpperCase(Locale.ROOT);
    }

    /**
     * Converts a String to lower case, handling nulls safely.
     */
    public static String lowerCase(String str) {
        return isNull(str) ? null : str.toLowerCase(Locale.ROOT);
    }

    /**
     * Capitalizes the first letter of a String, leaving the rest unchanged.
     */
    public static String capitalize(String str) {
        if (isBlank(str)) return str;
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    // --- Segmentation and Splitting ---

    /**
     * Extracts the portion of the string after the last occurrence of the separator.
     */
    public static String extractLastSegment(String value, String separator) {
        if (value == null || separator == null || separator.isEmpty()) return value;

        int lastIndex = value.lastIndexOf(separator);
        return lastIndex == -1 ? value : value.substring(lastIndex + separator.length());
    }

    /**
     * Splits a string by a literal separator.
     * Uses Pattern.quote to treat the separator as a literal string, not a regex.
     */
    public static String[] split(String value, String separator) {
        if (value == null) return new String[0];
        if (separator == null || separator.isEmpty()) return new String[]{value};

        // Ensure the separator is treated literally
        String escapedSeparator = Pattern.quote(separator);
        return value.split(escapedSeparator);
    }

    // --- Padding and Repeats ---

    /**
     * Repeats a string a specified number of times. Returns "" if count is 0 or less.
     */
    public static String repeat(String str, int count) {
        if (isNull(str) || count <= 0) {
            return "";
        }
        // Using String.repeat() for modern Java (requires Java 11+)
        // For compatibility with older Java versions, a loop/StringBuilder would be used.
        return str.repeat(count);
    }
}