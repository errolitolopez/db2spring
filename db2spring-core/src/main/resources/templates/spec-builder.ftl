package ${packageSpecBuilder};

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class ${classNameSpecBuilder}<T> {

    private final Root<T> root;
    private final CriteriaBuilder cb;
    private final List<Predicate> predicates = new ArrayList<>();

    private ${classNameSpecBuilder}(Root<T> root, CriteriaBuilder cb) {
        this.root = root;
        this.cb = cb;
    }

    public static <T> ${classNameSpecBuilder}<T> of(Root<T> root, CriteriaBuilder cb) {
        return new ${classNameSpecBuilder}<>(root, cb);
    }

    public <V> ${classNameSpecBuilder}<T> andEqual(String field, V value) {
        if (value != null) {
            predicates.add(cb.equal(root.get(field), value));
        }
        return this;
    }

    public ${classNameSpecBuilder}<T> andLike(String field, String value) {
        if (value != null && !value.isBlank()) {
            predicates.add(cb.like(cb.lower(root.get(field)), "%" + value.trim().toLowerCase() + "%"));
        }
        return this;
    }

    public <V extends Comparable<? super V>> ${classNameSpecBuilder}<T> andRange(String field, V from, V to) {
        Path<V> path = root.get(field);
        if (from != null) {
            predicates.add(cb.greaterThanOrEqualTo(path, from));
        }
        if (to != null) {
            predicates.add(cb.lessThanOrEqualTo(path, to));
        }
        return this;
    }

    public <V> ${classNameSpecBuilder}<T> orEqual(String field, V value) {
        if (value != null) {
            predicates.add(cb.or(cb.equal(root.get(field), value)));
        }
        return this;
    }

    public ${classNameSpecBuilder}<T> orLike(String field, String value) {
        if (value != null && !value.isBlank()) {
            predicates.add(cb.or(cb.like(cb.lower(root.get(field)), "%" + value.trim().toLowerCase() + "%")));
        }
        return this;
    }

    public Predicate build() {
        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
