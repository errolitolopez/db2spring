package com.errol.db2spring.utils.collection;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@UtilityClass
public final class MapUtil {

    /**
     * Retrieves the value associated with the given key from the map and converts it to a String.
     *
     * @param data the map to retrieve the value from, may be null
     * @param key  the key to look up
     * @return the string representation of the value, or null if the map or value is null
     */
    public static String getString(Map<String, Object> data, String key) {
        return Optional.ofNullable(data)
                .map(map -> map.get(key))
                .map(Object::toString)
                .orElse(null);
    }

    /**
     * Retrieves the value associated with the given key from the map and converts it to a String.
     * Returns a default value if the map or value is null.
     *
     * @param data         the map to retrieve the value from, may be null
     * @param key          the key to look up
     * @param defaultValue the value to return if the map or key is null
     * @return the string representation of the value, or defaultValue if the map or value is null
     */
    public static String getStringOrDefault(Map<String, Object> data, String key, String defaultValue) {
        return Optional.ofNullable(data)
                .map(map -> map.get(key))
                .map(Object::toString)
                .orElse(defaultValue);
    }

    /**
     * Retrieves the value associated with the given key from the map and casts it to the specified type.
     *
     * @param data the map to retrieve the value from, may be null
     * @param key  the key to look up
     * @param type the class type to cast the value to
     * @param <T>  the type to cast the value to
     * @return the value cast to the specified type, or null if the map, key, or type mismatch occurs
     */
    public static <T> T getAs(Map<String, Object> data, String key, Class<T> type) {
        return Optional.ofNullable(data)
                .map(map -> map.get(key))
                .filter(type::isInstance)
                .map(type::cast)
                .orElse(null);
    }

    /**
     * Retrieves the value associated with the given key from the map and applies a mapper function to it.
     *
     * @param data   the map to retrieve the value from, may be null
     * @param key    the key to look up
     * @param mapper a function to convert the value to the desired type
     * @param <T>    the type returned by the mapper function
     * @return the mapped value, or null if the map or value is null
     */
    public static <T> T getAs(Map<String, Object> data, String key, Function<Object, T> mapper) {
        return Optional.ofNullable(data)
                .map(map -> map.get(key))
                .map(mapper)
                .orElse(null);
    }

    /**
     * Converts a nullable list into a Map using a key extractor function.
     * Returns an empty map if input list is null.
     *
     * @param list      The input list
     * @param keyMapper Function to derive key from each element
     * @param <K>       Key type
     * @param <V>       Value type (element type)
     * @return Map with keys derived from keyMapper and values from list
     */
    public static <K, V> Map<K, V> toMap(List<V> list, Function<V, K> keyMapper) {
        return Optional.ofNullable(list)
                .orElseGet(List::of)
                .stream()
                .collect(Collectors.toMap(
                        keyMapper,
                        v -> v,
                        (existing, replacement) -> replacement  // last element wins
                ));
    }

    /**
     * Converts a nullable list into a Map using a key extractor and value mapper.
     * Returns an empty map if input list is null.
     *
     * @param list        The input list
     * @param keyMapper   Function to derive key from each element
     * @param valueMapper Function to derive value from each element
     * @param <K>         Key type
     * @param <V>         Element type
     * @param <R>         Value type
     * @return Map with keys and values derived from functions
     */
    public static <K, V, R> Map<K, R> toMap(List<V> list, Function<V, K> keyMapper, Function<V, R> valueMapper) {
        return Optional.ofNullable(list)
                .orElseGet(List::of)
                .stream()
                .collect(Collectors.toMap(
                        keyMapper,
                        valueMapper,
                        (existing, replacement) -> replacement  // last element wins
                ));
    }

    /**
     * Groups elements of a nullable list into a Map where the key is derived from a keyMapper,
     * and the value is a List of elements that share the same key.
     * Returns an empty map if the input list is null.
     *
     * @param list      The input list
     * @param keyMapper Function to derive the key from each element
     * @param <K>       Key type
     * @param <V>       Element type of the input list
     * @return Map with keys derived from keyMapper and values as lists of elements
     */
    public static <K, V> Map<K, List<V>> toListMap(List<V> list, Function<V, K> keyMapper) {
        return Optional.ofNullable(list)
                .orElseGet(List::of)
                .stream()
                .collect(Collectors.groupingBy(keyMapper));
    }

    /**
     * Groups elements of a nullable list into a Map where:
     * - Key is derived from keyMapper
     * - Value is a List of mapped values derived from valueMapper
     * Returns an empty map if input list is null.
     *
     * @param list        The input list
     * @param keyMapper   Function to derive the key from each element
     * @param valueMapper Function to derive the value from each element
     * @param <K>         Key type
     * @param <V>         Element type of the input list
     * @param <R>         Type of the mapped values
     * @return Map with keys derived from keyMapper and values as lists of mapped values
     */
    public static <K, V, R> Map<K, List<R>> toListMap(List<V> list,
                                                      Function<V, K> keyMapper,
                                                      Function<V, R> valueMapper) {
        return Optional.ofNullable(list)
                .orElseGet(List::of)
                .stream()
                .collect(Collectors.groupingBy(
                        keyMapper,
                        Collectors.mapping(valueMapper, Collectors.toList())
                ));
    }
}