package com.errol.db2spring.utils.collection;

import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor
public final class CollectionUtil {

    public static boolean isNull(Collection<?> collection) {
        return collection == null;
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection != null && collection.isEmpty();
    }

    public static boolean isNotNull(Collection<?> collection) {
        return collection != null;
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }

    public static boolean isBlank(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotBlank(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }

    public static boolean isNull(Map<?, ?> map) {
        return map == null;
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map != null && map.isEmpty();
    }

    public static boolean isNotNull(Map<?, ?> map) {
        return map != null;
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return map != null && !map.isEmpty();
    }


    @SafeVarargs
    private static <T> Stream<T> getFilteredStream(List<T> list, Predicate<T>... predicates) {
        // Optimization: Use a cleaner check for empty predicates
        Predicate<T> combinedPredicate = (predicates.length == 0)
                ? t -> true
                : Stream.of(predicates).reduce(Predicate::and).get();

        return Optional.ofNullable(list).orElseGet(List::of)
                .stream()
                .filter(combinedPredicate);
    }


    public static <T> List<T> findAll(List<T> list) {
        return getFilteredStream(list).collect(Collectors.toList());
    }

    @SafeVarargs
    public static <T> List<T> findAll(List<T> list, Predicate<T>... predicates) {
        return getFilteredStream(list, predicates).collect(Collectors.toList());
    }


    public static <T> Optional<T> findFirst(List<T> list) {
        return getFilteredStream(list).findFirst();
    }

    @SafeVarargs
    public static <T> Optional<T> findFirst(List<T> list, Predicate<T>... predicates) {
        return getFilteredStream(list, predicates).findFirst();
    }

    public static <T, R> List<R> findValuesAsList(List<T> list, Function<T, R> mapper) {
        return getFilteredStream(list).map(mapper).collect(Collectors.toList());
    }

    @SafeVarargs
    public static <T, R> List<R> findValuesAsList(List<T> list, Function<T, R> mapper, Predicate<T>... predicates) {
        return getFilteredStream(list, predicates).map(mapper).collect(Collectors.toList());
    }


    public static <T, R> Set<R> findValuesAsSet(List<T> list, Function<T, R> mapper) {
        return getFilteredStream(list).map(mapper).collect(Collectors.toSet());
    }

    @SafeVarargs
    public static <T, R> Set<R> findValuesAsSet(List<T> list, Function<T, R> mapper, Predicate<T>... predicates) {
        return getFilteredStream(list, predicates).map(mapper).collect(Collectors.toSet());
    }

    public static <T, R> Optional<R> findFirstAndMap(List<T> list, Function<T, R> mapper) {
        return findFirst(list).map(mapper);
    }

    @SafeVarargs
    public static <T, R> Optional<R> findFirstAndMap(List<T> list, Function<T, R> mapper, Predicate<T>... predicates) {
        return findFirst(list, predicates).map(mapper);
    }

    public static <T> boolean noneMatch(List<T> list, Predicate<T> predicate) {
        if (CollectionUtil.isBlank(list)) {
            return true;
        }
        return getFilteredStream(list, predicate).noneMatch(t -> true);
    }

    @SafeVarargs
    public static <T> boolean noneMatch(List<T> list, Predicate<T>... predicates) {
        if (CollectionUtil.isBlank(list)) {
            return true;
        }

        return getFilteredStream(list, predicates).noneMatch(t -> true);
    }

    public static <T> boolean anyMatch(List<T> list, Predicate<T> predicate) {
        if (CollectionUtil.isBlank(list)) {
            return true;
        }
        return getFilteredStream(list, predicate).anyMatch(t -> true);
    }

    @SafeVarargs
    public static <T> boolean anyMatch(List<T> list, Predicate<T>... predicates) {
        if (CollectionUtil.isBlank(list)) {
            return false;
        }
        return getFilteredStream(list, predicates).anyMatch(t -> true);
    }
}
