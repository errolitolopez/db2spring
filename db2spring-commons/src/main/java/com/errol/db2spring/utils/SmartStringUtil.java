package com.errol.db2spring.utils;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

public class SmartStringUtil {

    /**
     * Splits a string into words based on common naming conventions:
     * - CamelCase/PascalCase (handles transitions from lowercase to uppercase)
     * - Acronyms (handles transitions from uppercase to lowercase, e.g., 'HTTPClient' -> 'HTTP', 'Client')
     * - Separators (replaces underscores and hyphens with spaces)
     *
     * @param input The string to split.
     * @return An array of separated words.
     */
    private static String[] splitWords(String input) {
        if (input == null || input.isEmpty()) {
            return new String[0];
        }

        String normalized = input
                // 1. Add a space before a lowercase letter followed by an uppercase letter (camelCase/PascalCase split)
                .replaceAll("([a-z])([A-Z])", "$1 $2")
                // 2. Add a space before an uppercase letter that is followed by a lowercase letter,
                //    but ONLY if it is preceded by another uppercase letter (handles acronyms like HTMLClient -> HTML Client)
                .replaceAll("([A-Z]+)([A-Z][a-z])", "$1 $2")
                // 3. Replace underscores and hyphens with spaces (snake_case/kebab-case split)
                .replaceAll("[_\\-]+", " ")
                .trim();

        // Split by any sequence of one or more whitespace characters
        return normalized.split("\\s+");
    }

    public static String toPascalCase(String input) {
        if (input == null || input.isEmpty()) return input;
        return Arrays.stream(splitWords(input))
                .map(word -> word.substring(0, 1).toUpperCase(Locale.ROOT) +
                        word.substring(1).toLowerCase(Locale.ROOT))
                .collect(Collectors.joining());
    }

    public static String toCamelCase(String input) {
        if (input == null || input.isEmpty()) return input;
        String pascal = toPascalCase(input);
        if (pascal.isEmpty()) return pascal;
        return pascal.substring(0, 1).toLowerCase(Locale.ROOT) + pascal.substring(1);
    }

    public static String toSnakeCase(String input) {
        if (input == null || input.isEmpty()) return input;
        return Arrays.stream(splitWords(input))
                .map(String::toLowerCase)
                .collect(Collectors.joining("_"));
    }

    public static String toUpperSnakeCase(String input) {
        if (input == null || input.isEmpty()) return input;
        return Arrays.stream(splitWords(input))
                .map(String::toUpperCase)
                .collect(Collectors.joining("_"));
    }

    public static String toKebabCase(String input) {
        if (input == null || input.isEmpty()) return input;
        return Arrays.stream(splitWords(input))
                .map(String::toLowerCase)
                .collect(Collectors.joining("-"));
    }

    public static String toSentenceCase(String input) {
        if (input == null || input.isEmpty()) return input;
        String[] words = splitWords(input);
        if (words.length == 0) return "";
        String sentence = Arrays.stream(words)
                .map(String::toLowerCase)
                .collect(Collectors.joining(" "));
        return sentence.substring(0, 1).toUpperCase(Locale.ROOT) + sentence.substring(1);
    }

    public static String toLowerSentenceCase(String input) {
        if (input == null || input.isEmpty()) return input;
        return Arrays.stream(splitWords(input))
                .map(String::toLowerCase)
                .collect(Collectors.joining(" "));
    }

    /**
     * NOTE: English pluralization is complex and these rules are highly simplified.
     * For production-grade inflection, consider using a dedicated library (e.g., Evo Inflector).
     */
    public static String toPlural(String input) {
        if (input == null || input.isEmpty()) return input;

        // Use the word's original case, but check the lowercased version for rules
        String lower = input.toLowerCase(Locale.ROOT);

        // y -> ies (only if preceded by a consonant, e.g., 'city' not 'boy')
        if (lower.endsWith("y") && !lower.matches(".*[aeiou]y$")) {
            return input.substring(0, input.length() - 1) + "ies";
        }

        // s, sh, ch, x, z -> es (e.g., 'box' -> 'boxes', 'church' -> 'churches')
        if (lower.endsWith("s") || lower.endsWith("sh") ||
                lower.endsWith("ch") || lower.endsWith("x") || lower.endsWith("z")) {
            return input + "es";
        }

        // default: just add 's'
        return input + "s";
    }

    /**
     * NOTE: English singularization is complex and these rules are highly simplified.
     * For production-grade inflection, consider using a dedicated library (e.g., Evo Inflector).
     */
    public static String toSingular(String word) {
        if (word == null || word.isEmpty()) return word;

        // Use the word's original case, but check the lowercased version for rules
        String lower = word.toLowerCase(Locale.ROOT);

        // ies -> y (e.g., 'cities' -> 'city')
        if (lower.endsWith("ies")) {
            return word.substring(0, word.length() - 3) + "y";
        }

        // Remove 'es' for sibilant-ending plurals (fixes 'classes' -> 'class')
        if (lower.endsWith("ses") || lower.endsWith("shes") || lower.endsWith("ches") ||
                lower.endsWith("xes") || lower.endsWith("zes")) {
            return word.substring(0, word.length() - 2);
        }

        // general plural 's' -> remove 's' (e.g., 'tables' -> 'table')
        // *IMPORTANT: This is still error-prone (e.g., 'news' -> 'new'), but aligns with simple, general rules.
        if (lower.endsWith("s") && word.length() > 1) {
            return word.substring(0, word.length() - 1);
        }
        return word;
    }

    public static String capFirst(String input) {
        if (input == null || input.isEmpty()) return input;
        return Character.toUpperCase(input.charAt(0)) + input.substring(1);
    }

    public static String uncapFirst(String input) {
        if (input == null || input.isEmpty()) return input;
        return Character.toLowerCase(input.charAt(0)) + input.substring(1);
    }
}
