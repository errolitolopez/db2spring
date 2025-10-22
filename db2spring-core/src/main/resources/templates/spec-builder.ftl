package ${packageSpecBuilder};

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jakarta.persistence.criteria.*;
import java.util.*;

/**
 * Fluent JPA Criteria Specification builder supporting nested groups and common predicates.
 *
 * @param <T> the entity type
 */
public class ${classNameSpecBuilder}<T> {

    private final Root<T> root;
    private final CriteriaBuilder cb;
    private final List<Predicate> predicates = new ArrayList<>();

    private ${classNameSpecBuilder}(Root<T> root, CriteriaBuilder cb) {
        this.root = root;
        this.cb = cb;
    }

    /** Creates a new ${classNameSpecBuilder} for the given root and CriteriaBuilder. */
    public static <T> ${classNameSpecBuilder}<T> of(Root<T> root, CriteriaBuilder cb) {
        return new ${classNameSpecBuilder}<>(root, cb);
    }

    // ---------------- Basic Predicates ----------------

    /** AND equals predicate. */
    public <V> ${classNameSpecBuilder}<T> andEqual(String field, V value) {
        if (value != null) predicates.add(cb.equal(root.get(field), value));
        return this;
    }

    /** OR equals predicate. */
    public <V> ${classNameSpecBuilder}<T> orEqual(String field, V value) {
        if (value != null) predicates.add(cb.or(cb.equal(root.get(field), value)));
        return this;
    }

    /** AND not equal predicate. */
    public <V> ${classNameSpecBuilder}<T> andNotEqual(String field, V value) {
        if (value != null) predicates.add(cb.notEqual(root.get(field), value));
        return this;
    }

    /** OR not equal predicate. */
    public <V> ${classNameSpecBuilder}<T> orNotEqual(String field, V value) {
        if (value != null) predicates.add(cb.or(cb.notEqual(root.get(field), value)));
        return this;
    }

    /** AND like predicate (case-insensitive). */
    public ${classNameSpecBuilder}<T> andLike(String field, String value) {
        if (value != null && !value.isBlank())
            predicates.add(cb.like(cb.lower(root.get(field)), "%" + value.trim().toLowerCase() + "%"));
        return this;
    }

    /** OR like predicate (case-insensitive). */
    public ${classNameSpecBuilder}<T> orLike(String field, String value) {
        if (value != null && !value.isBlank())
            predicates.add(cb.or(cb.like(cb.lower(root.get(field)), "%" + value.trim().toLowerCase() + "%")));
        return this;
    }

    /** AND flexible range: from and/or to can be null. */
    public <V extends Comparable<? super V>> ${classNameSpecBuilder}<T> andRange(String field, V from, V to) {
        Path<V> path = root.get(field);
        if (from != null) predicates.add(cb.greaterThanOrEqualTo(path, from));
        if (to != null) predicates.add(cb.lessThanOrEqualTo(path, to));
        return this;
    }

    /** OR flexible range: from and/or to can be null. */
    public <V extends Comparable<? super V>> ${classNameSpecBuilder}<T> orRange(String field, V from, V to) {
        Path<V> path = root.get(field);
        if (from != null) predicates.add(cb.or(cb.greaterThanOrEqualTo(path, from)));
        if (to != null) predicates.add(cb.or(cb.lessThanOrEqualTo(path, to)));
        return this;
    }

    /** AND strict between: both bounds must be non-null. */
    public <V extends Comparable<? super V>> ${classNameSpecBuilder}<T> andBetween(String field, V from, V to) {
        if (from != null && to != null) predicates.add(cb.between(root.get(field), from, to));
        return this;
    }

    /** OR strict between: both bounds must be non-null. */
    public <V extends Comparable<? super V>> ${classNameSpecBuilder}<T> orBetween(String field, V from, V to) {
        if (from != null && to != null) predicates.add(cb.or(cb.between(root.get(field), from, to)));
        return this;
    }

    /** AND true predicate. */
    public ${classNameSpecBuilder}<T> isTrue(String field) {
        predicates.add(cb.isTrue(root.get(field)));
        return this;
    }

    /** AND false predicate. */
    public ${classNameSpecBuilder}<T> isFalse(String field) {
        predicates.add(cb.isFalse(root.get(field)));
        return this;
    }

    /** AND in collection predicate. */
    public <V> ${classNameSpecBuilder}<T> andIn(String field, Collection<V> values) {
        if (values != null && !values.isEmpty()) predicates.add(root.get(field).in(values));
        return this;
    }

    /** OR in collection predicate. */
    public <V> ${classNameSpecBuilder}<T> orIn(String field, Collection<V> values) {
        if (values != null && !values.isEmpty()) predicates.add(cb.or(root.get(field).in(values)));
        return this;
    }

    /** AND not in collection predicate. */
    public <V> ${classNameSpecBuilder}<T> andNotIn(String field, Collection<V> values) {
        if (values != null && !values.isEmpty()) predicates.add(cb.not(root.get(field).in(values)));
        return this;
    }

    /** OR not in collection predicate. */
    public <V> ${classNameSpecBuilder}<T> orNotIn(String field, Collection<V> values) {
        if (values != null && !values.isEmpty()) predicates.add(cb.or(cb.not(root.get(field).in(values))));
        return this;
    }

    // ---------------- Nested Groups ----------------

    /** AND a nested group of predicates. */
    public ${classNameSpecBuilder}<T> andGroup(${classNameSpecBuilder}<T> group) {
        if (group != null && !group.predicates.isEmpty()) {
            predicates.add(cb.and(group.predicates.toArray(new Predicate[0])));
        }
        return this;
    }

    /** OR a nested group of predicates. */
    public ${classNameSpecBuilder}<T> orGroup(${classNameSpecBuilder}<T> group) {
        if (group != null && !group.predicates.isEmpty()) {
            predicates.add(cb.or(group.predicates.toArray(new Predicate[0])));
        }
        return this;
    }

    // ---------------- Build ----------------

    /** Builds the final AND combination of all predicates. */
    public Predicate build() {
        return cb.and(predicates.toArray(new Predicate[0]));
    }
}